package me.totalfreedom.tfguilds.listener;

import me.totalfreedom.tfguilds.TFGuilds;
import me.totalfreedom.tfguilds.util.GUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatManager implements Listener
{
    private final TFGuilds plugin;
    public ChatManager()
    {
        this.plugin = TFGuilds.plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent e)
    {
        Player player = e.getPlayer();
        String guild = GUtil.getGuild(player);
        if (guild == null)
        {
            return;
        }
        e.setFormat(GUtil.color(GUtil.getTag(guild)) + ChatColor.RESET + e.getFormat());
    }
}