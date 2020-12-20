package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.guild.Guild;
import me.totalfreedom.tfguilds.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class ToggleTagSubcommand extends Common implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length > 2)
        {
            sender.sendMessage(tl(PREFIX + "Proper usage: /g toggletag [player]"));
            return true;
        }

        if (args.length == 2)
        {
            if (!plugin.bridge.isAdmin(sender))
            {
                sender.sendMessage(NO_PERMS);
                return true;
            }

            Player player = Bukkit.getPlayer(args[1]);
            if (player == null)
            {
                sender.sendMessage(PNF);
                return true;
            }
            User user = plugin.userData.get(player.getUniqueId());
            user.setTag(!user.isTag());
            user.save();
            sender.sendMessage(tl(PREFIX + (user.isTag() ? "Enabled" : "Disabled") + " personal guild tag for " + player.getName() + "."));
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
            sender.sendMessage(NG);
            return true;
        }

        User user = plugin.userData.get(player.getUniqueId());
        user.setTag(!user.isTag());
        user.save();
        sender.sendMessage(tl(PREFIX + (user.isTag() ? "Enabled" : "Disabled") + " personal guild tag."));
        return true;
    }
}