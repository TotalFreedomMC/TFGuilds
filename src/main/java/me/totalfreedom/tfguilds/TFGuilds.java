package me.totalfreedom.tfguilds;

import me.totalfreedom.tfguilds.bridge.TFMBridge;
import me.totalfreedom.tfguilds.command.GuildChatCommand;
import me.totalfreedom.tfguilds.command.GuildCommand;
import me.totalfreedom.tfguilds.command.TFGuildsCommand;
import me.totalfreedom.tfguilds.config.Config;
import me.totalfreedom.tfguilds.listener.ChatListener;
import me.totalfreedom.tfguilds.util.GLog;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class TFGuilds extends JavaPlugin
{
    private static TFGuilds plugin;
    public static TFGuilds getPlugin()
    {
        return plugin;
    }

    public Config config;
    public Config guilds;
    public TFMBridge bridge;

    @Override
    public void onEnable()
    {
        plugin = this;
        config = new Config("config.yml");
        guilds = new Config("guilds.yml");
        bridge = new TFMBridge();
        this.getCommand("guild").setExecutor(new GuildCommand());
        this.getCommand("guildchat").setExecutor(new GuildChatCommand());
        this.getCommand("tfguilds").setExecutor(new TFGuildsCommand());
        loadListeners();
        GLog.info("Enabled " + this.getDescription().getFullName());
    }

    @Override
    public void onDisable()
    {
        plugin = null;
        config.save();
        guilds.save();
        GLog.info("Disabled " + this.getDescription().getFullName());
    }

    private void loadListeners()
    {
        PluginManager manager = this.getServer().getPluginManager();
        manager.registerEvents(new ChatListener(), this);
    }
}
