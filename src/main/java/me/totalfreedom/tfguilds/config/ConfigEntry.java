package me.totalfreedom.tfguilds.config;

import me.totalfreedom.tfguilds.TFGuilds;

public enum ConfigEntry
{

    GUILD_CHAT_LOGGING("server.guild_chat_logging"),
    GUILD_TAGS("server.guild_tags"),
    GUILD_TAG_MAX_LENGTH("server.guild_tag_max_length"),
    GLOBAL_TAG_MAX_LENGTH("server.global_tag_max_length"),
    CONNECTION_TYPE("settings.connection"),
    MYSQL_HOST("mysql.host"),
    MYSQL_PORT("mysql.port"),
    MYSQL_USERNAME("mysql.username"),
    MYSQL_PASSWORD("mysql.password"),
    MYSQL_DATABASE("mysql.database");

    private final String path;
    private final Config config;

    ConfigEntry(String path)
    {
        this.path = path;
        this.config = TFGuilds.getPlugin().getConfig();
    }

    public boolean getBoolean()
    {
        return config.getBoolean(path);
    }

    public void setBoolean(boolean value)
    {
        config.set(path, value);
        config.save();
    }

    public int getInteger()
    {
        return config.getInt(path);
    }

    public String getString()
    {
        return config.getString(path);
    }

    public String getName() {
        return path;
    }
}
