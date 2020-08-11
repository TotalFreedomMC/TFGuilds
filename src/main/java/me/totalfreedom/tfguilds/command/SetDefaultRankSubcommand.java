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

public class SetDefaultRankSubcommand extends Common implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof ConsoleCommandSender)
        {
            sender.sendMessage(NO_PERMS);
            return true;
        }

        if (args.length == 0)
        {
            return false;
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
            sender.sendMessage(ChatColor.RED + "You do not have permissions to manage ranks in your guild!");
            return true;
        }

        String rank = StringUtils.join(args, " ", 1, args.length);
        if (rank.equalsIgnoreCase("members") || rank.equalsIgnoreCase("member") || rank.equalsIgnoreCase("none"))
        {
            guild.setDefaultRank(null);
            guild.save();
            sender.sendMessage(tl(PREFIX + "Removed the default guild rank."));
            return true;
        }

        if (!guild.hasRank(rank))
        {
            sender.sendMessage(ChatColor.RED + "A rank of that name does not exist in the guild!");
            return true;
        }

        guild.setDefaultRank(rank);
        guild.save();
        sender.sendMessage(tl(PREFIX + "Set " + rank + " as the default rank for your guild."));
        return true;
    }
}
