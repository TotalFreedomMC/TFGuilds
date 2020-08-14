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

public class KickSubcommand extends Common implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length > 3)
        {
            sender.sendMessage(tl(PREFIX + "Proper usage: /g kick <guild <player> | player>"));
            return true;
        }

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

            if (guild.getOwner().equals(player.getName()) || guild.hasModerator(player.getName()))
            {
                sender.sendMessage(ChatColor.RED + "You cannot kick the owner/moderator(s) of a guild!");
                return true;
            }

            guild.removeMember(player.getName());
            sender.sendMessage(tl(PREFIX + "Kicked %s%" + player.getName() + "%p% from %s%" + GUtil.colorize(guild.getName()) + "%p%."));
            player.sendMessage(tl("%s%You have been kicked from your guild."));
            guild.broadcast(tl("%s%" + player.getName() + " %p%has been kicked from your guild."));
            guild.save();
            return true;
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

        if (!guild.hasModerator(player.getName()))
        {
            sender.sendMessage(ChatColor.RED + "You can't kick people from your guild!");
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

        if (guild.getOwner().equals(n.getName()) || guild.hasModerator(n.getName()) && !guild.getOwner().equals(player.getName()))
        {
            sender.sendMessage(ChatColor.RED + "You cannot kick the owner/moderator(s) of a guild!");
            return true;
        }

        guild.removeMember(n.getName());
        sender.sendMessage(tl(PREFIX + "Kicked %s%" + n.getName() + "%p% from your guild."));
        n.sendMessage(tl("%s%You have been kicked from your guild."));
        guild.broadcast(tl("%s%" + n.getName() + " %p%has been kicked from your guild."));
        guild.save();
        return true;
    }
}