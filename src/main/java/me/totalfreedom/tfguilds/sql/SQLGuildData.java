package me.totalfreedom.tfguilds.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import me.totalfreedom.tfguilds.TFGuilds;
import me.totalfreedom.tfguilds.guild.Guild;
import me.totalfreedom.tfguilds.guild.GuildRank;
import me.totalfreedom.tfguilds.guild.GuildState;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SQLGuildData
{
    private static final TFGuilds plugin = TFGuilds.getPlugin();
    private static final String TABLE_NAME = "guilds";

    private final String SELECT = "SELECT * FROM `" + TABLE_NAME + "` WHERE identifier=?";
    private final String SELECT_MEMBER = "SELECT * FROM `" + TABLE_NAME + "` WHERE members LIKE ?";
    private final String SELECT_ALL = "SELECT * FROM `" + TABLE_NAME + "`";
    private final String UPDATE = "UPDATE `" + TABLE_NAME + "` SET name=?, owner=?, members=?, moderators=?, tag=?, state=?, ranks=?, motd=?, x=?, y=?, z=?, world=?, default_rank=? WHERE identifier=?";
    private final String DELETE = "DELETE FROM `" + TABLE_NAME + "` WHERE identifier=?";
    private final String INSERT = "INSERT INTO `" + TABLE_NAME + "` (`identifier`, `name`, `owner`, `members`, " +
            "`moderators`, `tag`, `state`, `ranks`, `motd`, `x`, `y`, `z`, `world`, `default_rank`, `creation`) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

    public boolean exists(String identifier)
    {
        try (Connection connection = plugin.sql.getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(SELECT);
            statement.setString(1, identifier);
            ResultSet set = statement.executeQuery();
            return set.next();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return false;
    }

    public Guild get(String identifier)
    {
        try (Connection connection = plugin.sql.getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(SELECT);
            statement.setString(1, identifier);
            ResultSet set = statement.executeQuery();
            boolean next = set.next();
            if (!next) return null;
            String name = set.getString("name");
            UUID owner = plugin.userData.get(set.getInt("owner")).getUuid();
            List<UUID> members = new ArrayList<>();
            for (String stringMember : set.getString("members").split(","))
                members.add(plugin.userData.get(Integer.parseInt(stringMember)).getUuid());
            List<UUID> moderators = new ArrayList<>();
            if (set.getString("moderators") != null)
            {
                for (String stringModerator : set.getString("moderators").split(","))
                    moderators.add(plugin.userData.get(Integer.parseInt(stringModerator)).getUuid());
            }
            String tag = set.getString("tag");
            GuildState state = GuildState.values()[set.getInt("state")];
            List<GuildRank> ranks = new ArrayList<>();
            if (set.getString("ranks") != null)
            {
                for (String rankString : Arrays.asList(set.getString("ranks").split(",")))
                    ranks.add(plugin.rankData.get(identifier, rankString));
            }
            String motd = set.getString("motd");
            Location home = new Location(plugin.worldData.getWorld(set.getInt("world")),
                    set.getDouble("x"),
                    set.getDouble("y"),
                    set.getDouble("z"));
            String defaultRank = set.getString("default_rank");
            long creation = set.getLong("creation");
            return new Guild(identifier, name, owner, members, moderators, tag, state, ranks, motd, home, creation, defaultRank);
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    public Guild get(Player player)
    {
        for (Guild guild : getAll())
        {
            if (guild.getMembers().contains(player.getUniqueId()))
                return guild;
        }
        return null;
    }

    public List<Guild> getAll()
    {
        try (Connection connection = plugin.sql.getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(SELECT_ALL);
            ResultSet set = statement.executeQuery();
            List<Guild> guilds = new ArrayList<>();
            while (set.next())
                guilds.add(get(set.getString("identifier")));
            return guilds;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    /*
    Guild guild = new Guild(identifier,
                name,
                owner.getName(),
                Collections.singletonList(owner.getName()),
                new ArrayList<>(),
                ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + name + ChatColor.DARK_GRAY + "]",
                GuildState.INVITE_ONLY,
                new ArrayList<>(),
                null,
                null,
                System.currentTimeMillis(),
                null);
     */

    public Guild create(String identifier, String name, Player owner)
    {
        try (Connection connection = plugin.sql.getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(INSERT);
            statement.setString(1, identifier);
            statement.setString(2, name);
            int ownerID = plugin.userData.get(owner.getUniqueId()).getId();
            statement.setInt(3, ownerID);
            statement.setString(4, "" + ownerID);
            statement.setString(5, null);
            statement.setString(6, "&8[&7" + name + "&8]");
            statement.setInt(7, GuildState.INVITE_ONLY.ordinal());
            statement.setString(8, null);
            statement.setString(9, null);
            statement.setDouble(10, 0.0);
            statement.setDouble(11, 100.0);
            statement.setDouble(12, 0.0);
            statement.setInt(13, plugin.worldData.getWorldID(Bukkit.getWorlds().get(0)));
            statement.setString(14, null);
            long creation = System.currentTimeMillis();
            statement.setLong(15, creation);
            statement.execute();
            return new Guild(identifier, name, owner.getUniqueId(), Collections.singletonList(owner.getUniqueId()), new ArrayList<>(),
                    ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + name + ChatColor.DARK_GRAY + "]",
                    GuildState.INVITE_ONLY, new ArrayList<>(), null, new Location(Bukkit.getWorlds().get(0), 0.0, 100.0, 0.0), creation, null);
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    public void save(Guild guild)
    {
        try (Connection connection = plugin.sql.getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(UPDATE);
            statement.setString(1, guild.getName());
            statement.setString(2, "" + plugin.userData.get(guild.getOwner()).getId());
            List<String> members = new ArrayList<>();
            for (UUID member : guild.getMembers())
                members.add("" + plugin.userData.get(member).getId());
            statement.setString(3, members.size() == 0 ? null : StringUtils.join(members, ","));
            List<String> moderators = new ArrayList<>();
            for (UUID moderator : guild.getModerators())
                moderators.add("" + plugin.userData.get(moderator).getId());
            statement.setString(4, moderators.size() == 0 ? null : StringUtils.join(moderators, ","));
            statement.setString(5, guild.getTag());
            statement.setInt(6, guild.getState().ordinal());
            List<String> stringRanks = new ArrayList<>();
            for (GuildRank rank : guild.getRanks())
                stringRanks.add(rank.getIdentifier());
            statement.setString(7, stringRanks.size() == 0 ? null : StringUtils.join(stringRanks, ","));
            statement.setString(8, guild.getMotd());
            Location home = guild.getHome();
            statement.setDouble(9,  home == null ? 0.0 : home.getX());
            statement.setDouble(10, home == null ? 100.0 : home.getY());
            statement.setDouble(11, home == null ? 0.0 : home.getZ());
            statement.setInt(12, home == null ? plugin.worldData.getWorldID(Bukkit.getWorlds().get(0)) : plugin.worldData.getWorldID(home.getWorld()));
            statement.setString(13, guild.getDefaultRank());
            statement.setString(14, guild.getIdentifier());
            statement.execute();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    public void delete(Guild guild)
    {
        try (Connection connection = plugin.sql.getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(DELETE);
            statement.setString(1, guild.getIdentifier());
            statement.execute();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }
}