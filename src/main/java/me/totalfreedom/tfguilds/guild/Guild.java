package me.totalfreedom.tfguilds.guild;

import lombok.Getter;
import lombok.Setter;
import me.totalfreedom.tfguilds.TFGuilds;
import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.util.GUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Guild
{
    private static TFGuilds plugin = TFGuilds.getPlugin();

    @Getter
    private final String identifier;

    @Getter
    private final String name;

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
    private final long creation;

    public Guild(String identifier, String name, String owner, List<String> members, List<String> moderators, String tag, GuildState state, long creation)
    {
        this.identifier = identifier;
        this.name = name;
        this.owner = owner;
        this.members = members;
        this.moderators = moderators;
        this.tag = tag;
        this.state = state;
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

    public boolean hasTag()
    {
        return tag != null;
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

    public String getList()
    {
        return Common.tl(Common.PREFIX + "Guild Roster\n" +
                "%s%Owner%p% - " + owner + "\n" +
                "%s%Moderators%p% - " + StringUtils.join(moderators, ", ") + "\n" +
                "%s%Members%p% - " + StringUtils.join(getOnlyMembers(), ", ") + "\n");
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
                "%s%Creation%p%: " + GUtil.format(creation) + "\n" +
                "%s%Identifier (Technical)%p%: " + identifier + "\n");
    }

    public void chat(String as, String msg)
    {
        broadcast(Common.tl("%s%[%p%Guild Chat %s%| %p%" + GUtil.colorize(name) + "%s%] %p%" + as + ChatColor.WHITE + ": %p%" + msg));
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
        Guild guild = new Guild(identifier, name, owner.getName(), Collections.singletonList(owner.getName()), new ArrayList<>(), null, GuildState.INVITE_ONLY, System.currentTimeMillis());
        guild.save();
        return guild;
    }

    public static Guild getGuild(String identifier)
    {
        if (!plugin.guilds.contains(identifier))
            return null;
        return new Guild(identifier,
                plugin.guilds.getString(identifier + ".name"),
                plugin.guilds.getString(identifier + ".owner"),
                plugin.guilds.getStringList(identifier + ".members"),
                plugin.guilds.getStringList(identifier + ".moderators"),
                plugin.guilds.getString(identifier + ".tag"),
                GuildState.valueOf(plugin.guilds.getString(identifier + ".state")),
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