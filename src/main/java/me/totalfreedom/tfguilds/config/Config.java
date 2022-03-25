package me.totalfreedom.tfguilds.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import me.totalfreedom.tfguilds.TFGuilds;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config extends YamlConfiguration
{

    private final File file;
    private final TFGuilds plugin;
    private final String fileName;

    public Config(TFGuilds plugin, String fileName)
    {

        this.fileName = fileName;
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), fileName);

        if (!file.exists())
        {
            plugin.saveResource(fileName, false);
        }

        verifyConfiguration();

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

    public void verifyConfiguration() {
        InputStreamReader stream = new InputStreamReader(plugin.getResource(fileName));
        YamlConfiguration reader = YamlConfiguration.loadConfiguration(stream);
        YamlConfiguration writer = YamlConfiguration.loadConfiguration(file);

        AtomicBoolean shouldSave = new AtomicBoolean(false);

        try {
            reader.getKeys(true).forEach(key -> {
                if (!writer.contains(key)) writer.set(key, reader.get(key));
                if (!shouldSave.get()) shouldSave.set(true);
            });
            if (shouldSave.get()) {
                writer.save(file);
            }
        } catch (IOException ex) {
            TFGuilds.getPlugin()
                    .getLogger()
                    .severe("Error attempting to verify configuration: \n"
                            + ex.getMessage()
                            + "\nCaused by: "
                            + ex.getCause());
        }
    }
}
