package me.totalfreedom.tfguilds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.totalfreedom.tfguilds.config.ConfigEntry;
import me.totalfreedom.tfguilds.guild.Guild;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Common
{
    protected static TFGuilds plugin = TFGuilds.getPlugin();

    public static final ChatColor PRIMARY = ConfigEntry.SCHEME_PRIMARY.getChatColor();
    public static final ChatColor SECONDARY = ConfigEntry.SCHEME_SECONDARY.getChatColor();
    public static final String NO_PERMS = ChatColor.RED + "No permission.";
    public static final String PREFIX = "%s%[%p%TFGuilds%s%] %p%";
    public static final String PNF = ChatColor.RED + "Player not found.";

    public static Map<Player, Guild> INVITES = new HashMap<>();
    public static List<Player> IN_GUILD_CHAT = new ArrayList<>();
    public static List<Player> CHAT_SPY = new ArrayList<>();

    public static String tl(String in)
    {
        return in.replaceAll("%p%", PRIMARY + "").replaceAll("%s%", SECONDARY + "");
    }
}