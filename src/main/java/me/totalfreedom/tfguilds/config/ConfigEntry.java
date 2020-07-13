package me.totalfreedom.tfguilds.config;

import me.totalfreedom.tfguilds.TFGuilds;
import org.bukkit.ChatColor;

public enum ConfigEntry
{
    SCHEME_PRIMARY("scheme.primary"),
    SCHEME_SECONDARY("scheme.secondary");

    private final String path;

    ConfigEntry(String path)
    {
        this.path = path;
    }

    private static Config config = TFGuilds.getPlugin().config;

    public ChatColor getChatColor()
    {
        return ChatColor.valueOf(config.getString(path).toUpperCase());
    }
}