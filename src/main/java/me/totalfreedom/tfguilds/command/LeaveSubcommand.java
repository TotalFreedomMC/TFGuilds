package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.guild.Guild;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class LeaveSubcommand extends Common implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length > 1)
        {
            return false;
        }

        if (sender instanceof ConsoleCommandSender)
        {
            sender.sendMessage(NO_PERMS);
            return true;
        }

        Player player = (Player)sender;
        Guild guild = Guild.getGuild(player);
        if (guild == null)
        {
            sender.sendMessage(ChatColor.RED + "You aren't in a guild!");
            return true;
        }

        if (guild.getOwner().equals(player.getName()))
        {
            sender.sendMessage(ChatColor.RED + "You cannot leave as you are the owner!");
            return true;
        }

        guild.removeModerator(player.getName());
        guild.removeMember(player.getName());
        guild.save();
        sender.sendMessage(tl("%p%You left your guild."));
        guild.broadcast(tl("%s%" + sender.getName() + " %p%left the guild."));
        return true;
    }
}