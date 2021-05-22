package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.guild.Guild;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinSubCommand extends Common implements SubCommand
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
            sender.sendMessage(USAGE + "/g join <name>");
            return;
        }

        if (Guild.getGuild(playerSender) != null)
        {
            sender.sendMessage(IN_GUILD);
            return;
        }

        Guild guild = Guild.getGuild(StringUtils.join(args, " ", 1, args.length));
        if (guild == null)
        {
            sender.sendMessage(PREFIX + "That guild does not exist.");
            return;
        }

        if (guild.getState() == Guild.State.CLOSED)
        {
            sender.sendMessage(PREFIX + "That guild is currently closed.");
            return;
        }

        if (guild.getState() == Guild.State.INVITE_ONLY)
        {
            if (guild.isInvited(playerSender))
            {
                sender.sendMessage(PREFIX + "You must be invited to join the guild.");
                return;
            }
            guild.removeInvite(playerSender);
        }

        guild.addMember(playerSender);
        guild.broadcast(PREFIX + ChatColor.GOLD + sender.getName() + ChatColor.GRAY + " has joined the guild.");
    }
}
