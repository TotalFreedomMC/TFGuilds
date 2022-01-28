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
import me.totalfreedom.tfguilds.TFGuilds;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config extends YamlConfiguration
{

    private final File file;

    public Config(String fileName)
    {
        TFGuilds plugin = TFGuilds.getPlugin();
        this.file = new File(plugin.getDataFolder(), fileName);

        if (!file.exists())
        {
            plugin.saveResource(fileName, false);
        }

        checkForFields();

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

    public void checkForFields()
    {
        if (!file.exists())
        {
            return;
        }

        Arrays.stream(ConfigEntry.values()).forEach(entry -> {
            if (entry.getString().isBlank() || entry.getString().isEmpty() || entry.getString() == null)
            {
                try
                {
                    String[] split = entry.getName().split("\\.");
                    String key = split[0];
                    String subKey = split[1];
                    String value = "REPLACE_ME";
                    FileWriter writer = new FileWriter(file, true);
                    InputStream is = TFGuilds.getPlugin().getResource("config.yml");
                    if (is == null) {
                        Bukkit.getServer().getLogger().severe("Unable to read from resource! Things may not work correctly!");
                        return;
                    }
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    while (reader.ready()) {
                        if (!reader.readLine().trim().startsWith(subKey)) {
                            continue;
                        }
                        value = reader.readLine().trim().split(":")[1].trim();
                        break;
                    }
                    writer.write("\n"
                            + key
                            + ": "
                            + "\n"
                            + subKey.indent(2)
                            + ": "
                            + value);
                }
                catch (IOException ignored)
                {
                    Bukkit.getServer().getLogger().severe("The FileWriter could not add the necessary configuration options to the yaml file! Things may not work correctly!");
                }
            }
        });
    }
}
