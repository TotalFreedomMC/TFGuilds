package me.totalfreedom.tfguilds.listener;

import me.totalfreedom.tfguilds.TFGuilds;
import me.totalfreedom.tfguilds.guild.Guild;
import me.totalfreedom.tfguilds.guild.User;
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

    @EventHandler(priority = EventPriority.HIGHEST)
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
    }
}
