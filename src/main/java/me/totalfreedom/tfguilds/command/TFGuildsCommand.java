package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.guild.Guild;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class TFGuildsCommand extends Common implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length != 0)
            return false;
        sender.sendMessage(tl(PREFIX + "Plugin Information"));
        sender.sendMessage(tl("%s%Programmers%p%: speed and super"));
        sender.sendMessage(tl("%s%Version%p%: " + plugin.getDescription().getVersion()));
        sender.sendMessage(tl("%s%Special Thanks%p%: ron (testing)"));
        return true;
    }
}