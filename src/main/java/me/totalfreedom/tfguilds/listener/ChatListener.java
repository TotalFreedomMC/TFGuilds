package me.totalfreedom.tfguilds.listener;

import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.config.ConfigEntry;
import me.totalfreedom.tfguilds.guild.Guild;
import me.totalfreedom.tfguilds.guild.GuildRank;
import me.totalfreedom.tfguilds.util.GUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener
{
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent e)
    {
        Player player = e.getPlayer();
        Guild guild = Guild.getGuild(player);
        if (guild == null)
        {
            return;
        }

        GuildRank rank = null;
        for (GuildRank r : guild.getRanks())
        {
            if (r.getMembers().contains(player.getName()))
            {
                rank = r;
            }
        }

        String display;
        if (rank == null)
        {
            if (guild.getOwner().equals(player.getName()))
            {
                display = "Guild Owner";
            }
            else if (guild.hasModerator(player.getName()))
            {
                display = "Guild Moderator";
            }
            else
            {
                display = "Guild Member";
            }
        }
        else
        {
            display = rank.getName();
        }

        if (Common.IN_GUILD_CHAT.contains(player))
        {
            guild.chat(player.getName(), e.getMessage());
            e.setCancelled(true);
            return;
        }

        if (guild.hasTag())
        {
            e.setFormat(GUtil.colorize(guild.getTag().replace("%rank%", display)) + ChatColor.RESET + " " + e.getFormat());
        }

        if (!ConfigEntry.GUILD_TAGS_ENABLED.getBoolean())
        {
            for (Player p : Bukkit.getOnlinePlayers())
            {
                Guild g = Guild.getGuild(p);

                if (guild.hasTag())
                {
                    e.setFormat(e.getFormat().replace(g.getTag(), ""));
                }
            }
        }
    }
}