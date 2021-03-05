package me.totalfreedom.tfguilds.listener;

import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.TFGuilds;
import me.totalfreedom.tfguilds.config.ConfigEntry;
import me.totalfreedom.tfguilds.guild.Guild;
import me.totalfreedom.tfguilds.guild.GuildRank;
import me.totalfreedom.tfguilds.user.User;
import me.totalfreedom.tfguilds.util.GUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener
{
    private static final TFGuilds plugin = TFGuilds.getPlugin();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent e)
    {
        Player player = e.getPlayer();
        Guild guild = Guild.getGuild(player);
        if (guild == null)
        {
            return;
        }

        if (Common.IN_GUILD_CHAT.contains(player))
        {
            guild.chat(player.getName(), e.getMessage());
            e.setCancelled(true);
            return;
        }

        GuildRank rank = null;
        for (GuildRank r : guild.getRanks())
        {
            if (r != null)
            {
                if (r.getMembers() != null)
                {
                    if (r.getMembers().contains(player.getUniqueId()))
                    {
                        rank = r;
                    }
                }
            }
        }

        String display;
        if (rank == null)
        {
            if (guild.getOwner().equals(player.getUniqueId()))
            {
                display = "Guild Owner";
            }
            else if (guild.hasModerator(player.getUniqueId()))
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

        if (!ConfigEntry.GUILD_TAGS_ENABLED.getBoolean())
        {
            return;
        }

        User user = plugin.userData.get(player.getUniqueId());
        if (!user.isTag())
        {
            return;
        }

        if (guild.hasTag())
        {
            // This seems to result in the entry being logged twice on the console, which is silly... Not sure if there was a good reason for it. 
            //System.out.println(GUtil.colorize(guild.getTag().replace("%rank%", display)) + ChatColor.RESET + " " + e.getFormat());
            e.setFormat(GUtil.colorize(guild.getTag().replace("%rank%", display)) + ChatColor.RESET + " " + e.getFormat());
        }
    }
}