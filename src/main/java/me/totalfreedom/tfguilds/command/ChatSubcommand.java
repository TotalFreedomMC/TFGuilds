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

public class ChatSubcommand extends Common implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
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

        if (args.length >= 2)
        {
            String message = StringUtils.join(args, " ", 1, args.length);
            guild.chat(player.getName(), message);
            return true;
        }

        if (IN_GUILD_CHAT.contains(player))
        {
            IN_GUILD_CHAT.remove(player);
            sender.sendMessage(tl("%p%Guild chat toggled off."));
            return true;
        }

        IN_GUILD_CHAT.add(player);
        sender.sendMessage(tl("%p%Guild chat toggled on."));
        return true;
    }
}