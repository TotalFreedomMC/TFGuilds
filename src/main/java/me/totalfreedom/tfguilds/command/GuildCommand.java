package me.totalfreedom.tfguilds.command;

import java.util.Collections;
import java.util.List;
import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.TFGuilds;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class GuildCommand extends Common implements CommandExecutor, TabCompleter
{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        Player playerSender = null;
        if (sender instanceof Player)
        {
            playerSender = (Player)sender;
        }

        if (args.length >= 1)
        {
            String name = args[0].toLowerCase();
            SubCommand command = TFGuilds.getPlugin().getSubCommand(name);
            if (command != null)
            {
                command.execute(sender, playerSender, args);
            }
            else
            {
                sender.sendMessage(PREFIX + "Unknown subcommand, do " + ChatColor.GOLD + "/g help" + ChatColor.GRAY + " for help.");
            }
            return true;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args)
    {
        return Collections.emptyList();
    }
}
