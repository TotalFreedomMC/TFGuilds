package me.totalfreedom.tfguilds.guild;

import lombok.Getter;
import lombok.Setter;
import me.totalfreedom.tfguilds.TFGuilds;
import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.util.GLog;
import me.totalfreedom.tfguilds.util.GUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Guild
{
    private static TFGuilds plugin = TFGuilds.getPlugin();

    @Getter
    @Setter
    private String identifier;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String owner;

    @Getter
    private List<String> moderators;

    @Getter
    private List<String> members;

    @Getter
    @Setter
    private String tag;

    @Getter
    @Setter
    private GuildState state;

    @Getter
    private List<GuildRank> ranks;

    @Getter
    @Setter
    private String motd;

    @Getter
    @Setter
    private Location home;

    @Getter
    private final long creation;

    public Guild(String identifier,
                 String name,
                 String owner,
                 List<String> members,
                 List<String> moderators,
                 String tag,
                 GuildState state,
                 List<GuildRank> ranks,
                 String motd,
                 Location home,
                 long creation)
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
    }

    public void save()
    {
        plugin.guilds.set(identifier + ".name", name);
        plugin.guilds.set(identifier + ".owner", owner);
        plugin.guilds.set(identifier + ".members", members);
        plugin.guilds.set(identifier + ".moderators", moderators);
        plugin.guilds.set(identifier + ".tag", tag);
        plugin.guilds.set(identifier + ".state", state.name());
        for (GuildRank rank : ranks)
            rank.set();
        plugin.guilds.set(identifier + ".motd", motd);
        plugin.guilds.set(identifier + ".home", home);
        plugin.guilds.set(identifier + ".creation", creation);
        plugin.guilds.save();
    }

    public void addMember(String name)
    {
        members.add(name);
    }

    public void removeMember(String name)
    {
        Player player = Bukkit.getPlayer(name);
        if (player != null)
            Common.IN_GUILD_CHAT.remove(player);
        members.remove(name);
        moderators.remove(name);
    }

    public boolean hasMember(String name)
    {
        return members.contains(name);
    }

    public void addModerator(String name)
    {
        moderators.add(name);
    }

    public void removeModerator(String name)
    {
        moderators.remove(name);
    }

    public boolean hasModerator(String name)
    {
        if (owner.equals(name))
            return true;
        return moderators.contains(name);
    }

    public void addRank(String name)
    {
        ranks.add(new GuildRank(identifier, GUtil.flatten(name), name, new ArrayList<>()));
    }

    public void removeRank(String name)
    {
        GuildRank remove = null;
        for (GuildRank rank : ranks)
        {
            if (GUtil.flatten(name).equals(rank.getIdentifier()))
                remove = rank;
        }
        if (remove == null)
            return;
        remove.delete();
        ranks.remove(remove);
    }

    public boolean hasRank(String name)
    {
        for (GuildRank rank : ranks)
        {
            if (GUtil.flatten(name).equals(rank.getIdentifier()))
                return true;
        }
        return false;
    }

    public GuildRank getRank(String name)
    {
        for (GuildRank rank : ranks)
        {
            if (GUtil.flatten(name).equals(rank.getIdentifier()))
                return rank;
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

    public void broadcast(String message)
    {
        for (Player player : Bukkit.getOnlinePlayers())
        {
            if (members.contains(player.getName()))
            {
                player.sendMessage(message);
            }
        }
    }

    public List<String> getOnlyMembers()
    {
        List<String> only = new ArrayList<>();
        for (String member : members)
        {
            if (member.equals(owner) || moderators.contains(member))
                continue;
            only.add(member);
        }
        return only;
    }

    public List<String> getRankNames()
    {
        List<String> names = new ArrayList<>();
        for (GuildRank rank : ranks)
            names.add(rank.getName());
        return names;
    }

    public String getRoster()
    {
        String list = Common.PREFIX + "Guild Roster\n" +
                "%s%Owner%p% - " + owner + "\n" +
                "%s%Moderators%p% - " + StringUtils.join(moderators, ", ") + "\n";

        for (GuildRank rank : ranks)
        {
            list += "%s%" + rank.getName() + "%p% - " + StringUtils.join(rank.getMembers(), ", ") + "\n";
        }

        return Common.tl(list +
                "%s%Members%p% - " + StringUtils.join(getOnlyMembers(), ", "));
    }

    public static List<String> getGuildList()
    {
        List<String> g = new ArrayList<>();
        for (String key : plugin.guilds.getKeys(false))
        {
            Guild guild = getGuild(key);
            g.add(GUtil.colorize(guild.getName()));
        }
        return g;
    }

    public String getInformation()
    {
        return Common.tl(Common.PREFIX + "Guild Information\n" +
                "%s%Name%p%: " + GUtil.colorize(name) + "\n" +
                "%s%Owner%p%: " + owner + "\n" +
                "%s%Moderators%p%: " + StringUtils.join(moderators, ", ") + "\n" +
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
        GLog.info(Common.tl("%s%[%p%Guild Chat %s%| %p%" + GUtil.colorize(name) + "%s%] %p%" + as + ChatColor.WHITE + ": %p%" + msg));
    }

    public void disband()
    {
        for (String member : members)
        {
            Player player = Bukkit.getPlayer(member);
            if (player == null)
                continue;
            Common.IN_GUILD_CHAT.remove(player);
        }
        plugin.guilds.set(identifier, null);
        plugin.guilds.save();
    }

    public static Guild createGuild(String identifier, String name, Player owner)
    {
        if (plugin.guilds.contains(identifier))
            return getGuild(identifier);
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
                System.currentTimeMillis());
        guild.save();
        GLog.info(owner.getName() + " has created guild " + name);
        return guild;
    }

    public static Guild getGuild(String identifier)
    {
        if (!plugin.guilds.contains(identifier))
            return null;
        List<GuildRank> ranks = new ArrayList<>();
        ConfigurationSection rankcs = plugin.guilds.getConfigurationSection(identifier + ".ranks");
        if (rankcs != null)
        {
            for (String key : rankcs.getKeys(false))
            {
                ranks.add(new GuildRank(identifier, key, plugin.guilds.getString(identifier + ".ranks." + key + ".name"),
                        plugin.guilds.getStringList(identifier + ".ranks." + key + ".members")));
            }
        }
        return new Guild(identifier,
                plugin.guilds.getString(identifier + ".name"),
                plugin.guilds.getString(identifier + ".owner"),
                plugin.guilds.getStringList(identifier + ".members"),
                plugin.guilds.getStringList(identifier + ".moderators"),
                plugin.guilds.getString(identifier + ".tag"),
                GuildState.valueOf(plugin.guilds.getString(identifier + ".state")),
                ranks,
                plugin.guilds.getString(identifier + ".motd"),
                plugin.guilds.getLocation(identifier + ".home"),
                plugin.guilds.getLong(identifier + ".creation"));
    }

    public static Guild getGuild(Player player)
    {
        Guild guild = null;
        for (String key : plugin.guilds.getKeys(false))
        {
            Guild kg = getGuild(key);
            if (kg.getMembers().contains(player.getName()))
                guild = kg;
        }
        return guild;
    }

    public static boolean guildExists(String identifier)
    {
        return plugin.guilds.contains(identifier);
    }

    public static boolean isInGuild(Player player)
    {
        for (String key : plugin.guilds.getKeys(false))
        {
            Guild guild = getGuild(key);
            if (guild.getMembers().contains(player.getName()))
                return true;
        }
        return false;
    }
}