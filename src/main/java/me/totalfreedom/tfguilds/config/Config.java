package me.totalfreedom.tfguilds.config;

import java.io.File;
import java.io.IOException;
import me.totalfreedom.tfguilds.TFGuilds;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config extends YamlConfiguration
{

    private final TFGuilds plugin;
    private final File file;

    public Config(String fileName)
    {
        this.plugin = TFGuilds.getPlugin();
        this.file = new File(plugin.getDataFolder(), fileName);

        if (!file.exists())
        {
            options().copyDefaults(true);
            plugin.saveResource(fileName, false);
        }

        load();
    }

    public void save()
    {
        try
        {
            super.save(file);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public void load()
    {
        try
        {
            super.load(file);
        }
        catch (IOException | InvalidConfigurationException ex)
        {
            ex.printStackTrace();
        }
    }
}
