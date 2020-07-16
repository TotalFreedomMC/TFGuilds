package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.guild.Guild;
import me.totalfreedom.tfguilds.guild.GuildState;
import me.totalfreedom.tfguilds.util.GUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class InviteSubcommand extends Common implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof ConsoleCommandSender)
        {
            sender.sendMessage(NO_PERMS);
            return true;
        }
        
        if (args.length != 2)
            return false;
            
        Player player = (Player) sender;
        Guild guild = Guild.getGuild(player);
        Player invitee = Bukkit.getPlayer(args[1]);
        if (invitee == null)
        {
            sender.sendMessage(PNF);
            return true;
        }
        
        if (Guild.getGuild(invitee) != null)
        {
            sender.sendMessage(ChatColor.RED + "This player is already in another guild!");
            return true;
        }
        
        if (guild == null)
        {
            sender.sendMessage(ChatColor.RED + "You aren't in a guild!");
            return true;
        }
        
        if (guild.getState() == GuildState.CLOSED)
        {
            sender.sendMessage(ChatColor.RED + "The guild is currently closed!");
            return true;
        }
        
        if (guild.getState() == GuildState.OPEN)
        {
            sender.sendMessage(ChatColor.RED + "The guild is open! Tell your friend to join using \"/g join\"!");
            return true;
        }
        
        if (INVITES.containsKey(invitee))
        {
            sender.sendMessage(ChatColor.RED + "They have already been invited to your guild!");
            return true;
        }
        
        INVITES.put(invitee, guild);
        invitee.sendMessage(tl("%p%You have been invited to join %s%" + GUtil.colorize(guild.getName()) + "%p% through %s%" + player.getName() + "%p%'s invite!"));
        invitee.sendMessage(tl("%p%Do %s%/g join " + ChatColor.stripColor(GUtil.colorize(guild.getName())) + "%p% to join!"));
        invitee.sendMessage(tl("%p%This invite will expire in 90 seconds."));
        sender.sendMessage(tl("%p%Invite sent!"));
        new BukkitRunnable()
        {
            public void run()
            {
                if (!INVITES.containsKey(invitee))
                    return;
                INVITES.remove(player);
                invitee.sendMessage(ChatColor.RED + "Invite expired.");
                sender.sendMessage(ChatColor.RED + "Invite expired.");
            }
        }.runTaskLater(plugin, 20 * 90);
        return true;
    }
}
