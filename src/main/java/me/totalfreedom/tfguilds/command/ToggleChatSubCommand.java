package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.guild.User;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggleChatSubCommand extends Common implements SubCommand
{

    @Override
    public void execute(CommandSender sender, Player playerSender, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(NOT_IN_GUILD);
            return;
        }

        if (args.length > 1)
        {
            sender.sendMessage(USAGE + "/g togglechat");
            return;
        }

        User user = User.getUserFromPlayer(playerSender);
        if (user == null)
        {
            user = User.create(playerSender);
        }

        // Must use this otherwise the subcommand will not work properly, unlike ToggleTagSubCommand.java
        boolean enabled = user.displayChat();
        if (enabled)
        {
            user.setDisplayChat(false);
            sender.sendMessage(PREFIX + ChatColor.GOLD + "Disabled" + ChatColor.GRAY + " personal guild chat.");
        }
        else
        {
            user.setDisplayChat(true);
            sender.sendMessage(PREFIX + ChatColor.GOLD + "Enabled" + ChatColor.GRAY + " personal guild chat.");
        }
    }
}
