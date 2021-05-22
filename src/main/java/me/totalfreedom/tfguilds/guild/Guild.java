package me.totalfreedom.tfguilds.guild;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.TFGuilds;
import me.totalfreedom.tfguilds.config.ConfigEntry;
import me.totalfreedom.tfguilds.util.GUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Guild
{

    private static Map<String, Guild> guilds = new HashMap<>();
    //
    private final String id;
    private final String name;
    private final long createdAt;
    private List<Player> invites = new ArrayList<>();
    private UUID owner;
    private List<UUID> moderators;
    private List<UUID> members;
    private Map<String, List<UUID>> ranks;
    private Map<String, Location> warps;
    private String defaultRank;
    private String tag;
    private State state;
    private String motd;
    private Location home;

    public Guild(String name,
                 UUID owner,
                 List<UUID> moderators,
                 List<UUID> members,
                 Map<String, List<UUID>> ranks,
                 Map<String, Location> warps,
                 String defaultRank,
                 State state,
                 String motd,
                 double x,
                 double y,
                 double z,
                 String world,
                 long createdAt)
    {
        this.id = name.toLowerCase().replaceAll(" ", "_");
        this.name = name;
        this.owner = owner;
        this.moderators = moderators;
        this.members = members;
        this.ranks = ranks;
        this.warps = warps;
        this.defaultRank = defaultRank;
        this.state = state;
        this.motd = motd;
        World w;
        if (world == null)
        {
            w = Bukkit.getWorlds().get(0);
        }
        else
        {
            w = Bukkit.getWorld(world);
        }
        if (w == null)
        {
            w = Bukkit.getWorlds().get(0);
        }
        this.home = new Location(w, x, y, z);
        this.createdAt = createdAt;
    }

    public Guild(Player player, String name)
    {
        this(name,
                player.getUniqueId(),
                new ArrayList<>(),
                new ArrayList<>(),
                new HashMap<>(),
                new HashMap<>(),
                null,
                State.OPEN,
                null,
                0,
                50,
                0,
                null,
                System.currentTimeMillis());
        save(true);
    }

    public static void create(Player player, String name)
    {
        guilds.put(name.toLowerCase().replaceAll(" ", "_"),
                new Guild(player, name));
    }

    public static boolean isAlreadyMember(Player player)
    {
        for (Guild guild : guilds.values())
        {
            if (guild.isMember(player))
            {
                return true;
            }
        }
        return false;
    }

    public static void loadAll()
    {
        Connection connection = TFGuilds.getPlugin().getSQL().getConnection();
        try
        {
            ResultSet set = connection.prepareStatement("SELECT * FROM guilds;").executeQuery();
            while (set.next())
            {
                String id = set.getString("id");
                UUID owner = User.getUserFromId(set.getInt("owner")).getUuid();
                List<UUID> moderators = new ArrayList<>();
                if (set.getString("moderators") != null)
                {
                    for (String string : set.getString("moderators").split(","))
                    {
                        User user = User.getUserFromId(Integer.parseInt(string));
                        if (user != null)
                        {
                            moderators.add(user.getUuid());
                        }
                    }
                }
                List<UUID> members = new ArrayList<>();
                members.add(owner);
                if (set.getString("members") != null)
                {
                    for (String string : set.getString("members").split(","))
                    {
                        User user = User.getUserFromId(Integer.parseInt(string));
                        if (user != null)
                        {
                            members.add(user.getUuid());
                        }
                    }
                }
                Map<String, List<UUID>> ranks = new HashMap<>();
                PreparedStatement rankStatement = connection.prepareStatement("SELECT * FROM ranks WHERE guild_id=?;");
                rankStatement.setString(1, id);
                ResultSet rankSet = rankStatement.executeQuery();
                while (rankSet.next())
                {
                    List<UUID> rankMembers = new ArrayList<>();
                    if (rankSet.getString("members") != null)
                    {
                        for (String string : rankSet.getString("members").split(","))
                        {
                            if (string == null || string.isEmpty())
                            {
                                break;
                            }
                            User user = User.getUserFromId(Integer.parseInt(string));
                            if (user != null)
                            {
                                rankMembers.add(user.getUuid());
                            }
                        }
                    }
                    ranks.put(rankSet.getString("name"), rankMembers);
                }
                Map<String, Location> warps = new HashMap<>();
                PreparedStatement warpStatement = connection.prepareStatement("SELECT * FROM warps WHERE guild_id=?;");
                warpStatement.setString(1, id);
                ResultSet warpSet = warpStatement.executeQuery();
                while (warpSet.next())
                {
                    World w = Bukkit.getWorld(warpSet.getString("world"));
                    if (w == null)
                    {
                        continue;
                    }
                    double x = warpSet.getDouble("x");
                    double y = warpSet.getDouble("y");
                    double z = warpSet.getDouble("z");
                    Location location = new Location(w, x, y, z);
                    warps.put(set.getString("name"), location);
                }
                Guild guild = new Guild(set.getString("name"),
                        owner,
                        moderators,
                        members,
                        ranks,
                        warps,
                        set.getString("default_rank"),
                        State.fromInt(set.getInt("state")),
                        set.getString("motd"),
                        set.getDouble("x"),
                        set.getDouble("y"),
                        set.getDouble("z"),
                        set.getString("world"),
                        set.getLong("creation"));
                guilds.put(id, guild);
            }
            TFGuilds.getPlugin().getLogger().info(guilds.size() + " guilds loaded!");
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    public static Guild getGuild(Player player)
    {
        for (Guild guild : guilds.values())
        {
            if (guild.isMember(player))
            {
                return guild;
            }
        }
        return null;
    }

    public static Guild getGuild(String string)
    {
        for (Guild guild : guilds.values())
        {
            if (guild.getName().equalsIgnoreCase(string))
            {
                return guild;
            }
        }
        return guilds.get(string) != null ? guilds.get(string) : null;
    }

    public static boolean hasGuild(String string)
    {
        for (Guild guild : guilds.values())
        {
            if (guild.getName().equalsIgnoreCase(string))
            {
                return true;
            }
        }
        return guilds.get(string) != null;
    }

    public static List<String> getGuildNames()
    {
        List<String> names = new ArrayList<>();
        guilds.values().forEach(guild ->
                names.add(guild.getName()));
        return names;
    }

    public void addMember(Player player)
    {
        if (!isMember(player))
        {
            members.add(player.getUniqueId());
            save();
        }
    }

    public void removeMember(Player player)
    {
        if (isMember(player))
        {
            if (isModerator(player))
            {
                moderators.remove(player);
            }
            members.remove(player.getUniqueId());
            save();
        }
    }

    public boolean isMember(Player player)
    {
        return members.contains(player.getUniqueId()) || owner.equals(player.getUniqueId());
    }

    public void addModerator(Player player)
    {
        if (!isModerator(player))
        {
            moderators.add(player.getUniqueId());
            save();
        }
    }

    public void removeModerator(Player player)
    {
        if (isModerator(player))
        {
            moderators.remove(player.getUniqueId());
            save();
        }
    }

    public boolean isModerator(Player player)
    {
        return moderators.contains(player.getUniqueId()) || owner.equals(player.getUniqueId());
    }

    public void addWarp(String name, Location location)
    {
        if (!hasWarp(name))
        {
            warps.put(name, location);
            saveWarp(name);
        }
    }

    public void removeWarp(String name)
    {
        if (hasWarp(name))
        {
            warps.remove(name);
            Connection connection = TFGuilds.getPlugin().getSQL().getConnection();
            try
            {
                PreparedStatement statement = connection.prepareStatement("DELETE FROM warps WHERE guild_id=? AND name=?");
                statement.setString(1, id);
                statement.setString(2, name);
                statement.execute();
            }
            catch (SQLException ex)
            {
                ex.printStackTrace();
            }
        }
    }

    public boolean hasWarp(String name)
    {
        return warps.get(name) != null;
    }

    public void createRank(String name)
    {
        ranks.put(name.toLowerCase().replaceAll(" ", "_"), new ArrayList<>());
        Connection connection = TFGuilds.getPlugin().getSQL().getConnection();
        try
        {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO ranks (guild_id, name, members) VALUES (?, ?, ?)");
            statement.setString(1, id);
            statement.setString(2, name);
            statement.setString(3, null);
            statement.execute();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    public void deleteRank(String name)
    {
        if (hasRank(name))
        {
            ranks.remove(name);
            Connection connection = TFGuilds.getPlugin().getSQL().getConnection();
            try
            {
                PreparedStatement statement = connection.prepareStatement("DELETE FROM ranks WHERE guild_id=? AND name=?");
                statement.setString(1, id);
                statement.setString(2, name);
                statement.execute();
            }
            catch (SQLException ex)
            {
                ex.printStackTrace();
            }
        }
    }

    public boolean hasRank(String name)
    {
        return ranks.get(name) != null;
    }

    public void setPlayerRank(Player player, String name)
    {
        if (hasRank(name))
        {
            String str = getPlayerRank(player);
            if (!str.equals(name) && !str.equals(defaultRank))
            {
                removePlayerRank(player, str);
            }
            List<UUID> list = ranks.get(name);
            list.add(player.getUniqueId());
            ranks.put(name, list);
            saveRankMembers(name);
        }
    }

    public void removePlayerRank(Player player, String name)
    {
        if (hasRank(name))
        {
            List<UUID> list = ranks.get(name);
            list.remove(player.getUniqueId());
            ranks.put(name, list);
            saveRankMembers(name);
        }
    }

    public String getPlayerRank(Player player)
    {
        for (String rank : ranks.keySet())
        {
            if (ranks.get(rank).contains(player.getUniqueId()))
            {
                return rank;
            }
        }
        return defaultRank;
    }

    public String getModeratorIds()
    {
        List<Integer> mod = new ArrayList<>();
        moderators.forEach(uuid ->
                mod.add(User.getUserFromUuid(uuid).getId()));
        return StringUtils.join(mod, ",");
    }

    public String getMemberIds()
    {
        List<Integer> mem = new ArrayList<>();
        members.forEach(member ->
                mem.add(User.getUserFromUuid(member).getId()));
        return StringUtils.join(mem, ",");
    }

    public String getMotd()
    {
        return GUtil.colorize(motd);
    }

    public void setMotd(String motd)
    {
        this.motd = motd;
    }

    public void invite(Player player)
    {
        invites.add(player);
    }

    public void removeInvite(Player player)
    {
        invites.remove(player);
    }

    public boolean isInvited(Player player)
    {
        return invites.contains(player);
    }

    public void save(boolean newSave)
    {
        Connection connection = TFGuilds.getPlugin().getSQL().getConnection();
        try
        {
            PreparedStatement statement = newSave ? connection.prepareStatement("INSERT INTO guilds VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")
                    : connection.prepareStatement("UPDATE guilds SET owner=?," +
                    "moderators=?," +
                    "members=?," +
                    "tag=?," +
                    "default_rank=?," +
                    "state=?," +
                    "motd=?," +
                    "x=?," +
                    "y=?," +
                    "z=?," +
                    "world=? WHERE id=?");
            if (newSave)
            {
                statement.setString(1, id);
                statement.setString(2, name);
                statement.setInt(3, User.getUserFromUuid(owner).getId());
                statement.setString(4, null);
                statement.setString(5, null);
                statement.setString(6, tag);
                statement.setString(7, defaultRank);
                statement.setInt(8, state.ordinal());
                statement.setString(9, motd);
                statement.setDouble(10, home.getX());
                statement.setDouble(11, home.getY());
                statement.setDouble(12, home.getZ());
                statement.setString(13, home.getWorld().getName());
                statement.setLong(14, createdAt);
            }
            else
            {
                statement.setInt(1, User.getUserFromUuid(owner).getId());
                statement.setString(2, moderators.isEmpty() ? null : getModeratorIds());
                statement.setString(3, members.isEmpty() ? null : getMemberIds());
                statement.setString(4, tag);
                statement.setString(5, defaultRank);
                statement.setInt(6, state.ordinal());
                statement.setString(7, motd);
                statement.setDouble(8, home.getX());
                statement.setDouble(9, home.getY());
                statement.setDouble(10, home.getZ());
                statement.setString(11, home.getWorld().getName());
                statement.setString(12, id);
            }
            statement.execute();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    public void save()
    {
        save(false);
    }

    public void saveRankMembers(String name)
    {
        if (hasRank(name))
        {
            Connection connection = TFGuilds.getPlugin().getSQL().getConnection();
            try
            {
                PreparedStatement statement = connection.prepareStatement("UPDATE ranks SET members=? WHERE guild_id=? AND name=?");
                statement.setString(1, ranks.get(name).isEmpty() ? null : getMemberIdsByRank(name));
                statement.setString(2, id);
                statement.setString(3, name);
                statement.execute();
            }
            catch (SQLException ex)
            {
                ex.printStackTrace();
            }
        }
    }

    public void saveWarp(String name)
    {
        if (hasWarp(name))
        {
            Connection connection = TFGuilds.getPlugin().getSQL().getConnection();
            try
            {
                PreparedStatement statement = connection.prepareStatement("INSERT INTO warps VALUES (?, ?, ?, ?, ?, ?)");
                statement.setString(1, id);
                statement.setString(2, name);
                Location location = warps.get(name);
                statement.setDouble(3, location.getX());
                statement.setDouble(4, location.getY());
                statement.setDouble(5, location.getZ());
                statement.setString(6, location.getWorld().getName());
                statement.execute();
            }
            catch (SQLException ex)
            {
                ex.printStackTrace();
            }
        }
    }

    public String getMemberIdsByRank(String name)
    {
        if (hasRank(name))
        {
            ArrayList<Integer> rankMembers = new ArrayList<>();
            ranks.get(name).forEach(uuid ->
                    rankMembers.add(User.getUserFromUuid(uuid).getId()));
            return StringUtils.join(rankMembers, ",");
        }
        return null;
    }

    public String getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public UUID getOwner()
    {
        return owner;
    }

    public void setOwner(Player player)
    {
        members.add(owner);
        owner = player.getUniqueId();
        members.remove(player.getUniqueId());
        save();
    }

    public List<UUID> getModerators()
    {
        return moderators;
    }

    public List<String> getModeratorNames()
    {
        List<String> names = new ArrayList<>();
        moderators.forEach(uuid ->
                names.add(Bukkit.getOfflinePlayer(uuid).getName()));
        return names;
    }

    public List<UUID> getMembers()
    {
        return members;
    }

    public List<String> getMemberOnlyNames()
    {
        List<String> names = new ArrayList<>();
        for (UUID uuid : members)
        {
            if (!moderators.contains(uuid) && !uuid.equals(owner))
            {
                names.add(Bukkit.getOfflinePlayer(uuid).getName());
            }
        }
        return names;
    }

    public List<String> getNamesByRank(String name)
    {
        List<String> names = new ArrayList<>();
        if (hasRank(name))
        {
            ranks.get(name).forEach(uuid ->
                    names.add(Bukkit.getOfflinePlayer(uuid).getName()));
        }
        return names;
    }

    public Map<String, List<UUID>> getRanks()
    {
        return ranks;
    }

    public List<String> getRankNames()
    {
        List<String> names = new ArrayList<>();
        ranks.keySet().forEach(rank ->
                names.add(rank));
        return names;
    }

    public Map<String, Location> getWarps()
    {
        return warps;
    }

    public List<String> getWarpNames()
    {
        List<String> names = new ArrayList<>();
        warps.keySet().forEach(name ->
                names.add(name));
        return names;
    }

    public Location getWarp(String name)
    {
        if (hasWarp(name))
        {
            return warps.get(name);
        }
        return null;
    }

    public String getDefaultRank()
    {
        return defaultRank;
    }

    public void setDefaultRank(String defaultRank)
    {
        this.defaultRank = defaultRank;
        save();
    }

    public String getTag()
    {
        return tag;
    }

    public void setTag(String tag)
    {
        this.tag = tag;
        save();
    }

    public State getState()
    {
        return state;
    }

    public void setState(State state)
    {
        this.state = state;
        save();
    }

    public Location getHome()
    {
        return home;
    }

    public void setHome(Location home)
    {
        this.home = home;
        save();
    }

    public void disband()
    {
        if (hasGuild(name))
        {
            Connection connection = TFGuilds.getPlugin().getSQL().getConnection();
            try
            {
                PreparedStatement ranks = connection.prepareStatement("DELETE FROM ranks WHERE guild_id=?");
                ranks.setString(1, id);
                ranks.execute();
                PreparedStatement warps = connection.prepareStatement("DELETE FROM warps WHERE guild_id=?");
                warps.setString(1, id);
                warps.execute();
                PreparedStatement guild = connection.prepareStatement("DELETE FROM guilds WHERE id=?");
                guild.setString(1, id);
                guild.execute();
                guilds.remove(id);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

    public void broadcast(String message)
    {
        for (Player player : Bukkit.getOnlinePlayers())
        {
            if (isMember(player))
            {
                player.sendMessage(message);
            }
        }
    }

    public void chat(Player player, String message)
    {
        broadcast(GUtil.colorize("&7[&bGuild Chat &7| &b" + name + "&7] " + player.getName() + " &8\u00BB &6") + message);

        if (ConfigEntry.GUILD_CHAT_LOGGING.getBoolean())
        {
            Bukkit.getServer().getLogger().info("[Guild Chat | " + name + "] " + player.getName() + " \u00BB " + message);
        }

        for (Player p : Bukkit.getOnlinePlayers())
        {
            if (Common.GUILD_CHAT_SPY.contains(p) && player != p)
            {
                p.sendMessage(GUtil.colorize("&7[&bGuild Chat Spy &7| &b" + name + "&7] " + player.getName() + " &8\u00BB &6") + message);
            }
        }
    }

    public String getRoster()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(ChatColor.AQUA).append("Guild Roster for ").append(ChatColor.GOLD).append(name).append("\n")
                .append(ChatColor.GRAY).append("Owner: ")
                .append(ChatColor.GOLD).append(Bukkit.getOfflinePlayer(owner).getName()).append("\n")
                .append(ChatColor.GRAY).append("Moderators: ")
                .append(ChatColor.GOLD).append(StringUtils.join(getModeratorNames(), ", ")).append("\n");

        for (String rank : ranks.keySet())
        {
            builder.append(ChatColor.GRAY).append(rank).append(": ")
                    .append(ChatColor.GOLD).append(StringUtils.join(getNamesByRank(rank), ", ")).append("\n");
        }

        builder.append(ChatColor.GRAY).append("Members: ")
                .append(ChatColor.GOLD).append(StringUtils.join(getMemberOnlyNames(), ", "));
        return builder.toString();
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(ChatColor.AQUA).append("Guild Information").append("\n").append(ChatColor.GRAY)
                .append(" Identifier: ").append(ChatColor.GOLD).append(id).append("\n")
                .append(ChatColor.GRAY).append(" Name: ").append(ChatColor.GOLD).append(name).append("\n")
                .append(ChatColor.GRAY).append(" Owner: ").append(ChatColor.GOLD).append(Bukkit.getOfflinePlayer(owner).getName()).append("\n")
                .append(ChatColor.GRAY).append(" Moderators (").append(getModerators().size()).append("): ").append(ChatColor.GOLD).append(StringUtils.join(getModeratorNames(), ", ")).append("\n")
                .append(ChatColor.GRAY).append(" Members (").append(getMembers().size()).append("): ").append(ChatColor.GOLD).append(StringUtils.join(getMemberOnlyNames(), ", ")).append("\n")
                .append(ChatColor.GRAY).append(" Tag: ").append(ChatColor.GOLD).append(tag != null ? GUtil.colorize(tag) : "None").append("\n")
                .append(ChatColor.GRAY).append(" State: ").append(ChatColor.GOLD).append(state.name()).append("\n")
                .append(ChatColor.GRAY).append(" Ranks (").append(ranks.size()).append("): ").append(ChatColor.GOLD).append(StringUtils.join(getRankNames(), ", ")).append("\n")
                .append(ChatColor.GRAY).append(" Created At: ").append(ChatColor.GOLD).append(GUtil.formatTime(createdAt));
        return builder.toString();
    }

    public long getCreatedAt()
    {
        return createdAt;
    }

    public enum State
    {
        OPEN,
        INVITE_ONLY,
        CLOSED;

        public static State fromString(String string)
        {
            if (string.contains(" "))
            {
                string = string.replaceAll(" ", "_");
            }
            try
            {
                return valueOf(string.toUpperCase());
            }
            catch (Exception ex)
            {
            }
            return null;
        }

        public static State fromInt(int value)
        {
            for (State state : values())
            {
                if (state.ordinal() == value)
                {
                    return state;
                }
            }
            return null;
        }
    }
}
