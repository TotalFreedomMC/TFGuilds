package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.guild.Guild;
import me.totalfreedom.tfguilds.util.GUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetDefaultRankSubCommand extends Common implements SubCommand
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
            sender.sendMessage(USAGE + "/g setdefaultrank <name>");
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
            sender.sendMessage(PREFIX + "Only the guild owner can set default rank.");
            return;
        }

        String name = StringUtils.join(args, " ", 1, args.length);
        if (name.equalsIgnoreCase("none"))
        {
            guild.setDefaultRank(null);
            sender.sendMessage(PREFIX + "Removed default rank.");
            return;
        }

        if (!StringUtils.isAlphanumericSpace(name) || name.isBlank())
        {
            sender.sendMessage(PREFIX + "The name must be alphanumeric.");
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

        guild.setDefaultRank(name);
        sender.sendMessage(PREFIX + "Successfully set " + ChatColor.GOLD + name + ChatColor.GRAY + " as the default rank of your guild.");
    }
}
