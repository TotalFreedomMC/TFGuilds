package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.TFGuilds;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelpSubCommand extends Common implements SubCommand
{


    @Override
    public void execute(CommandSender sender, Player playerSender, String[] args)
    {
        sender.sendMessage(PREFIX + "Command List");
        for (String command : TFGuilds.getPlugin().getSubCommands())
        {
            sender.sendMessage(ChatColor.GRAY + " - " + ChatColor.GOLD + command);
        }
    }
}
