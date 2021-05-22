package me.totalfreedom.tfguilds;

import me.totalfreedom.totalfreedommod.TotalFreedomMod;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class TFMBridge
{

    private final TFGuilds plugin = TFGuilds.getPlugin();
    private TotalFreedomMod tfm = null;

    public TotalFreedomMod getTfm()
    {
        if (tfm == null)
        {
            try
            {
                final Plugin tfmPlugin = plugin.getServer().getPluginManager().getPlugin("TotalFreedomMod");
                if (tfmPlugin != null && tfmPlugin.isEnabled() && tfmPlugin instanceof TotalFreedomMod)
                {
                    tfm = (TotalFreedomMod)tfmPlugin;
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        return tfm;
    }

    public boolean isAdmin(Player player)
    {
        if (getTfm() == null)
        {
            Bukkit.getLogger().warning("TotalFreedomMod not detected, checking operator status instead.");
            return player.isOp();
        }
        return getTfm().al.isAdmin(player);
    }

    public boolean isAdmin(CommandSender sender)
    {
        if (getTfm() == null)
        {
            Bukkit.getLogger().warning("TotalFreedomMod not detected, checking operator status instead.");
            return sender.isOp();
        }
        return getTfm().al.isAdmin(sender);
    }

    public boolean isVanished(Player player)
    {
        if (getTfm() == null)
        {
            Bukkit.getLogger().warning("TotalFreedomMod not detected, vanish will return false.");
            return false;
        }
        return getTfm().al.isVanished(player.getName());
    }
}
