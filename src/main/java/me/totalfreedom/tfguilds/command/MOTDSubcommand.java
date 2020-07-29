package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.guild.Guild;
import me.totalfreedom.tfguilds.util.GUtil;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class MOTDSubcommand extends Common implements CommandExecutor
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
            sender.sendMessage(ChatColor.RED + "You can't modify your guild's MOTD!");
            return true;
        }

        if (args.length >= 3)
        {
            if (args[1].equalsIgnoreCase("set"))
            {
                String motd = StringUtils.join(args, " ", 2, args.length);
                guild.setMotd(motd);
                guild.save();
                sender.sendMessage(tl(PREFIX + "Set %s%" + GUtil.colorize(motd) + "%p% as the new MOTD of your guild%p%."));
                return true;
            }
            return false;
        }

        if (!args[1].equalsIgnoreCase("clear"))
        {
            return false;
        }

        guild.setMotd(null);
        guild.save();
        sender.sendMessage(tl(PREFIX + "Cleared your guild's MOTD."));
        return true;
    }
}