package me.totalfreedom.tfguilds.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import me.totalfreedom.tfguilds.TFGuilds;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GUtil
{
    public static TFGuilds plugin = TFGuilds.plugin;
    public static HashMap<String, String> invitedPlayers = new HashMap<>();

    public static boolean isConsole(CommandSender sender)
    {
        return sender instanceof ConsoleCommandSender;
    }

    public static void createGuild(CommandSender owner, String guildName)
    {
        // Set guild name & owner
        plugin.guilds.set("guilds." + guildName, guildName);
        plugin.guilds.set("guilds." + guildName + ".owner", owner.getName());

        // Set guild moderator
        List<String> moderators = plugin.guilds.getStringList("guilds." + guildName + ".moderators");
        moderators.add(owner.getName());
        plugin.guilds.set("guilds." + guildName + ".moderators", moderators);

        // Set guild player
        List<String> players = plugin.guilds.getStringList("guilds." + guildName + ".members");
        players.add(owner.getName());
        plugin.guilds.set("guilds." + guildName + ".members", players);
        plugin.guilds.set("guilds." + guildName + ".tag", GUtil.color("&8[&7" + guildName + "&8]&r "));

        // Set time guild was created
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date date = new Date();
        plugin.guilds.set("guilds." + guildName + ".created", dateFormat.format(date));

        // Add guild to guild list
        List<String> guilds = plugin.guilds.getStringList("list");
        guilds.add(guildName);
        plugin.guilds.set("list", guilds);

        // Save everything & log guild creation
        plugin.guilds.save();
        GLog.info(owner.getName() + " has created a new guild: " + guildName);
    }

    public static void deleteGuild(CommandSender owner, String guildName)
    {
        GLog.info("Removing guilds.yml data for " + guildName);
        plugin.guilds.set("guilds." + guildName, null);
        List<String> guilds = plugin.guilds.getStringList("list");
        guilds.remove(guildName);
        plugin.guilds.set("list", guilds);
        plugin.guilds.save();
    }

    public static void deleteGuild(String guildName)
    {
        GLog.info("Removing guilds.yml data for " + guildName);
        plugin.guilds.set("guilds." + guildName, null);
        List<String> guilds = plugin.guilds.getStringList("list");
        guilds.remove(guildName);
        plugin.guilds.set("list", guilds);
        plugin.guilds.save();
    }

    public static void invitePlayer(Player player, String guild, int seconds)
    {
        if (seconds > 0)
        {
            invitedPlayers.put(player.getName(), guild);
            player.sendMessage(ChatColor.GREEN + "To accept or decline, type /inviteguild accept or /inviteguild deny");
            player.sendMessage(ChatColor.GREEN + "You have " + seconds + " seconds to accept the request before it gets declined automatically.");
        }
        new BukkitRunnable()
        {
            public void run()
            {
                if (!invitedPlayers.containsKey(player.getName()))
                {
                    return;
                }
                invitedPlayers.remove(player.getName());
                player.sendMessage(ChatColor.RED + "Your invitation has expired.");
            }
        }.runTaskLater(plugin, seconds * 20);
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

    public static String getTimeCreated(String guildName)
    {
        return plugin.guilds.getString("guilds." + guildName + ".created");
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

    public static String getGuild(String arg)
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
                    if (guild.equals(arg))
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

    public static List<String> getGuilds()
    {
        return plugin.guilds.getStringList("list");
    }

    public static String getOwner(String guildName)
    {
        return plugin.guilds.getString("guilds." + guildName + ".owner");
    }

    public static List<String> getModerators(String guildName)
    {
        return plugin.guilds.getStringList("guilds." + guildName + ".moderators");
    }

    public static List<String> getMember(String guildName)
    {
        return plugin.guilds.getStringList("guilds." + guildName + ".members");
    }

    public static boolean isGuildMember(Player player, String guildName)
    {
        return getMember(guildName).contains(player.getName());
    }

    public static boolean isGuildModerator(Player player, String guildName)
    {
        return getModerators(guildName).contains(player.getName());
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

    public static String translateHexColorCodes(String message)
    {
        Pattern pattern = Pattern.compile("&#[a-f0-9A-F]{6}");
        Matcher matcher = pattern.matcher(message);

        while (matcher.find())
        {
            String color = matcher.group().replace("&", "");
            message = message.replace("&" + color, net.md_5.bungee.api.ChatColor.of(color) + "");
        }

        message = ChatColor.translateAlternateColorCodes('&', message);
        return message;
    }
}