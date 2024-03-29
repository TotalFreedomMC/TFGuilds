package me.totalfreedom.tfguilds.command;

import java.util.HashMap;
import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.TFGuilds;
import me.totalfreedom.tfguilds.guild.Guild;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SetOwnerSubCommand extends Common implements SubCommand
{
    private final HashMap<CommandSender, Player> confirm = new HashMap<>();

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
            sender.sendMessage(USAGE + "/g setowner <player>");
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
            sender.sendMessage(PREFIX + "Only the guild owner can set the owner of the guild.");
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
            sender.sendMessage(PREFIX + "You are already the owner of the guild.");
            return;
        }

        if (!guild.isMember(player))
        {
            sender.sendMessage(PREFIX + "That player is not in your guild.");
            return;
        }

        if (!confirm.containsKey(sender) || !confirm.containsValue(player))
        {
            sender.sendMessage(WARN + "Are you sure you want to transfer ownership of "
                    + ChatColor.GOLD + guild.getName() + ChatColor.GRAY + " to "
                    + ChatColor.GOLD + player.getName() + ChatColor.GRAY + "? Type "
                    + ChatColor.GOLD + "/g setowner " + player.getName()
                    + ChatColor.GRAY + " again within 10 seconds to confirm.");
            confirm.put(sender, player);
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    confirm.remove(sender);
                }
            }.runTaskLater(TFGuilds.getPlugin(), 10 * 20);
        }
        else
        {
            confirm.remove(sender);
            guild.setOwner(player);
            sender.sendMessage(PREFIX + "Successfully set " + ChatColor.GOLD + player.getName() + ChatColor.GRAY + " as the new owner of the guild.");
            guild.broadcast(PREFIX + ChatColor.GOLD + sender.getName() + ChatColor.GRAY + " has set " + ChatColor.GOLD + player.getName() + ChatColor.GRAY + " as the new owner of the guild.");
            player.sendMessage(PREFIX + ChatColor.GOLD + sender.getName() + ChatColor.GRAY + " has set you as the owner of " + ChatColor.GOLD + guild.getName());
        }
    }
}
