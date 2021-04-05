package me.totalfreedom.tfguilds.guild;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.TFGuilds;
import me.totalfreedom.tfguilds.config.ConfigEntry;
import me.totalfreedom.tfguilds.util.GLog;
import me.totalfreedom.tfguilds.util.GUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class Guild
{
    private static final TFGuilds plugin = TFGuilds.getPlugin();

    @Getter
    @Setter
    private String identifier;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private UUID owner;

    @Getter
    private final List<UUID> moderators;

    @Getter
    private final List<UUID> members;

    @Getter
    @Setter
    private String tag;

    @Getter
    @Setter
    private GuildState state;

    @Getter
    private final List<GuildRank> ranks;

    @Getter
    @Setter
    private String motd;

    @Getter
    @Setter
    private Location home;

    @Getter
    @Setter
    private String defaultRank;

    @Getter
    private final long creation;

    public Guild(String identifier,
                 String name,
                 UUID owner,
                 List<UUID> members,
                 List<UUID> moderators,
                 String tag,
                 GuildState state,
                 List<GuildRank> ranks,
                 String motd,
                 Location home,
                 long creation,
                 String defaultrank)
    {
        this.identifier = identifier;
        this.name = name;
        this.owner = owner;
        this.members = members;
        this.moderators = moderators;
        this.tag = tag;
        this.state = state;
        this.ranks = ranks;
        this.motd = motd;
        this.home = home;
        this.creation = creation;
        this.defaultRank = defaultrank;
    }

    public void save()
    {
        plugin.guildData.save(this);
    }

    public void addMember(UUID uuid)
    {
        if (hasDefaultRank())
        {
            getRank(defaultRank).getMembers().add(uuid);
        }

        members.add(uuid);
    }

    public void removeMember(UUID uuid)
    {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null)
        {
            Common.IN_GUILD_CHAT.remove(player);
        }

        members.remove(uuid);
        for (GuildRank gr : getRanks())
        {
            getRank(gr.getName()).getMembers().remove(uuid);
        }
        moderators.remove(uuid);
    }

    public boolean hasMember(UUID uuid)
    {
        return members.contains(uuid);
    }

    public void addModerator(UUID uuid)
    {
        moderators.add(uuid);
    }

    public void removeModerator(UUID uuid)
    {
        moderators.remove(uuid);
    }

    public boolean hasModerator(UUID uuid)
    {
        if (owner.equals(uuid))
        {
            return true;
        }

        return moderators.contains(uuid);
    }

    public void addRank(String name)
    {
        ranks.add(GuildRank.createGuildRank(identifier, GUtil.flatten(name), name));
    }

    public void removeRank(String name)
    {
        GuildRank remove = null;
        for (GuildRank rank : ranks)
        {
            if (GUtil.flatten(name).equals(rank.getIdentifier()))
            {
                remove = rank;
            }
        }

        if (remove == null)
        {
            return;
        }

        remove.delete();
        ranks.remove(remove);
    }

    public boolean hasRank(String name)
    {
        for (GuildRank rank : ranks)
        {
            if (GUtil.flatten(name).equals(rank.getIdentifier()))
            {
                return true;
            }
        }
        return false;
    }

    public GuildRank getRank(String name)
    {
        for (GuildRank rank : ranks)
        {
            if (GUtil.flatten(name).equals(rank.getIdentifier()))
            {
                return rank;
            }
        }
        return null;
    }

    public boolean hasTag()
    {
        return tag != null;
    }

    public boolean hasMOTD()
    {
        return motd != null;
    }

    public boolean hasHome()
    {
        return home != null;
    }

    public boolean hasDefaultRank()
    {
        return defaultRank != null;
    }

    public void broadcast(String message)
    {
        for (Player player : Bukkit.getOnlinePlayers())
        {
            if (members.contains(player.getUniqueId()))
            {
                player.sendMessage(message);
            }
        }
    }

    public List<String> getModeratorNames()
    {
        List<String> only = new ArrayList<>();
        for (UUID moderator : moderators)
        {
            if (moderator.equals(owner))
            {
                continue;
            }
            OfflinePlayer player = Bukkit.getOfflinePlayer(moderator);
            only.add(player.getName());
        }
        return only;
    }

    public List<String> getOnlyMembers()
    {
        List<String> only = new ArrayList<>();
        for (UUID member : members)
        {
            if (member.equals(owner) || moderators.contains(member))
            {
                continue;
            }
            OfflinePlayer player = Bukkit.getOfflinePlayer(member);
            only.add(player.getName());
        }
        return only;
    }

    public List<String> getRankNames()
    {
        List<String> names = new ArrayList<>();
        for (GuildRank rank : ranks)
        {
            names.add(rank.getName());
        }
        return names;
    }

    public String getRoster()
    {
        StringBuilder list = new StringBuilder(Common.PREFIX + "Guild Roster for " + name + "\n" +
                "%s%Owner%p% - " + Bukkit.getOfflinePlayer(owner).getName() + "\n" +
                "%s%Moderators%p% - " + StringUtils.join(getModeratorNames(), ", ") + "\n");

        for (GuildRank rank : ranks)
        {
            list.append("%s%")
                    .append(rank.getName()).append("%p% - ")
                    .append(StringUtils.join(rank.getMemberNames(), ", "))
                    .append("\n");
        }

        return Common.tl(list +
                "%s%Members%p% - " + StringUtils.join(getOnlyMembers(), ", "));
    }

    public static List<String> getGuildList()
    {
        List<String> g = new ArrayList<>();
        for (Guild guild : plugin.guilds.values())
        {
            g.add(guild.getName());
        }
        return g;
    }

    public static List<String> getGuildWarps()
    {
        List<String> warps = new ArrayList<>();
        for (GuildWarp warp : plugin.warps.values())
        {
            warps.add(warp.getWarpName());
        }
        return warps;
    }

    public GuildWarp getWarp(String warpName)
    {
        GuildWarp warp = plugin.warps.get(identifier);
        if (warp != null && warp.getWarpName().equalsIgnoreCase(warpName))
        {
            return warp;
        }
        return null;
    }

    public String getInformation()
    {
        return Common.tl(Common.PREFIX + "Guild Information\n" +
                "%s%Name%p%: " + name + "\n" +
                "%s%Owner%p%: " + Bukkit.getOfflinePlayer(owner).getName() + "\n" +
                "%s%Moderators%p%: " + StringUtils.join(getModeratorNames(), ", ") + "\n" +
                "%s%Members%p%: " + StringUtils.join(getOnlyMembers(), ", ") + "\n" +
                "%s%Tag%p%: " + (tag == null ? "None" : GUtil.colorize(tag)) + "\n" +
                "%s%State%p%: " + state.getDisplay() + "\n" +
                "%s%Ranks%p%: " + StringUtils.join(getRankNames(), ", ") + "\n" +
                "%s%Creation%p%: " + GUtil.format(creation) + "\n" +
                "%s%Identifier (Technical)%p%: " + identifier);
    }

    public void chat(String as, String msg)
    {
        broadcast(Common.tl("%s%[%p%Guild Chat %s%| %p%" + GUtil.colorize(name) + "%s%] %p%" + as + ChatColor.WHITE + ": %p%" + msg));

        if (ConfigEntry.GUILD_CHAT_LOGGING_ENABLED.getBoolean())
        {
            GLog.info(Common.tl("%s%[%p%Guild Chat %s%| %p%" + GUtil.colorize(name) + "%s%] %p%" + as + ChatColor.WHITE + ": %p%" + msg));
        }

        Player sender = Bukkit.getPlayer(as);

        for (Player player : Bukkit.getOnlinePlayers())
        {
            if (Common.CHAT_SPY.contains(player))
            {
                if (sender != null)
                {
                    if (player == sender)
                    {
                        continue;
                    }
                }
                player.sendMessage(GUtil.colorize("&7[GUILD CHAT SPY | " + GUtil.colorize(name) + "] " + as + ": " + msg));
            }
        }
    }

    public void disband()
    {
        for (UUID member : members)
        {
            Player player = Bukkit.getPlayer(member);
            if (player == null)
            {
                continue;
            }
            Common.IN_GUILD_CHAT.remove(player);
        }
        plugin.guildData.delete(this);
    }

    public void rename(String name)
    {
        String oldIdentifier = this.identifier;
        this.identifier = GUtil.flatten(name);
        updateRankIdentifiers();
        this.name = name;
        this.tag = GUtil.colorize("&8[&7" + name + "&8]");
        plugin.guildData.save(this, oldIdentifier);
    }

    public void updateRankIdentifiers()
    {
        for (GuildRank rank : ranks)
        {
            rank.updateGuildIdentifier(identifier);
        }
    }

    public static Guild createGuild(String identifier, String name, Player owner)
    {
        if (plugin.guildData.exists(identifier))
        {
            return getGuild(identifier);
        }

        Guild guild = plugin.guildData.create(identifier, name, owner);
        GLog.info(owner.getName() + " has created guild " + name);
        return guild;
    }

    public static Guild getGuild(String identifier)
    {
        return plugin.guilds.get(identifier);
    }

    public static Guild getGuild(Player player)
    {
        return plugin.guildData.get(player);
    }

    public static boolean guildExists(String identifier)
    {
        return plugin.guildData.exists(identifier);
    }

    public static boolean isInGuild(Player player)
    {
        return getGuild(player) != null;
    }

    public static GuildWarp createWarp(String identifier, String warpName, Player player)
    {
        return GuildWarp.createGuildWarp(identifier, warpName, player);
    }

    public boolean warpExists(String warpName)
    {
        return plugin.warpData.exists(identifier, warpName);
    }
}