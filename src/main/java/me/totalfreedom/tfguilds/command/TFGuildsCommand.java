package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.TFGuilds;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TFGuildsCommand extends Common implements CommandExecutor
{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length > 1)
        {
            return false;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("reload"))
        {
            if (!tfmBridge.isAdmin(sender))
            {
                sender.sendMessage(PREFIX + "You do not have the permission.");
                return true;
            }

            try
            {
                TFGuilds.getPlugin().getConfig().load();
                TFGuilds.getPlugin().getLogger().info("Successfully reload the configuration file.");
                sender.sendMessage(PREFIX + "The configuration file have been reloaded successfully.");
                return true;
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
            return true;
        }

        sender.sendMessage(ChatColor.AQUA + "TFGuilds " + ChatColor.GRAY + "is a plugin which allows players to create their own guilds, provide guild chat, guild teleportation and many more.");
        sender.sendMessage(ChatColor.GRAY + "Version " + ChatColor.GOLD + TFGuilds.getPlugin().getDescription().getVersion());
        sender.sendMessage(ChatColor.GRAY + "Originally created by " + ChatColor.GOLD + "speednt " + ChatColor.GRAY + "and " + ChatColor.GOLD + "supernt");
        sender.sendMessage(ChatColor.GRAY + "Developed by " + ChatColor.GOLD + "NotInSync");
        sender.sendMessage(ChatColor.GRAY + "Source code " + ChatColor.GOLD + "https://github.com/AtlasMediaGroup/TFGuilds");
        return true;
    }
}
