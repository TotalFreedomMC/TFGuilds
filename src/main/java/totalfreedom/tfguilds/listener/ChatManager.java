package totalfreedom.tfguilds.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import totalfreedom.tfguilds.TFGuilds;
import totalfreedom.tfguilds.util.GUtil;

public class ChatManager implements Listener
{
    private final TFGuilds plugin;
    public ChatManager()
    {
        this.plugin = TFGuilds.plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e)
    {
        Player player = e.getPlayer();
        String guild = GUtil.getGuild(player);
        if (guild == null)
            return;
        if (!GUtil.hasTag(guild))
            return;
        e.setFormat(GUtil.color(GUtil.getTag(guild)) + " " + ChatColor.RESET + e.getFormat());
    }
}