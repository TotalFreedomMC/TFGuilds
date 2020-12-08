package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.guild.Guild;
import me.totalfreedom.tfguilds.guild.GuildState;
import me.totalfreedom.tfguilds.util.GUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class SetStateSubcommand extends Common implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length == 1)
        {
            sender.sendMessage(tl(PREFIX + "Proper usage: /g setstate <open | invite | closed>"));
            return true;
        }

        if (args.length == 3)
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

            GuildState state = GuildState.findState(args[2]);
            if (state == null)
            {
                sender.sendMessage(ChatColor.RED + "That is not a valid state!");
                return true;
            }

            guild.setState(state);
            sender.sendMessage(tl(PREFIX + "Set the guild state to %s%" + state.getDisplay().toLowerCase() + "%p% in %s%" + GUtil.colorize(guild.getName()) + "%p%."));
            guild.broadcast(tl("%p%The guild is now %s%" + state.getDisplay().toLowerCase() + "%p%."));
            guild.save();
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
            sender.sendMessage(ChatColor.RED + "You aren't in a guild!");
            return true;
        }

        if (!guild.hasModerator(player.getUniqueId()))
        {
            sender.sendMessage(ChatColor.RED + "You can't change the state of the guild!");
            return true;
        }

        GuildState state = GuildState.findState(args[1]);
        if (state == null)
        {
            sender.sendMessage(ChatColor.RED + "That is not a valid state!");
            return true;
        }

        guild.setState(state);
        sender.sendMessage(tl(PREFIX + "Set the guild state to %s%" + state.getDisplay().toLowerCase() + "%p% in your guild."));
        guild.broadcast(tl("%p%The guild is now %s%" + state.getDisplay().toLowerCase() + "%p%."));
        guild.save();
        return true;
    }
}