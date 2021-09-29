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
            guild.chat(player, event.getMessage(), false);
            return;
        }

        if (!ConfigEntry.GUILD_TAGS.getBoolean())
        {
            return;
        }

        User user = User.getUserFromPlayer(player);
        if (guild.getTag() != null && user.displayTag())
        {
            int maxLength = ConfigEntry.GLOBAL_TAG_MAX_LENGTH.getInteger();
            String tfmTag = TFGuilds.getPlugin().getTfmBridge().getTag(player);
            if (tfmTag != null && maxLength > 0)
            {
                int length = GUtil.removeColorCodes(tfmTag).length() + GUtil.removeColorCodes(guild.getTag()).length();
                if (length > maxLength)
                {
                    TFGuilds.getPlugin().getTfmBridge().clearTag(player);
                }
            }
            event.setFormat(GUtil.colorize(guild.getTag().replace("%rank%", guild.getPlayerRank(player))) + ChatColor.RESET + " " + event.getFormat());
        }
    }
}
