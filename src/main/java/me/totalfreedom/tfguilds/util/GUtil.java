package me.totalfreedom.tfguilds.util;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GUtil
{
    private static final SimpleDateFormat STANDARD = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");

    public static String flatten(String s)
    {
        String[] split = s.split(" ");
        for (int i = 0; i < split.length; i++)
        {
            split[i] = ChatColor.stripColor(colorize(split[i].toLowerCase()));
        }
        return StringUtils.join(split, "_");
    }

    public static String colorize(String string)
    {
        Matcher matcher = Pattern.compile("&#[a-f0-9A-F]{6}").matcher(string);
        while (matcher.find())
        {
            String code = matcher.group().replace("&", "");
            string = string.replace("&" + code, net.md_5.bungee.api.ChatColor.of(code) + "");
        }

        string = ChatColor.translateAlternateColorCodes('&', string);
        return string;
    }

    public static String format(long time)
    {
        Date date = new Date(time);
        return STANDARD.format(date);
    }

    public static List<String> BLACKLISTED_NAMES_AND_TAGS = Arrays.asList(
            "admin", "owner", "moderator", "developer", "console", "dev", "staff", "mod", "sra", "sta", "sa", "super admin", "telnet admin", "senior admin");
}