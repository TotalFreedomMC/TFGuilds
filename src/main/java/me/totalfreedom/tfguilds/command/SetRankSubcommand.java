package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.guild.Guild;
import me.totalfreedom.tfguilds.guild.GuildRank;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class SetRankSubcommand extends Common implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length == 1)
        {
            return false;
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
            sender.sendMessage(ChatColor.RED + "You aren't in a guild!");
            return true;
        }

        if (!guild.hasModerator(player.getName()))
        {
            sender.sendMessage(ChatColor.RED + "You can't change the ranks of your guild members!");
            return true;
        }

        Player r = Bukkit.getPlayer(args[1]);
        if (r == null)
        {
            sender.sendMessage(PNF);
            return true;
        }

        if (!guild.hasMember(r.getName()))
        {
            sender.sendMessage(ChatColor.RED + "This player is not in your guild!");
            return true;
        }

        String name = StringUtils.join(args, " ", 2, args.length);
        if (name.toLowerCase().equals("members") ||
                name.toLowerCase().equals("member") ||
                name.toLowerCase().equals("none"))
        {
            for (GuildRank gr : guild.getRanks())
            {
                gr.getMembers().remove(r.getName());
            }

            sender.sendMessage(tl(PREFIX + "Removed the rank of %s%" + r.getName() + "%p% in your guild."));
            r.sendMessage(tl("%p%Your rank in your guild has been removed."));
            guild.save();
            return true;
        }

        GuildRank rank = guild.getRank(name);
        if (rank == null)
        {
            sender.sendMessage(ChatColor.RED + "A rank of that name does not exist in the guild!");
            return true;
        }

        for (GuildRank gr : guild.getRanks())
        {
            gr.getMembers().remove(r.getName());
        }

        rank.getMembers().add(r.getName());
        sender.sendMessage(tl(PREFIX + "Set the rank of %s%" + r.getName() + "%p% in your guild to %s%" + rank.getName() + "%p%."));
        r.sendMessage(tl("%p%Your rank in your guild has been set to %s%" + rank.getName() + "%p%."));
        guild.save();
        return true;
    }
}