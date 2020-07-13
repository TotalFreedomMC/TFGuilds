package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.guild.Guild;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class SaveConfigSubcommand extends Common implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length != 1)
            return false;
        if (!plugin.bridge.isAdmin(sender))
        {
            sender.sendMessage(NO_PERMS);
            return true;
        }
        plugin.config.load();
        plugin.guilds.load();
        plugin.config.save();
        plugin.guilds.save();
        sender.sendMessage(tl(PREFIX + "Saved all configuration files in their current state."));
        return true;
    }
}