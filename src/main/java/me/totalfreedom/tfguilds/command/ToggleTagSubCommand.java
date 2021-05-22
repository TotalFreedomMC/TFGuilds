package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.guild.User;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggleTagSubCommand extends Common implements SubCommand
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
            sender.sendMessage(USAGE + "/g toggletag");
            return;
        }

        User user = User.getUserFromPlayer(playerSender);
        if (user == null)
        {
            user = User.create(playerSender);
        }

        user.setDisplayTag(!user.displayTag());
        sender.sendMessage(PREFIX + ChatColor.GOLD + (user.displayTag() ? "Enabled" : "Disabled") + ChatColor.GRAY + " personal guild tag.");
    }
}
