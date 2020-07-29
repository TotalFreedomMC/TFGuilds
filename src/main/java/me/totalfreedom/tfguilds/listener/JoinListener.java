package me.totalfreedom.tfguilds.listener;

import me.totalfreedom.tfguilds.guild.Guild;
import me.totalfreedom.tfguilds.util.GUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener
{
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        Player player = e.getPlayer();
        Guild guild = Guild.getGuild(player);
        if (guild == null)
        {
            return;
        }

        if (guild.hasMOTD())
        {
            player.sendMessage(GUtil.colorize(guild.getMotd()));
        }
    }
}