package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.guild.Guild;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NoTagSubCommand extends Common implements SubCommand
{

    @Override
    public void execute(CommandSender sender, Player playerSender, String[] args)
    {
        if (!tfmBridge.isAdmin(sender))
        {
            sender.sendMessage(PREFIX + "You do not have the permission.");
            return;
        }

        if (args.length == 1)
        {
            sender.sendMessage(USAGE + "/g edittag <guild>");
            return;
        }

        Guild guild = Guild.getGuild(StringUtils.join(args, " ", 1, args.length));
        if (guild == null)
        {
            sender.sendMessage(PREFIX + "That guild does not exist.");
            return;
        }

        boolean enabled = guild.canUseTag();
        if (enabled)
        {
            guild.setUseTag(false);
            sender.sendMessage(PREFIX + ChatColor.GOLD + "Disabled " + ChatColor.GRAY + "guild tags for " + ChatColor.GOLD + guild.getName());
            guild.broadcast(PREFIX + ChatColor.GOLD + sender.getName() + ChatColor.GRAY + " has disabled guild tags for your guild.");
        }
        else
        {
            guild.setUseTag(true);
            sender.sendMessage(PREFIX + ChatColor.GOLD + "Enabled " + ChatColor.GRAY + "guild tags for " + ChatColor.GOLD + guild.getName());
            guild.broadcast(PREFIX + ChatColor.GOLD + sender.getName() + ChatColor.GRAY + " has enabled guild tags for your guild.");
        }
    }
}
