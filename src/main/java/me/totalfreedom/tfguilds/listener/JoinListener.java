package me.totalfreedom.tfguilds.listener;

import me.totalfreedom.tfguilds.TFGuilds;
import me.totalfreedom.tfguilds.config.ConfigEntry;
import me.totalfreedom.tfguilds.guild.Guild;
import me.totalfreedom.tfguilds.guild.User;
import me.totalfreedom.tfguilds.util.GUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener
{

    public JoinListener(TFGuilds plugin)
    {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();

        User user = User.getUserFromPlayer(player);
        if (user == null)
        {
            User.create(player);
        }

        Guild guild = Guild.getGuild(player);
        if (guild == null)
        {
            return;
        }

        if (guild.getMotd() != null && !guild.getMotd().isEmpty())
        {
            player.sendMessage(guild.getMotd());
        }

        int maxLength = ConfigEntry.GLOBAL_TAG_MAX_LENGTH.getInteger();
        String tfmTag = TFGuilds.getPlugin().getTfmBridge().getTag(player);
        if (user != null && user.displayTag() && tfmTag != null && guild.getTag() != null && maxLength > 0)
        {
            String tfmTagStripped = GUtil.removeColorCodes(tfmTag);
            String guildTagStripped = GUtil.removeColorCodes(guild.getTag());
            if (tfmTagStripped != null && guildTagStripped != null)
            {
                int length = GUtil.removeColorCodes(tfmTag).length() + GUtil.removeColorCodes(guild.getTag()).length();
                if (length > maxLength)
                {
                    TFGuilds.getPlugin().getTfmBridge().clearTag(player);
                }
            }

        }
    }
}
