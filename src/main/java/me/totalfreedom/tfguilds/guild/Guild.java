package me.totalfreedom.tfguilds.guild;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import me.totalfreedom.tfguilds.TFGuilds;
import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.config.ConfigEntry;
import me.totalfreedom.tfguilds.util.GLog;
import me.totalfreedom.tfguilds.util.GUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
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
    private String owner;

    @Getter
    private final List<String> moderators;

    @Getter
    private final List<String> members;

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
                 String owner,
                 List<String> members,
                 List<String> moderators,
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
        plugin.guilds.set(identifier + ".name", name);
        plugin.guilds.set(identifier + ".owner", owner);
        plugin.guilds.set(identifier + ".members", members);
        plugin.guilds.set(identifier + ".moderators", moderators);
        plugin.guilds.set(identifier + ".tag", tag);
        plugin.guilds.set(identifier + ".state", state.name());
        for (GuildRank rank : ranks)
        {
            rank.set();
        }
        plugin.guilds.set(identifier + ".motd", motd);
        plugin.guilds.set(identifier + ".home", home);
        plugin.guilds.set(identifier + ".creation", creation);
        plugin.guilds.set(identifier + ".defaultrank", defaultRank);
        plugin.guilds.save();
    }

    public void addMember(String name)
    {
        if (hasDefaultRank())
        {
            getRank(defaultRank).getMembers().add(name);
        }

        members.add(name);
    }

    public void removeMember(String name)
    {
        Player player = Bukkit.getPlayer(name);
        if (player != null)
        {
            Common.IN_GUILD_CHAT.remove(player);
        }

        members.remove(name);
        for (GuildRank gr : getRanks())
        {
            getRank(gr.getName()).getMembers().remove(name);
        }
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
        {
            return true;
        }

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
            {
                continue;
            }
            only.add(member);
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
                "%s%Owner%p% - " + owner + "\n" +
                "%s%Moderators%p% - " + StringUtils.join(moderators, ", ") + "\n");

        for (GuildRank rank : ranks)
        {
            list.append("%s%")
                    .append(rank.getName()).append("%p% - ")
                    .append(StringUtils.join(rank.getMembers(), ", "))
                    .append("\n");
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
                "%s%Name%p%: " + name + "\n" +
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

        if (ConfigEntry.SERVER_GUILD_CHAT_LOGGING_ENABLED.getBoolean())
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
        for (String member : members)
        {
            Player player = Bukkit.getPlayer(member);
            if (player == null)
            {
                continue;
            }
            Common.IN_GUILD_CHAT.remove(player);
        }
        plugin.guilds.set(identifier, null);
        plugin.guilds.save();
    }

    public void updateRankIdentifiers()
    {
        for (GuildRank rank : ranks)
        {
            rank.delete();
            rank.setIguild(identifier);
            rank.set();
            plugin.guilds.save();
        }
    }

    public static Guild createGuild(String identifier, String name, Player owner)
    {
        if (plugin.guilds.contains(identifier))
        {
            return getGuild(identifier);
        }

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
        guild.save();
        GLog.info(owner.getName() + " has created guild " + name);
        return guild;
    }

    public static Guild getGuild(String identifier)
    {
        if (!plugin.guilds.contains(identifier))
        {
            return null;
        }

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
                GuildState.findState(plugin.guilds.getString(identifier + ".state")),
                ranks,
                plugin.guilds.getString(identifier + ".motd"),
                plugin.guilds.getLocation(identifier + ".home"),
                plugin.guilds.getLong(identifier + ".creation"),
                plugin.guilds.getString(identifier + ".defaultrank"));
    }

    public static Guild getGuild(Player player)
    {
        Guild guild = null;
        for (String key : plugin.guilds.getKeys(false))
        {
            Guild kg = getGuild(key);
            if (kg.getMembers().contains(player.getName()))
            {
                guild = kg;
            }
        }
        return guild;
    }

    public static Guild getGuild(CommandSender sender)
    {
        Guild guild = null;
        for (String key : plugin.guilds.getKeys(false))
        {
            Guild kg = getGuild(key);
            if (kg.getMembers().contains(sender.getName()))
            {
                guild = kg;
            }
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
            {
                return true;
            }
        }
        return false;
    }
}