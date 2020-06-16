package totalfreedom.tfguilds.util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import totalfreedom.tfguilds.TFGuilds;

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

    public static String color(String s)
    {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}