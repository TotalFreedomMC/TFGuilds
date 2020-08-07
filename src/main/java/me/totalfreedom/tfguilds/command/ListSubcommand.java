package me.totalfreedom.tfguilds.command;

import java.util.List;
import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.guild.Guild;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ListSubcommand extends Common implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        List<String> guilds = Guild.getGuildList();

        if (guilds.isEmpty())
        {
            sender.sendMessage(ChatColor.RED + "Nobody has made a guild yet.");
            return true;
        }

        sender.sendMessage(tl(PREFIX + "%s%Guild List (%p%" + guilds.size() + " total%s%)"));
        sender.sendMessage(tl("%s%- %p%" + StringUtils.join(guilds, "\n%s%- %p%")));
        return true;
    }
}