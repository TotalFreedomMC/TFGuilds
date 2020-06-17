package totalfreedom.tfguilds.util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
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
        for (String guild : guildMembers.getKeys(false))
        {
            List<String> members = plugin.guilds.getStringList("guilds." + guild + ".members");
            if (members.contains(player.getName()))
            {
                a = true;
                g = guild;
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

    public static String color(String s)
    {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}