package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.guild.Guild;
import me.totalfreedom.tfguilds.util.GUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateRankSubCommand extends Common implements SubCommand
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
            sender.sendMessage(USAGE + " /g createrank <name>");
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
            sender.sendMessage(PREFIX + "Only the guild owner can create ranks for the guild!");
            return;
        }

        String name = StringUtils.join(args, " ", 1, args.length);
        if (!StringUtils.isAlphanumericSpace(name) || name.isBlank())
        {
            sender.sendMessage(PREFIX + "The rank name must be alphanumeric!");
            return;
        }

        if (name.length() > 15)
        {
            sender.sendMessage(PREFIX + "The rank name cannot go over 15 characters limit.");
            return;
        }

        if (GUtil.containsBlacklistedWord(name))
        {
            sender.sendMessage(PREFIX + "The guild name contains a blacklisted word.");
            return;
        }

        if (guild.hasRank(name))
        {
            sender.sendMessage(PREFIX + "That rank already exists!");
            return;
        }

        guild.createRank(name);
        sender.sendMessage(PREFIX + "Successfully created a new rank " + ChatColor.GOLD + name);
    }
}
