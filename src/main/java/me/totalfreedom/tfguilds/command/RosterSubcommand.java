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

public class RosterSubcommand extends Common implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length >= 2)
        {
            Player player = Bukkit.getPlayer(args[1]);
            if (player != null)
            {
                Guild guild = Guild.getGuild(player);
                if (guild != null)
                {
                    sender.sendMessage(guild.getRoster());
                    return true;
                }
            }

            Guild guild = Guild.getGuild(GUtil.flatten(StringUtils.join(args, " ", 1, args.length)));
            if (guild == null)
            {
                sender.sendMessage(ChatColor.RED + "That guild doesn't exist!");
                return true;
            }

            sender.sendMessage(guild.getRoster());
            return true;
        }

        if (sender instanceof ConsoleCommandSender)
        {
            sender.sendMessage(NO_PERMS);
            return true;
        }

        Guild guild = Guild.getGuild((Player)sender);
        if (guild == null)
        {
            sender.sendMessage(ChatColor.RED + "You aren't in a guild!");
            return true;
        }

        sender.sendMessage(guild.getRoster());
        return true;
    }
}