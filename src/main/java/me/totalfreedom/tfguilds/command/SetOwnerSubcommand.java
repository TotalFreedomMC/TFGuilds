package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.guild.Guild;
import me.totalfreedom.tfguilds.util.GUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class SetOwnerSubcommand extends Common implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length == 1 || args.length > 3)
            return false;
        if (args.length == 3)
        {
            if (!plugin.bridge.isAdmin(sender))
            {
                sender.sendMessage(NO_PERMS);
                return true;
            }
            Guild guild = Guild.getGuild(args[1]);
            if (guild == null)
            {
                sender.sendMessage(ChatColor.RED + "That guild doesn't exist!");
                return true;
            }
            Player player = Bukkit.getPlayer(args[2]);
            if (player == null)
            {
                sender.sendMessage(PNF);
                return true;
            }
            if (!guild.hasMember(player.getName()))
            {
                sender.sendMessage(ChatColor.RED + "This player is not in the specified guild!");
                return true;
            }
            guild.removeModerator(player.getName());
            guild.setOwner(player.getName());
            sender.sendMessage(tl(PREFIX + "Ownership has been transferred to %s%" + player.getName() + "%p% in %s%" + GUtil.colorize(guild.getName()) + "%p%."));
            guild.broadcast(tl("%s%" + player.getName() + " %p%has been made the owner of your guild."));
            guild.save();
            return true;
        }
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
        if (!guild.getOwner().equals(player.getName()))
        {
            sender.sendMessage(ChatColor.RED + "You can't change who is the owner of your guild!");
            return true;
        }
        Player n = Bukkit.getPlayer(args[1]);
        if (n == null)
        {
            sender.sendMessage(PNF);
            return true;
        }
        if (!guild.hasMember(n.getName()))
        {
            sender.sendMessage(ChatColor.RED + "This player is not in your guild!");
            return true;
        }
        guild.removeModerator(n.getName());
        guild.setOwner(n.getName());
        sender.sendMessage(tl(PREFIX + "Ownership has been transferred to %s%" + n.getName() + "%p% in your guild."));
        guild.broadcast(tl("%s%" + n.getName() + " %p%has been made the owner of your guild."));
        guild.save();
        return true;
    }
}