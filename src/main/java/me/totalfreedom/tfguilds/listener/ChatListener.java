package me.totalfreedom.tfguilds.listener;

import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.TFGuilds;
import me.totalfreedom.tfguilds.config.ConfigEntry;
import me.totalfreedom.tfguilds.guild.Guild;
import me.totalfreedom.tfguilds.guild.User;
import me.totalfreedom.tfguilds.util.GUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener
{

    public ChatListener(TFGuilds plugin)
    {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event)
    {
        Player player = event.getPlayer();
        Guild guild = Guild.getGuild(player);
        if (guild == null)
        {
            return;
        }

        if (Common.GUILD_CHAT.contains(player))
        {
            event.setCancelled(true);
            guild.chat(player, event.getMessage());
            return;
        }

        if (!ConfigEntry.GUILD_TAGS.getBoolean())
        {
            return;
        }

        String display = guild.getPlayerRank(player);
        if (display == guild.getDefaultRank())
        {
            if (guild.getOwner().equals(player.getUniqueId()))
            {
                display = "Guild Owner";
            }
            else if (guild.isModerator(player))
            {
                display = "Guild Moderator";
            }
        }
        else if (display == null)
        {
            display = "Guild Member";
        }

        User user = User.getUserFromPlayer(player);
        if (guild.getTag() != null && user.displayTag())
        {
            event.setFormat(GUtil.colorize(guild.getTag().replace("%rank%", display)) + ChatColor.RESET + " " + event.getFormat());
        }
    }
}
