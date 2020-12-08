package me.totalfreedom.tfguilds.config;

import java.io.File;
import me.totalfreedom.tfguilds.TFGuilds;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config extends YamlConfiguration
{
    private final TFGuilds plugin;
    private final File file;

    public Config(String name)
    {
        this.plugin = TFGuilds.getPlugin();
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