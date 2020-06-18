package totalfreedom.tfguilds;

import org.bukkit.plugin.java.JavaPlugin;
import totalfreedom.tfguilds.bridge.TFMBridge;
import totalfreedom.tfguilds.command.CreateGuildCommand;
import totalfreedom.tfguilds.command.TfGuildsCommand;
import totalfreedom.tfguilds.config.Config;
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
    }
}
