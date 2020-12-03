package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.guild.Guild;
import me.totalfreedom.tfguilds.util.GUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateSubcommand extends Common implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof ConsoleCommandSender)
        {
            sender.sendMessage(NO_PERMS);
            return true;
        }

        if (args.length < 2)
        {
            sender.sendMessage(tl(PREFIX + "Proper usage: /g create <name>"));
            return true;
        }

        Player player = (Player) sender;
        String name = StringUtils.join(args, " ", 1, args.length);
        String identifier = GUtil.flatten(name);

        if (Guild.isInGuild(player))
        {
            sender.sendMessage(ChatColor.RED + "You are already in a guild!");
            return true;
        }

        Pattern pattern = Pattern.compile("^[A-Za-z0-9? ,_-]+$");
        Matcher matcher = pattern.matcher(name);

        if (!matcher.matches())
        {
            sender.sendMessage(ChatColor.RED + "Guild names must be alphanumeric.");
            return true;
        }

        if (name.length() > 30)
        {
            sender.sendMessage(ChatColor.RED + "Your guild name may not be over 30 characters.");
            return true;
        }

        if (Guild.guildExists(identifier))
        {
            sender.sendMessage(ChatColor.RED + "A guild with a name similar to yours already exists!");
            return true;
        }

        for (String blacklisted : GUtil.BLACKLISTED_NAMES_AND_TAGS)
        {
            if (name.equalsIgnoreCase(blacklisted))
            {
                if (!plugin.bridge.isAdmin(player))
                {
                    player.sendMessage(ChatColor.RED + "You may not use that name.");
                    return true;
                }
            }
        }

        Guild.createGuild(identifier, name, player);
        sender.sendMessage(tl(PREFIX + "Created a guild named \"" + GUtil.colorize(name) + "%p%\"!"));
        broadcast(GUtil.colorize(tl("%p%" + sender.getName() + " has created guild %p%&l" + name)));
        return true;
    }
}