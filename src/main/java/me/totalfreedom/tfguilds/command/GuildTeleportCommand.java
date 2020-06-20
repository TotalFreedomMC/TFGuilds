package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.util.GUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GuildTeleportCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length == 0)
        {
            return false;
        }

        if (GUtil.isConsole(sender))
        {
            sender.sendMessage(ChatColor.RED + "You are not allowed to run this command.");
            return true;
        }

        String guild = GUtil.getGuild((Player) sender);
        if (guild == null)
        {
            sender.sendMessage(ChatColor.RED + "You aren't in a guild!");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null)
        {
            sender.sendMessage(ChatColor.RED + "Player not found.");
            return true;
        }

        if (!GUtil.isGuildMember(target, GUtil.getGuild((Player) sender)))
        {
            sender.sendMessage(ChatColor.RED + "That player isn't in your guild.");
            return true;
        }

        Location targetLoc = target.getLocation();
        ((Player) sender).teleport(targetLoc);

        sender.sendMessage(ChatColor.GREEN + "Teleported to " + target.getName() + " successfully.");
        target.sendMessage(ChatColor.GREEN + sender.getName() + " has teleported to you.");
        return true;
    }
}