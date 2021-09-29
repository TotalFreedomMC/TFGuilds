package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.guild.Guild;
import me.totalfreedom.tfguilds.util.GUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateSubCommand extends Common implements SubCommand
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
            sender.sendMessage(USAGE + "/g create <name>");
            return;
        }

        if (Guild.isAlreadyMember(playerSender))
        {
            sender.sendMessage(IN_GUILD);
            return;
        }

        String name = StringUtils.join(args, " ", 1, args.length);
        if (!StringUtils.isAlphanumericSpace(name) || name.isBlank())
        {
            sender.sendMessage(PREFIX + "The guild name must be alphanumeric.");
            return;
        }

        if (name.length() > 30)
        {
            sender.sendMessage(PREFIX + "The guild name cannot go over 30 characters limit.");
            return;
        }

        if (Guild.hasGuild(name))
        {
            sender.sendMessage(PREFIX + "The guild name is already taken.");
            return;
        }

        if (GUtil.containsBlacklistedWord(name))
        {
            sender.sendMessage(PREFIX + "The guild name contains a blacklisted word.");
            return;
        }

        Guild.create(playerSender, name);
        sender.sendMessage(PREFIX + "The guild " + ChatColor.GOLD + name + ChatColor.GRAY + " has been created.");
        Bukkit.broadcastMessage(PREFIX + ChatColor.GOLD + sender.getName() + ChatColor.GRAY + " has created a guild " + ChatColor.GOLD + name);
    }
}
