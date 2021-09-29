package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.guild.Guild;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RemoveModSubCommand extends Common implements SubCommand
{

    @Override
    public void execute(CommandSender sender, Player playerSender, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(IN_GAME_ONLY);
            return;
        }

        if (args.length != 2)
        {
            sender.sendMessage(USAGE + "/g removemod <player>");
            return;
        }

        Guild guild = Guild.getGuild(playerSender);
        if (guild == null)
        {
            sender.sendMessage(NOT_IN_GUILD);
            return;
        }

        Player player = Bukkit.getPlayer(args[1]);
        if (player == null || tfmBridge.isVanished(player))
        {
            sender.sendMessage(PLAYER_NOT_FOUND);
            return;
        }

        if (!guild.getOwner().equals(playerSender.getUniqueId()))
        {
            sender.sendMessage(PREFIX + "You must be the guild owner to remove moderators.");
            return;
        }

        if (guild.getOwner().equals(player.getUniqueId()))
        {
            sender.sendMessage(PREFIX + "You are the owner of your guild, you cannot remove yourself.");
            return;
        }

        if (!guild.isMember(player))
        {
            sender.sendMessage(NOT_IN_GUILD);
            return;
        }

        if (!guild.isModerator(player))
        {
            sender.sendMessage(PREFIX + "That player is not a moderator.");
            return;
        }

        guild.removeModerator(player);
        sender.sendMessage(PREFIX + "Successfully removed " + ChatColor.GOLD + player.getName() + ChatColor.GRAY + " as a moderator.");
        player.sendMessage(PREFIX + "You are no longer a guild moderator for your guild.");
    }
}
