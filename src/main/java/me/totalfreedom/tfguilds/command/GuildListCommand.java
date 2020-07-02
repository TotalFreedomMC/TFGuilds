package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.util.GBase;
import me.totalfreedom.tfguilds.util.GUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class GuildListCommand extends GBase implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        List<String> guilds = GUtil.getGuilds();

        if (guilds.isEmpty())
        {
            sender.sendMessage(ChatColor.RED + "Nobody has made a guild yet.");
            return true;
        }
        
        sender.sendMessage(GUtil.color("&2-=-=-=- &aGuild List (Total: " + guilds.size() + ") &2-=-=-=-"));
        sender.sendMessage(GUtil.color("&2- &a" + StringUtils.join(guilds, ",\n&2- &a")));
        sender.sendMessage(GUtil.color("&2-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-"));
        return true;
    }
}