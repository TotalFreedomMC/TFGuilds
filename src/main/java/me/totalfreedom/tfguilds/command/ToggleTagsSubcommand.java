/*package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.config.ConfigEntry;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ToggleTagsSubcommand extends Common implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (!plugin.bridge.isAdmin(sender))
        {
            sender.sendMessage(NO_PERMS);
            return true;
        }

        boolean enabled = ConfigEntry.GUILD_TAGS_ENABLED.getBoolean();
        if (enabled)
        {
            ConfigEntry.GUILD_TAGS_ENABLED.setBoolean(false);
            sender.sendMessage(tl(PREFIX + "Globally disabled guild tags."));
            broadcast(ChatColor.RED + sender.getName() + " - Globally disabling guild tags");
        }
        else
        {
            ConfigEntry.GUILD_TAGS_ENABLED.setBoolean(true);
            sender.sendMessage(tl(PREFIX + "Globally enabled guild tags."));
            broadcast(ChatColor.RED + sender.getName() + " - Globally enabling guild tags");
        }
        return true;
    }
}*/