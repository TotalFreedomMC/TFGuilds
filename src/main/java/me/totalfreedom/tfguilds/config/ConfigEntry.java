package me.totalfreedom.tfguilds.config;

import me.totalfreedom.tfguilds.TFGuilds;
import org.bukkit.ChatColor;

public enum ConfigEntry
{
    SCHEME_PRIMARY("scheme.primary"),
    SCHEME_SECONDARY("scheme.secondary"),
    // Server
    SERVER_GUILD_CHAT_LOGGING_ENABLED("server.guild_chat_logging.enabled");

    private final String path;

    ConfigEntry(String path)
    {
        this.path = path;
    }

    private static Config config = TFGuilds.getPlugin().config;

    public boolean getBoolean()
    {
        return config.getBoolean(path);
    }

    public ChatColor getChatColor()
    {
        return ChatColor.valueOf(config.getString(path).toUpperCase());
    }
}