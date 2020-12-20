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

public class TPSubcommand extends Common implements CommandExecutor
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
        {
            sender.sendMessage(tl(PREFIX + "Proper usage: /g tp <player>"));
            return true;
        }

        Player player = (Player)sender;
        Guild guild = Guild.getGuild(player);
        if (guild == null)
        {
            sender.sendMessage(NG);
            return true;
        }

        Player to = Bukkit.getPlayer(args[1]);
        if (to == null)
        {
            sender.sendMessage(PNF);
            return true;
        }

        if (!guild.getMembers().contains(to.getUniqueId()))
        {
            sender.sendMessage(ChatColor.RED + "That player is not in your guild.");
            return true;
        }

        player.teleport(to.getLocation());
        sender.sendMessage(tl("%p%Teleported to %s%" + to.getName() + "%p%."));
        to.sendMessage(tl("%s%" + sender.getName() + " %p%has teleported to you."));
        return true;
    }
}