package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.guild.Guild;
import me.totalfreedom.tfguilds.util.GUtil;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class HomeSubcommand extends Common implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length > 2)
            return false;
        if (sender instanceof ConsoleCommandSender)
        {
            sender.sendMessage(NO_PERMS);
            return true;
        }
        Player player = (Player) sender;
        Guild guild = Guild.getGuild(player);
        if (guild == null)
        {
            sender.sendMessage(ChatColor.RED + "You aren't in a guild!");
            return true;
        }
        if (args.length == 2)
        {
            if (args[1].equalsIgnoreCase("set"))
            {
                if (!guild.hasModerator(player.getName()))
                {
                    sender.sendMessage(ChatColor.RED + "You can't modify your guild's home!");
                    return true;
                }
                guild.setHome(player.getLocation());
                guild.save();
                sender.sendMessage(tl(PREFIX + "Set your current location as the new home of your guild%p%."));
                return true;
            }
            return false;
        }
        if (!guild.hasHome())
        {
            sender.sendMessage(ChatColor.RED + "Your guild doesn't have a home!");
            return true;
        }
        player.teleport(guild.getHome());
        sender.sendMessage(tl("%p%Teleported you to your guild's home!"));
        return true;
    }
}