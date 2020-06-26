package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.util.GBase;
import me.totalfreedom.tfguilds.util.GLog;
import me.totalfreedom.tfguilds.util.GMessage;
import me.totalfreedom.tfguilds.util.GUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TfGuildsCommand extends GBase implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length == 0)
        {
            sender.sendMessage(GUtil.color("&aTFGuilds &2is a plugin which allows for players to make their own guilds, providing guild chat, guild teleportation, and more."));
            sender.sendMessage(String.format(GUtil.color("&2Version &av%s"), plugin.getDescription().getVersion()));
            sender.sendMessage(GUtil.color("&2Developed by &aspeednt"));
            sender.sendMessage(GUtil.color("&2Contributors"));
            sender.sendMessage(GUtil.color("&a- supernt"));
            sender.sendMessage(GUtil.color("&2https://github.com/speedxx/TFGuilds"));
            return true;
        }

        if (args[0].toLowerCase().equals("reload"))
        {
            if (!plugin.tfmb.isAdmin((Player) sender))
            {
                sender.sendMessage(GMessage.NO_PERMISSION);
                return true;
            }

            try
            {
                plugin.config.load();
                plugin.guilds.load();
                GLog.info("All configs reloaded successfully");
                sender.sendMessage(GUtil.color("&aAll configuration files have been reloaded successfully."));
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