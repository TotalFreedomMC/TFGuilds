package me.totalfreedom.tfguilds;

import java.util.HashMap;
import java.util.Map;
import me.totalfreedom.tfguilds.bridge.TFMBridge;
import me.totalfreedom.tfguilds.command.GuildChatCommand;
import me.totalfreedom.tfguilds.command.GuildChatSpyCommand;
import me.totalfreedom.tfguilds.command.GuildCommand;
import me.totalfreedom.tfguilds.command.TFGuildsCommand;
import me.totalfreedom.tfguilds.config.Config;
import me.totalfreedom.tfguilds.guild.Guild;
import me.totalfreedom.tfguilds.guild.GuildWarp;
import me.totalfreedom.tfguilds.listener.ChatListener;
import me.totalfreedom.tfguilds.listener.JoinListener;
import me.totalfreedom.tfguilds.sql.SQLDatabase;
import me.totalfreedom.tfguilds.sql.SQLGuildData;
import me.totalfreedom.tfguilds.sql.SQLRankData;
import me.totalfreedom.tfguilds.sql.SQLUserData;
import me.totalfreedom.tfguilds.sql.SQLWarpData;
import me.totalfreedom.tfguilds.sql.SQLWorldData;
import me.totalfreedom.tfguilds.util.GLog;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class TFGuilds extends JavaPlugin
{

    // TEMP FIX UNTIL REWRITE
    public Map<String, Guild> guilds;
    public Map<String, GuildWarp> warps;

    private static TFGuilds plugin;

    public static TFGuilds getPlugin()
    {
        return plugin;
    }

    public Config config;
    public TFMBridge bridge;
    public SQLDatabase sql;
    public SQLGuildData guildData;
    public SQLRankData rankData;
    public SQLUserData userData;
    public SQLWarpData warpData;
    public SQLWorldData worldData;

    @Override
    public void onEnable()
    {
        plugin = this;
        config = new Config("config.yml");
        bridge = new TFMBridge();
        guilds = new HashMap<>();
        warps = new HashMap<>();
        sql = new SQLDatabase();
        guildData = new SQLGuildData();
        rankData = new SQLRankData();
        userData = new SQLUserData();
        warpData = new SQLWarpData();
        worldData = new SQLWorldData();
        guildData.getAll();
        warpData.getAll();
        loadCommands();
        loadListeners();
        GLog.info("Enabled " + this.getDescription().getFullName());
    }

    @Override
    public void onDisable()
    {
        plugin = null;
        config.save();
        GLog.info("Disabled " + this.getDescription().getFullName());
    }

    private void loadCommands()
    {
        this.getCommand("guild").setExecutor(new GuildCommand());
        this.getCommand("guildchat").setExecutor(new GuildChatCommand());
        this.getCommand("tfguilds").setExecutor(new TFGuildsCommand());
        this.getCommand("guildchatspy").setExecutor(new GuildChatSpyCommand());
    }

    private void loadListeners()
    {
        PluginManager manager = this.getServer().getPluginManager();
        manager.registerEvents(new ChatListener(), this);
        manager.registerEvents(new JoinListener(), this);
    }
}