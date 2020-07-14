package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.guild.Guild;
import me.totalfreedom.tfguilds.util.GUtil;
import org.apache.commons.lang3.StringUtils;
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
        Player player = (Player) sender;
        Guild guild = Guild.getGuild(player);
        if (guild == null)
        {
            sender.sendMessage(ChatColor.RED + "You aren't in a guild!");
            return true;
        }
        if (!guild.getOwner().equals(player.getName()))
        {
            sender.sendMessage(ChatColor.RED + "You can't change the name of your guild!");
            return true;
        }
        String rank = StringUtils.join(args, " ", 1, args.length);
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