package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.Common;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GuildChatSpyCommand extends Common implements CommandExecutor
{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(PREFIX + "Console can already view guild chat.");
            return true;
        }

        if (!tfmBridge.isAdmin(sender))
        {
            sender.sendMessage(PREFIX + "You do not have the permission.");
            return true;
        }

        Player player = (Player)sender;
        if (GUILD_CHAT_SPY.contains(player))
        {
            GUILD_CHAT_SPY.remove(player);
            sender.sendMessage(PREFIX + ChatColor.GOLD + "Disabled " + ChatColor.GRAY + "guild chat spy.");
            return true;
        }

        GUILD_CHAT_SPY.add(player);
        sender.sendMessage(PREFIX + ChatColor.GOLD + "Enabled " + ChatColor.GRAY + "guild chat spy.");
        return true;
    }
}
