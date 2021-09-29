package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.guild.Guild;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveSubCommand extends Common implements SubCommand
{

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

        guild.removeMember(playerSender);
        guild.broadcast(PREFIX + ChatColor.GOLD + sender.getName() + ChatColor.GRAY + " has left the guild.");
        sender.sendMessage(PREFIX + "You have left the guild.");
    }
}
