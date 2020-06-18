package me.totalfreedom.tfguilds.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import me.totalfreedom.tfguilds.TFGuilds;

import java.util.Arrays;
import java.util.List;

public class GUtil
{
    public static TFGuilds plugin = TFGuilds.plugin;

    public static boolean isConsole(CommandSender sender)
    {
        return sender instanceof ConsoleCommandSender;
    }

    public static void createGuild(CommandSender owner, String guildName)
    {
        plugin.guilds.set("guilds." + guildName, guildName);
        plugin.guilds.set("guilds." + guildName + ".owner", owner.getName());
        plugin.guilds.set("guilds." + guildName + ".members", owner.getName());

        List<String> players = plugin.guilds.getStringList("guilds." + guildName + ".members");
        players.add(owner.getName());
        plugin.guilds.set("guilds." + guildName + ".members", players);

        plugin.guilds.save();
        GLog.info(owner.getName() + " has created a new guild: " + guildName);
    }

    public static void setTag(String tag, String guildName)
    {
        plugin.guilds.set("guilds." + guildName + ".tag", tag);
        plugin.guilds.save();
    }

    public static String getTag(String guildName)
    {
        return plugin.guilds.getString("guilds." + guildName + ".tag");
    }

    public static boolean hasTag(String guildName)
    {
        return plugin.guilds.contains("guilds." + guildName + ".tag");
    }

    public static String getGuild(Player player)
    {
        String g = "";
        boolean a = false;
        ConfigurationSection guildMembers = plugin.guilds.getConfigurationSection("guilds");
        if (guildMembers != null)
        {
            try
            {
                for (String guild : guildMembers.getKeys(false))
                {
                    List<String> members = plugin.guilds.getStringList("guilds." + guild + ".members");
                    if (members.contains(player.getName()))
                    {
                        a = true;
                        g = guild;
                    }
                }
            }
            catch (Exception e)
            {
                e.fillInStackTrace();
            }
        }
        if (!a)
            return null;
        return g;
    }

    public static String getOwner(String guildName)
    {
        return plugin.guilds.getString("guilds." + guildName + ".owner");
    }

    public static List<String> getMember(String guildName)
    {
        return plugin.guilds.getStringList("guilds." + guildName + ".members");
    }

    public static boolean isGuildMember(Player player, String guildName)
    {
        return getMember(guildName).contains(player.getName().toLowerCase());
    }

    public static void guildChat(CommandSender sender, String message, String guildName)
    {
        String sent = ChatColor.DARK_GRAY + "[" + ChatColor.BLUE + "GC" + ChatColor.DARK_GRAY + "] " + getTag(guildName) + ChatColor.BLUE + sender
                .getName() + ChatColor.GRAY + ": " + ChatColor.AQUA + message;
        GLog.info(sent);
        for (Player player : Bukkit.getOnlinePlayers())
        {
            if (isGuildMember(player, guildName))
            {
                player.sendMessage(sent);
            }
        }
    }

    public static List<String> BLACKLISTED_NAMES_AND_TAGS = Arrays.asList(
            "admin", "owner", "moderator", "developer", "console", "dev", "staff", "mod", "sra", "tca", "sta", "sa");

    public static String color(String s)
    {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}