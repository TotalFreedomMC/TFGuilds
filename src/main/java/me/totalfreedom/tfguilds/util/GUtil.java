package me.totalfreedom.tfguilds.util;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.md_5.bungee.api.ChatColor;

public class GUtil
{

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEE d MMM yyyy HH:mm:ss");
    private static final List<String> BLACKLISTED_NAMES_AND_TAGS = Arrays.asList(
            "admin", "owner", "moderator", "developer", "console", "dev", "staff",
            "mod", "sra", "sta", "sa", "super admin", "telnet admin", "senior admin",
            "trial mod", "trial moderator", "trialmod", "trialmoderator");

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

    public static String formatTime(long time)
    {
        Date date = new Date(time);
        return DATE_FORMAT.format(date);
    }
}
