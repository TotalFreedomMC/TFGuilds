package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.guild.Guild;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeleteRankSubCommand extends Common implements SubCommand
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
            sender.sendMessage(USAGE + "/g deleterank <name>");
            return;
        }

        Guild guild = Guild.getGuild(playerSender);
        if (guild == null)
        {
            sender.sendMessage(PREFIX + "You are not in a guild!");
            return;
        }

        if (!guild.getOwner().equals(playerSender.getUniqueId()))
        {
            sender.sendMessage(PREFIX + "Only the guild owner can delete ranks for the guild!");
            return;
        }

        String name = StringUtils.join(args, " ", 1, args.length);
        if (!guild.hasRank(name))
        {
            sender.sendMessage(PREFIX + "That rank does not exist!");
            return;
        }

        guild.deleteRank(name);
        sender.sendMessage(PREFIX + "Successfully deleted a rank " + ChatColor.GOLD + name);
    }
}
