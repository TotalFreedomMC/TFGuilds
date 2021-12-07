package me.totalfreedom.tfguilds.command;

import java.util.ArrayList;
import java.util.List;
import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.TFGuilds;
import me.totalfreedom.tfguilds.guild.Guild;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class LeaveSubCommand extends Common implements SubCommand
{

    private final List<CommandSender> confirm = new ArrayList<>();

    @Override
    public void execute(CommandSender sender, Player playerSender, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(IN_GAME_ONLY);
            return;
        }

        Guild guild = Guild.getGuild(playerSender);
        if (guild == null)
        {
            sender.sendMessage(NOT_IN_GUILD);
            return;
        }

        if (guild.getOwner().equals(playerSender.getUniqueId()))
        {
            sender.sendMessage(PREFIX + "You may not leave your guild as the owner, please disband the guild instead.");
            return;
        }

        if (!confirm.contains(sender))
        {
            sender.sendMessage(WARN + "Are you sure you want to leave "
                    + ChatColor.GOLD + guild.getName() + ChatColor.GRAY + "? Type "
                    + ChatColor.GOLD + "/g leave"
                    + ChatColor.GRAY + " again within 10 seconds to confirm.");
            confirm.add(sender);
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
            guild.removeMember(playerSender);
            guild.broadcast(PREFIX + ChatColor.GOLD + sender.getName() + ChatColor.GRAY + " has left the guild.");
            sender.sendMessage(PREFIX + "You have left the guild.");
        }
    }
}
