package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.TFGuilds;
import me.totalfreedom.tfguilds.guild.Guild;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class InviteSubCommand extends Common implements SubCommand
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
            sender.sendMessage(USAGE + "/g invite <player>");
            return;
        }

        Guild guild = Guild.getGuild(playerSender);
        if (guild == null)
        {
            sender.sendMessage(NOT_IN_GUILD);
            return;
        }

        if (guild.getState() == Guild.State.CLOSED)
        {
            sender.sendMessage(PREFIX + "The guild is closed.");
            return;
        }

        if (guild.getState() == Guild.State.OPEN)
        {
            sender.sendMessage(PREFIX + "The guild is open to public, tell your friends to join by " + ChatColor.GOLD + "/g join " + guild.getName());
            return;
        }

        Player player = Bukkit.getPlayer(args[1]);
        if (player == null || tfmBridge.isVanished(player))
        {
            sender.sendMessage(PLAYER_NOT_FOUND);
            return;
        }

        if (player.equals(playerSender))
        {
            sender.sendMessage(PREFIX + "You cannot invite yourself.");
            return;
        }

        if (Guild.getGuild(player) != null)
        {
            sender.sendMessage(PREFIX + "That player is already in a guild.");
            return;
        }

        if (guild.isInvited(player))
        {
            sender.sendMessage(PREFIX + "That player is already been invited.");
            return;
        }

        guild.invite(player);
        player.sendMessage(PREFIX + ChatColor.GOLD + sender.getName() + ChatColor.GRAY + " has sent you an invite to join " + ChatColor.GOLD + guild.getName());
        player.sendMessage("Do " + ChatColor.GOLD + "/g join " + guild.getName() + ChatColor.GRAY + " to join!");
        player.sendMessage(PREFIX + "The invite will expire in 90 seconds.");
        sender.sendMessage(PREFIX + "The invite has been sent to " + ChatColor.GOLD + player.getName());

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (!guild.isInvited(player))
                {
                    return;
                }
                guild.removeInvite(player);
                if (player.isOnline())
                {
                    player.sendMessage(PREFIX + "The invite to " + ChatColor.GOLD + guild.getName() + ChatColor.GRAY + " has expired!");
                    sender.sendMessage(PREFIX + ChatColor.GOLD + player.getName() + ChatColor.GRAY + " has not accepted your invite.");
                }
            }
        }.runTaskLater(TFGuilds.getPlugin(), 20 * 90);
    }
}
