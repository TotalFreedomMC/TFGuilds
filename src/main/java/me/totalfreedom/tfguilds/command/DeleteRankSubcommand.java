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

public class DeleteRankSubcommand extends Common implements CommandExecutor
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
            sender.sendMessage(tl(PREFIX + "Proper usage: /g deleterank <rank>"));
            return true;
        }

        Player player = (Player)sender;
        Guild guild = Guild.getGuild(player);
        if (guild == null)
        {
            sender.sendMessage(NG);
            return true;
        }

        if (!guild.getOwner().equals(player.getUniqueId()))
        {
            sender.sendMessage(ChatColor.RED + "You can't delete ranks from your guild!");
            return true;
        }

        String rank = StringUtils.join(args, " ", 1, args.length);
        if (!guild.hasRank(rank))
        {
            sender.sendMessage(ChatColor.RED + "A rank of that name does not exist in the guild!");
            return true;
        }

        guild.removeRank(rank);

        if (guild.hasDefaultRank() && rank.equals(guild.getDefaultRank()))
        {
            guild.setDefaultRank(null);
            guild.save();
        }

        sender.sendMessage(tl(PREFIX + "Deleted the rank named %s%" + rank + "%p% in your guild."));
        guild.save();
        return true;
    }
}