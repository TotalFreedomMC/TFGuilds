package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.guild.Guild;
import me.totalfreedom.tfguilds.util.GLog;
import me.totalfreedom.tfguilds.util.GUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class DisbandSubcommand extends Common implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length > 2)
        {
            return false;
        }

        if (args.length == 2)
        {
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

            String n = guild.getName();
            GLog.info("Removing guilds.yml data for " + n);
            guild.disband();
            GLog.info(sender.getName() + " deleted guild " + guild.getName());
            sender.sendMessage(tl(PREFIX + "Disbanded \"" + GUtil.colorize(n) + "%p%\"."));
            return true;
        }

        if (sender instanceof ConsoleCommandSender)
        {
            sender.sendMessage(NO_PERMS);
            return true;
        }

        Player player = (Player)sender;
        if (!Guild.isInGuild(player))
        {
            sender.sendMessage(ChatColor.RED + "You aren't in a guild!");
            return true;
        }

        Guild guild = Guild.getGuild(player);
        if (!guild.getOwner().equals(player.getName()))
        {
            sender.sendMessage(ChatColor.RED + "You are not the owner of this guild!");
            return true;
        }

        GLog.info("Removing guilds.yml data for " + guild.getName());
        guild.disband();
        GLog.info(player.getName() + " deleted guild " + guild.getName());
        sender.sendMessage(tl(PREFIX + "You have disbanded your guild!"));
        Bukkit.broadcastMessage(GUtil.colorize(tl("%p%&l" + guild.getName() + "%p% has been disbanded")));
        return true;
    }
}