package me.totalfreedom.tfguilds.bridge;

import me.totalfreedom.tfguilds.TFGuilds;
import me.totalfreedom.tfguilds.util.GLog;
import me.totalfreedom.totalfreedommod.TotalFreedomMod;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class TFMBridge
{
    private final TFGuilds plugin;
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
        if (getTFM() == null)
        {
            GLog.warn("TFM not detected on the server. Checking if player is OP...");
            return player.isOp();
        }

        return getTFM().sl.isStaff(player);
    }

    public boolean isAdmin(CommandSender sender)
    {
        if (getTFM() == null)
        {
            GLog.warn("TFM not detected on the server. Checking if sender is OP...");
            return sender.isOp();
        }

        return getTFM().sl.isStaff(sender);
    }

    public boolean isVanished(Player player)
    {
        return getTFM().sl.isVanished(player.getName());
    }
}