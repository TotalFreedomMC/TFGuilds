package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.guild.Guild;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DisbandSubCommand extends Common implements SubCommand
{

    @Override
    public void execute(CommandSender sender, Player playerSender, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(IN_GAME_ONLY);
            return;
        }

        if (args.length >= 2)
        {
            if (!tfmBridge.isAdmin(sender))
            {
                sender.sendMessage(PREFIX + "You do not have permission.");
                return;
            }

            Guild guild = Guild.getGuild(playerSender);
            if (guild == null)
            {
                sender.sendMessage(PREFIX + "That guild does not exist.");
                return;
            }

            guild.disband();
            sender.sendMessage(PREFIX + "The guild " + ChatColor.GOLD + guild.getName() + ChatColor.GRAY + " has been disbanded.");
            Bukkit.broadcastMessage(PREFIX + ChatColor.GOLD + sender.getName() + ChatColor.GRAY + " has disbanded the guild " + ChatColor.GOLD + guild.getName());
            return;
        }

        Guild guild = Guild.getGuild(playerSender);
        if (guild == null)
        {
            sender.sendMessage(NOT_IN_GUILD);
            return;
        }

        if (!guild.getOwner().equals(playerSender.getUniqueId()))
        {
            sender.sendMessage(PREFIX + "You are not the guild owner!");
            return;
        }

        sender.sendMessage(PREFIX + "The guild " + ChatColor.GOLD + guild.getName() + ChatColor.GRAY + " has been disbanded.");
        Bukkit.broadcastMessage(PREFIX + ChatColor.GOLD + sender.getName() + ChatColor.GRAY + " has disbanded the guild " + ChatColor.GOLD + guild.getName());
        guild.disband();
    }
}
