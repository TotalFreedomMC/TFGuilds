package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.guild.Guild;
import me.totalfreedom.tfguilds.util.GUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CreateRankSubcommand extends Common implements CommandExecutor
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
            sender.sendMessage(tl(PREFIX + "Proper usage: /g createrank <name>"));
            return true;
        }

        Player player = (Player)sender;
        Guild guild = Guild.getGuild(player);
        if (guild == null)
        {
            sender.sendMessage(ChatColor.RED + "You aren't in a guild!");
            return true;
        }

        if (!guild.getOwner().equals(player.getName()))
        {
            sender.sendMessage(ChatColor.RED + "You can't create ranks for your guild!");
            return true;
        }

        String rank = StringUtils.join(args, " ", 1, args.length);

        for (String blacklisted : GUtil.BLACKLISTED_NAMES_AND_TAGS)
        {
            if (rank.equalsIgnoreCase(blacklisted))
            {
                if (!plugin.bridge.isAdmin(player))
                {
                    player.sendMessage(ChatColor.RED + "You may not use that name.");
                    return true;
                }
            }
        }

        if (guild.hasRank(rank))
        {
            sender.sendMessage(ChatColor.RED + "A rank of that name already exists in the guild!");
            return true;
        }

        guild.addRank(rank);
        sender.sendMessage(tl(PREFIX + "Created a new rank named %s%" + rank + "%p% for your guild."));
        guild.save();
        return true;
    }
}