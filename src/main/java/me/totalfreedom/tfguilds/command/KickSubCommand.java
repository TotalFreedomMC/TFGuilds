package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.guild.Guild;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KickSubCommand extends Common implements SubCommand
{

    @Override
    public void execute(CommandSender sender, Player playerSender, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(IN_GAME_ONLY);
            return;
        }

        if (args.length < 2)
        {
            sender.sendMessage(USAGE + "/g kick <player>");
            return;
        }

        Guild guild = Guild.getGuild(playerSender);
        if (guild == null)
        {
            sender.sendMessage(NOT_IN_GUILD);
            return;
        }

        if (!guild.isModerator(playerSender))
        {
            sender.sendMessage(PREFIX + "Only the guild owner and moderators can kick members.");
            return;
        }

        Player player = Bukkit.getPlayer(args[1]);
        if (player == null || tfmBridge.isVanished(player))
        {
            sender.sendMessage(PLAYER_NOT_FOUND);
            return;
        }

        if (playerSender.equals(player))
        {
            sender.sendMessage(PREFIX + "You cannot kick yourself.");
            return;
        }

        if (guild.getOwner().equals(player.getUniqueId()) || guild.isModerator(player) && !guild.getOwner().equals(playerSender.getUniqueId()))
        {
            sender.sendMessage(PREFIX + "You may not kick the guild owner or moderators from your guild.");
            return;
        }

        if (!guild.isMember(player))
        {
            sender.sendMessage(PLAYER_NOT_IN_GUILD);
            return;
        }

        guild.removeMember(player);
        sender.sendMessage(PREFIX + "Successfully kicked " + ChatColor.GOLD + player.getName() + ChatColor.GRAY + " from your guild.");
        player.sendMessage(PREFIX + ChatColor.GOLD + sender.getName() + ChatColor.GRAY + " has kicked you from " + ChatColor.GOLD + guild.getName());
    }
}
