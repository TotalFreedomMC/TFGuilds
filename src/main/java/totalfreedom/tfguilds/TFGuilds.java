package totalfreedom.tfguilds;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import totalfreedom.tfguilds.bridge.TFMBridge;
import totalfreedom.tfguilds.command.CreateGuildCommand;
import totalfreedom.tfguilds.command.GuildChatCommand;
import totalfreedom.tfguilds.command.GuildTagCommand;
import totalfreedom.tfguilds.command.TfGuildsCommand;
import totalfreedom.tfguilds.config.Config;
import totalfreedom.tfguilds.listener.ChatManager;
import totalfreedom.tfguilds.util.GLog;

public final class TFGuilds extends JavaPlugin
{
    public static TFGuilds plugin;
    public Config config;
    public Config guilds;
    public TFMBridge tfmb;

    @Override
    public void onEnable()
    {
        plugin = this;
        enableCommands();
        enableListeners();
        config = new Config(plugin, "config.yml");
        guilds = new Config(plugin, "guilds.yml");
        tfmb = new TFMBridge();
        GLog.info("Enabled");
    }

    @Override
    public void onDisable()
    {
        config.save();
        guilds.save();
        GLog.info("Disabled");
    }

    private void enableCommands()
    {
        this.getCommand("tfguilds").setExecutor(new TfGuildsCommand());
        this.getCommand("createguild").setExecutor(new CreateGuildCommand());
        this.getCommand("guildtag").setExecutor(new GuildTagCommand());
        this.getCommand("guildchat").setExecutor(new GuildChatCommand());
    }

    private void enableListeners()
    {
        PluginManager manager = this.getServer().getPluginManager();
        manager.registerEvents(new ChatManager(), this);
    }
}
