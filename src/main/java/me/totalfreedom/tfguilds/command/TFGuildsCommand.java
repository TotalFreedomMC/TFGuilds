package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.util.GLog;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TFGuildsCommand extends Common implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length == 0)
        {
            sender.sendMessage(tl("%p%TFGuilds %s%is a plugin which allows for players to make their own guilds, providing guild chat, guild teleportation, and more."));
            sender.sendMessage(tl("%s%Version %p%v" + plugin.getDescription().getVersion()));
            sender.sendMessage(tl("%s%Developed by %p%speednt & supernt"));
            sender.sendMessage(tl("%s%https://github.com/TFPatches/TFGuilds"));
            return true;
        }

        if (args[0].toLowerCase().equals("reload"))
        {
            if (!plugin.bridge.isAdmin(sender))
            {
                sender.sendMessage(NO_PERMS);
                return true;
            }
            try
            {
                plugin.config.load();
                plugin.guilds.load();
                GLog.info("All configs reloaded successfully");
                sender.sendMessage(tl(PREFIX + "All configuration files have been reloaded successfully."));
                return true;
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
            return true;
        }
        return false;
    }
}