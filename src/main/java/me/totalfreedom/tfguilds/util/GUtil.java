package me.totalfreedom.tfguilds.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.totalfreedom.tfguilds.TFGuilds;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class GUtil
{

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEE d MMM yyyy HH:mm:ss");
    private static final List<String> BLACKLISTED_NAMES_AND_TAGS = Arrays.asList(
            "admin", "owner", "moderator", "developer", "console", "dev", "staff",
            "mod", "sra", "sta", "sa", "super admin", "telnet admin", "senior admin",
            "trial mod", "trial moderator", "trialmod", "trialmoderator");
    private static final Pattern CHAT_COLOR_FORMAT = Pattern.compile("&[a-fk-or0-9]", Pattern.CASE_INSENSITIVE);

    public static String colorize(String string)
    {
        if (string != null)
        {
            Matcher matcher = Pattern.compile("&#[a-f0-9A-F]{6}").matcher(string);
            while (matcher.find())
            {
                String code = matcher.group().replace("&", "");
                string = string.replace("&" + code, ChatColor.of(code) + "");
            }

            string = ChatColor.translateAlternateColorCodes('&', string);
        }
        return string;
    }

    public static String removeColorCodes(String string)
    {
        String s = null;
        if (string != null)
        {
            Matcher matcher = CHAT_COLOR_FORMAT.matcher(string);
            while (matcher.find())
            {
                s = string.replaceAll(matcher.group(), "");
            }
        }
        return s;
    }

    public static boolean containsBlacklistedWord(String string)
    {
        for (String blacklist : BLACKLISTED_NAMES_AND_TAGS)
        {
            if (string.contains(blacklist))
            {
                return true;
            }
        }
        return false;
    }

    public static List<String> getPlayerNames()
    {
        List<String> names = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers())
        {
            if (!TFGuilds.getPlugin().getTfmBridge().isVanished(player))
            {
                names.add(player.getName());
            }
        }
        return names;
    }

    public static String formatTime(long time)
    {
        Date date = new Date(time);
        return DATE_FORMAT.format(date);
    }
}
