package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.Common;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class GuildChatSpyCommand extends Common implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof ConsoleCommandSender)
        {
            sender.sendMessage(NO_PERMS);
            return true;
        }

        if (!plugin.bridge.isAdmin(sender))
        {
            sender.sendMessage(NO_PERMS);
            return true;
        }

        Player player = (Player)sender;
        if (CHAT_SPY.contains(player))
        {
            CHAT_SPY.remove(player);
            sender.sendMessage(tl(PREFIX + "%p%Global guild chat spy disabled."));
            return true;
        }

        CHAT_SPY.add(player);
        sender.sendMessage(tl(PREFIX + "%p%Global guild chat spy enabled."));
        return true;
    }
}