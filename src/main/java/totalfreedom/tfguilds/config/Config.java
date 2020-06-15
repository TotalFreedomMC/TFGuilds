package totalfreedom.tfguilds.config;

import org.bukkit.configuration.file.YamlConfiguration;
import totalfreedom.tfguilds.TFGuilds;

import java.io.File;

public class Config extends YamlConfiguration
{
    private final TFGuilds plugin;
    private final File file;

    public Config(TFGuilds plugin, String name)
    {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), name);

        if (!file.exists())
        {
            options().copyDefaults(true);
            plugin.saveResource(name, false);
        }
        load();
    }

    public void load()
    {
        try
        {
            super.load(file);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void save()
    {
        try
        {
            super.save(file);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}