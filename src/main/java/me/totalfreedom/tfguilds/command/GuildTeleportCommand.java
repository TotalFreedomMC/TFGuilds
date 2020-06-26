package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.util.GMessage;
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
            sender.sendMessage(GMessage.PLAYER_ONLY);
            return true;
        }

        Player player = (Player) sender;
        String guild = GUtil.getGuild(player);

        if (guild == null)
        {
            player.sendMessage(GMessage.NOT_IN_GUILD);
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null)
        {
            player.sendMessage(GMessage.PLAYER_NOT_FOUND);
            return true;
        }

        if (!GUtil.isGuildMember(target, GUtil.getGuild(player)))
        {
            player.sendMessage(ChatColor.RED + "That player isn't in your guild.");
            return true;
        }

        Location targetLoc = target.getLocation();
        player.teleport(targetLoc);

        sender.sendMessage(ChatColor.GREEN + "Teleported to " + target.getName() + " successfully.");
        target.sendMessage(ChatColor.GREEN + player.getName() + " has teleported to you.");
        return true;
    }
}