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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RenameSubcommand extends Common implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length < 2)
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
        String newName = StringUtils.join(args, " ", 1, args.length);
        String identifier = GUtil.flatten(newName);
        if (guild == null)
        {
            sender.sendMessage(ChatColor.RED + "You aren't in a guild!");
            return true;
        }

        if (!guild.getOwner().equals(player.getName()))
        {
            sender.sendMessage(ChatColor.RED + "You can't change the name of your guild!");
            return true;
        }

        Pattern pattern = Pattern.compile("^[A-Za-z0-9? ,_-]+$");
        Matcher matcher = pattern.matcher(newName);

        if (!matcher.matches())
        {
            sender.sendMessage(ChatColor.RED + "Guild names must be alphanumeric.");
            return true;
        }

        if (Guild.guildExists(identifier))
        {
            sender.sendMessage(ChatColor.RED + "A guild with a name similar to yours already exists!");
            return true;
        }

        for (String blacklisted : GUtil.BLACKLISTED_NAMES_AND_TAGS)
        {
            if (args[0].equalsIgnoreCase(blacklisted))
            {
                if (!plugin.bridge.isAdmin(player))
                {
                    player.sendMessage(ChatColor.RED + "You may not use that name.");
                    return true;
                }
            }
        }

        guild.disband();
        guild.setIdentifier(GUtil.flatten(newName));
        guild.setName(newName);
        guild.updateRankIdentifiers();
        guild.setTag(GUtil.colorize("&8[&7" + newName + "&8]"));
        sender.sendMessage(tl(PREFIX + "Set %s%" + GUtil.colorize(newName) + "%p% as the new name of your guild%p%."));
        guild.broadcast(tl("%p%Your guild has been renamed to " + GUtil.colorize(newName) + "%p%."));
        guild.save();
        return true;
    }
}