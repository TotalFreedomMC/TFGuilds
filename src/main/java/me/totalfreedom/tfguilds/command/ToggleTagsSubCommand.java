package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.config.ConfigEntry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggleTagsSubCommand extends Common implements SubCommand
{

    @Override
    public void execute(CommandSender sender, Player playerSender, String[] args)
    {
        if (!tfmBridge.isAdmin(sender))
        {
            sender.sendMessage(PREFIX + "You do not have the permission.");
            return;
        }

        boolean enabled = ConfigEntry.GUILD_TAGS.getBoolean();
        if (enabled)
        {
            ConfigEntry.GUILD_TAGS.setBoolean(false);
            sender.sendMessage(PREFIX + ChatColor.GOLD + "Disabled " + ChatColor.GRAY + "global guild tags.");
            Bukkit.broadcastMessage(PREFIX + ChatColor.GOLD + sender.getName() + ChatColor.GRAY + " has globally disabled guild tags.");
        }
        else
        {
            ConfigEntry.GUILD_TAGS.setBoolean(true);
            sender.sendMessage(PREFIX + ChatColor.GOLD + "Enabled " + ChatColor.GRAY + " global guild tags.");
            Bukkit.broadcastMessage(PREFIX + ChatColor.GOLD + sender.getName() + ChatColor.GRAY + " has globally enabled guild tags.");
        }
    }
}
