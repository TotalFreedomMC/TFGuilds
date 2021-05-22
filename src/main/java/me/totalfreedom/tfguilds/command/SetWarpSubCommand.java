package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.guild.Guild;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetWarpSubCommand extends Common implements SubCommand
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
            sender.sendMessage(USAGE + "/g setwarp <name>");
            return;
        }

        Guild guild = Guild.getGuild(playerSender);
        if (guild == null)
        {
            sender.sendMessage(NOT_IN_GUILD);
            return;
        }

        if (!guild.isModerator(playerSender))
        {
            sender.sendMessage(PREFIX + "You must be a guild moderator or owner to create a warp for the guild.");
            return;
        }

        String name = StringUtils.join(args, " ", 1, args.length);
        if (!StringUtils.isAlphanumericSpace(name))
        {
            sender.sendMessage(PREFIX + "The name must be alphanumeric.");
            return;
        }

        if (guild.hasWarp(name))
        {
            sender.sendMessage(PREFIX + "A warp with that name already exist.");
            return;
        }

        guild.addWarp(name, playerSender.getLocation());
        sender.sendMessage(PREFIX + "A warp with the name " + ChatColor.GOLD + name + ChatColor.GRAY + " has been successfully created.");
    }
}
