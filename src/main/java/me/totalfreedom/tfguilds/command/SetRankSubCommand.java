package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.guild.Guild;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetRankSubCommand extends Common implements SubCommand
{

    @Override
    public void execute(CommandSender sender, Player playerSender, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(IN_GAME_ONLY);
            return;
        }

        if (args.length < 3)
        {
            sender.sendMessage(USAGE + "/g setrank <player> <name>");
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
            sender.sendMessage(PREFIX + "You must be a guild moderator to set player's rank for your guild.");
            return;
        }

        Player player = Bukkit.getPlayer(args[1]);
        if (player == null || tfmBridge.isVanished(player))
        {
            sender.sendMessage(PLAYER_NOT_FOUND);
            return;
        }

        if (!guild.isMember(player))
        {
            sender.sendMessage(PLAYER_NOT_IN_GUILD);
            return;
        }

        String name = StringUtils.join(args, " ", 2, args.length);
        if (!guild.hasRank(name))
        {
            sender.sendMessage(PREFIX + "That rank does not exist.");
            return;
        }

        guild.setPlayerRank(player, name);
        sender.sendMessage(PREFIX + "Successfully set " + ChatColor.GOLD + player.getName() + ChatColor.GRAY + "'s guild rank to " + ChatColor.GOLD + name);
        player.sendMessage(PREFIX + ChatColor.GOLD + sender.getName() + ChatColor.GRAY + " has set your guild rank to " + ChatColor.GOLD + name);
    }
}
