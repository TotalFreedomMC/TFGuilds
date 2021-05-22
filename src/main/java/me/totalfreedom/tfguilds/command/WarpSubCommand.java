package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.guild.Guild;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarpSubCommand extends Common implements SubCommand
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
            sender.sendMessage(USAGE + "/g warp <name>");
            return;
        }

        Guild guild = Guild.getGuild(playerSender);
        if (guild == null)
        {
            sender.sendMessage(NOT_IN_GUILD);
            return;
        }

        String name = StringUtils.join(args, " ", 1, args.length);
        if (!guild.hasWarp(name))
        {
            sender.sendMessage(PREFIX + "The guild does not have the warp named " + ChatColor.GOLD + name);
            return;
        }

        Location location = guild.getWarp(name);
        playerSender.teleport(location);
        sender.sendMessage(PREFIX + "Successfully warped to " + ChatColor.GOLD + name);
    }
}
