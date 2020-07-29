package me.totalfreedom.tfguilds.bridge;

import me.totalfreedom.tfguilds.TFGuilds;
import me.totalfreedom.totalfreedommod.TotalFreedomMod;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class TFMBridge
{
    private TFGuilds plugin;
    private TotalFreedomMod tfmPlugin;

    public TFMBridge()
    {
        this.plugin = TFGuilds.getPlugin();
        this.tfmPlugin = null;
    }

    public TotalFreedomMod getTFM()
    {
        if (tfmPlugin == null)
        {
            try
            {
                final Plugin tfm = plugin.getServer().getPluginManager().getPlugin("TotalFreedomMod");
                if (tfm != null && tfm instanceof TotalFreedomMod)
                {
                    tfmPlugin = (TotalFreedomMod)tfm;
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        return tfmPlugin;
    }

    public boolean isAdmin(Player player)
    {
        return getTFM().al.isAdmin(player);
    }

    public boolean isAdmin(CommandSender sender)
    {
        return getTFM().al.isAdmin(sender);
    }
}