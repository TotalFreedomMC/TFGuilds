package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.guild.Guild;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetMemberSubcommand extends Common implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length == 1 || args.length == 2 || args.length > 3)
        {
            sender.sendMessage(tl(PREFIX + "Proper usage: /g setmember <guild> <player>"));
            return true;
        }

        if (!plugin.bridge.isAdmin(sender))
        {
            sender.sendMessage(NO_PERMS);
            return true;
        }

        Guild guild = Guild.getGuild(args[1]);
        if (guild == null)
        {
            sender.sendMessage(ChatColor.RED + "That guild doesn't exist!");
            return true;
        }

        Player player = Bukkit.getPlayer(args[2]);
        if (player == null)
        {
            sender.sendMessage(PNF);
            return true;
        }

        if (Guild.getGuild(player) != null)
        {
            sender.sendMessage(ChatColor.RED + "This player is already in another guild!");
            return true;
        }

        guild.addMember(player.getUniqueId());
        guild.broadcast(tl("%s%" + player.getName() + " %p%has joined the guild."));
        guild.save();
        return true;
    }
}