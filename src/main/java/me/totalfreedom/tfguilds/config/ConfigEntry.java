package me.totalfreedom.tfguilds.config;

import me.totalfreedom.tfguilds.TFGuilds;
import org.bukkit.ChatColor;

public enum ConfigEntry
{
    SCHEME_PRIMARY("scheme.primary"),
    SCHEME_SECONDARY("scheme.secondary"),
    // Server
    GUILD_CHAT_LOGGING_ENABLED("server.guild_chat_logging.enabled"),
    GUILD_TAGS_ENABLED("server.guild_tags.enabled");

    private final String path;

    ConfigEntry(String path)
    {
        this.path = path;
    }

    private static final Config config = TFGuilds.getPlugin().config;

    public boolean getBoolean()
    {
        return config.getBoolean(path);
    }

    public void setBoolean(boolean value)
    {
        config.set(path, value);
        config.save();
    }

    public ChatColor getChatColor()
    {
        return ChatColor.valueOf(config.getString(path).toUpperCase());
    }
}