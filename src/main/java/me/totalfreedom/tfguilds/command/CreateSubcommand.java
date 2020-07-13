package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.guild.Guild;
import me.totalfreedom.tfguilds.util.GUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CreateSubcommand extends Common implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof ConsoleCommandSender)
        {
            sender.sendMessage(NO_PERMS);
            return true;
        }
        Player player = (Player) sender;
        String name = StringUtils.join(args, " ", 1, args.length);
        String identifier = GUtil.flatten(name);
        if (Guild.isInGuild(player))
        {
            sender.sendMessage(ChatColor.RED + "You are already in a guild!");
            return true;
        }
        if (Guild.guildExists(identifier))
        {
            sender.sendMessage(ChatColor.RED + "A guild with a name similar to yours already exists!");
            return true;
        }
        Guild.createGuild(identifier, name, player);
        sender.sendMessage(tl(PREFIX + "Created a guild named \"" + GUtil.colorize(name) + "%p%\"!"));
        return true;
    }
}