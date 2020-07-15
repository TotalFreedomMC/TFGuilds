package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.util.GLog;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class TFGuildsCommand extends Common implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length > 1)
            return false;
        if (args.length == 1)
        {
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

        }
        sender.sendMessage(tl("%p%TFGuilds %s%is a plugin which allows for players to make their own guilds, providing guild chat, guild teleportation, and more."));
        sender.sendMessage(tl("%s%Version %p%v" + plugin.getDescription().getVersion()));
        OfflinePlayer sp = Bukkit.getOfflinePlayer(UUID.fromString("d018f2b8-ce60-4672-a45f-e580e0331299"));
        OfflinePlayer su = Bukkit.getOfflinePlayer(UUID.fromString("53b1512e-3481-4702-9f4f-63cb9c8be6a1"));
        sender.sendMessage(tl("%s%Developed by %p%" + sp.getName() + " & " + su.getName()));
        sender.sendMessage(tl("%s%https://github.com/TFPatches/TFGuilds"));
        return true;
    }
}