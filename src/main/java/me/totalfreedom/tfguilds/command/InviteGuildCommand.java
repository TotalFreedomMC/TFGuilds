package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.util.GBase;
import me.totalfreedom.tfguilds.util.GUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class InviteGuildCommand extends GBase implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length != 1)
        {
            return false;
        }

        if (GUtil.isConsole(sender))
        {
            sender.sendMessage(ChatColor.RED + "You are not allowed to run this command.");
            return true;
        }

        Player player = (Player) sender;
        String guild = GUtil.getGuild(player);
        Player target = Bukkit.getPlayer(args[0]);

        if (target != null)
        {
            if (GUtil.isGuildMember(player, GUtil.getGuild(target)))
            {
                player.sendMessage(ChatColor.RED + "That player is already in a guild.");
                return true;
            }
            GUtil.invitePlayer(target, 60);
            player.sendMessage(GUtil.color("&aSent an invitation to " + target.getName()));
            return true;
        }

        if (GUtil.invitedPlayers.containsKey(player.getName()))
        {
            if (args[0].equalsIgnoreCase("accept"))
            {
                List<String> players = plugin.guilds.getStringList("guilds." + guild + ".members");
                players.add(player.getName());
                GUtil.invitedPlayers.remove(player.getName());
                player.sendMessage(ChatColor.GREEN + "You have successfully joined " + guild);
            }
            if (args[0].equalsIgnoreCase("deny"))
            {
                GUtil.invitedPlayers.remove(player.getName());
                player.sendMessage(ChatColor.GREEN + "You have declined to join " + guild);
            }
            return true;
        }

        sender.sendMessage(ChatColor.GRAY + "Player not found.");
        return true;
    }
}