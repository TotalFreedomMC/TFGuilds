package me.totalfreedom.tfguilds.command;

import java.util.HashMap;
import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.TFGuilds;
import me.totalfreedom.tfguilds.guild.Guild;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class DisbandSubCommand extends Common implements SubCommand
{

    private final HashMap<CommandSender, Boolean> confirm = new HashMap<>();

    @Override
    public void execute(CommandSender sender, Player playerSender, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(IN_GAME_ONLY);
            return;
        }

        if (args.length >= 2)
        {
            if (!tfmBridge.isAdmin(sender))
            {
                sender.sendMessage(PREFIX + "You do not have permission.");
                return;
            }

            Guild guild = Guild.getGuild(StringUtils.join(args, " ", 1, args.length));
            if (guild == null)
            {
                sender.sendMessage(PREFIX + "That guild does not exist.");
                return;
            }

            disband(sender, guild);
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
            sender.sendMessage(PREFIX + "You are not the guild owner!");
            return;
        }

        disband(sender, guild);
    }
    private void disband(CommandSender sender, Guild guild)
    {
        if (!confirm.containsKey(sender))
        {
            sender.sendMessage(WARN + "Are you sure you want to disband the guild "
                    + ChatColor.GOLD + guild.getName() + ChatColor.GRAY + "? Type "
                    + ChatColor.GOLD + "/g disband" + ChatColor.GRAY + " again within 10 seconds to confirm.");
            confirm.put(sender, true);
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
            guild.disband();
            sender.sendMessage(PREFIX + "The guild " + ChatColor.GOLD + guild.getName() + ChatColor.GRAY + " has been disbanded.");
            Bukkit.broadcastMessage(PREFIX + ChatColor.GOLD + sender.getName() + ChatColor.GRAY + " has disbanded the guild " + ChatColor.GOLD + guild.getName());
        }
    }
}
