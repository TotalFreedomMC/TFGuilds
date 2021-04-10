package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.guild.Guild;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class SetWarpSubcommand extends Common implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length < 2)
        {
            sender.sendMessage(tl(PREFIX + "Proper usage: /g setwarp <name>"));
            return true;
        }

        if (sender instanceof ConsoleCommandSender)
        {
            sender.sendMessage(NO_PERMS);
            return true;
        }

        Player player = (Player)sender;
        Guild guild = Guild.getGuild(player);
        if (guild == null)
        {
            sender.sendMessage(NG);
            return true;
        }

        if (!guild.hasModerator(player.getUniqueId()))
        {
            sender.sendMessage(ChatColor.RED + "Only guild moderators and guild owners are allowed to create guild warps.");
            return true;
        }

        String warpName = StringUtils.join(args, " ", 1, args.length);
        if (guild.warpExists(warpName))
        {
            sender.sendMessage(ChatColor.RED + "A warp with that name already exists.");
            return true;
        }

        Guild.createWarp(guild.getIdentifier(), warpName, player);
        sender.sendMessage(tl(PREFIX + "Successfully created new guild warp \"" + warpName + "\"."));
        return true;
    }
}