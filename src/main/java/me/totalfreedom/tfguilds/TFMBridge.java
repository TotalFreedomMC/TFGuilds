package me.totalfreedom.tfguilds;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import me.totalfreedom.tfguilds.util.Reflections;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class TFMBridge
{

    private final TFGuilds plugin = TFGuilds.getPlugin();
    private /*TotalFreedomMod*/ Plugin tfm = null;

    public /*TotalFreedomMod*/ Plugin getTfm()
    {
        if (tfm == null)
        {
            try
            {
                final Plugin tfmPlugin = plugin.getServer().getPluginManager().getPlugin("TotalFreedomMod");
                if (tfmPlugin != null && tfmPlugin.isEnabled())
                {
                    tfm = /*(TotalFreedomMod)*/tfmPlugin;
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
        Object al = Reflections.getField(getTfm(), "al");
        Method isAdmin = Reflections.getMethod(al, "isAdmin", Player.class);
        try
        {
            return (boolean)isAdmin.invoke(al, player) /*getTfm().al.isAdmin(player)*/;
        }
        catch (IllegalAccessException | InvocationTargetException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isAdmin(CommandSender sender)
    {
        if (getTfm() == null)
        {
            Bukkit.getLogger().warning("TotalFreedomMod not detected, checking operator status instead.");
            return sender.isOp();
        }
        Object al = Reflections.getField(getTfm(), "al");
        Method isAdmin = Reflections.getMethod(al, "isAdmin", CommandSender.class);
        try
        {
            return (boolean)isAdmin.invoke(al, sender) /*getTfm().al.isAdmin(player)*/;
        }
        catch (IllegalAccessException | InvocationTargetException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isVanished(Player player)
    {
        if (getTfm() == null)
        {
            Bukkit.getLogger().warning("TotalFreedomMod not detected, vanish will return false.");
            return false;
        }
        Object al = Reflections.getField(getTfm(), "al");
        Method isVanished = Reflections.getMethod(al, "isVanished", String.class);
        try
        {
            return (boolean)isVanished.invoke(al, player.getName()) /*getTfm().al.isVanished(player.getName)*/;
        }
        catch (IllegalAccessException | InvocationTargetException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public String getTag(Player player)
    {
        if (getTfm() == null)
        {
            return null;
        }

        Object pl = Reflections.getField(getTfm(), "pl");
        Method getPlayer = Reflections.getMethod(pl, "getPlayer", Player.class);
        try
        {
            Object fPlayer = getPlayer.invoke(pl, player);
            Method getTag = Reflections.getMethod(fPlayer, "getTag");

            return (String)getTag.invoke(fPlayer);
        }
        catch (IllegalAccessException | InvocationTargetException e)
        {
            e.printStackTrace();
        }

        return "" /*ChatColor.stripColor(getTfm().pl.getPlayer(player).getTag())*/;
    }

    public void clearTag(Player player)
    {
        if (getTfm() == null)
        {
            return;
        }
//        getTfm().pl.getPlayer(player).setTag(null);
        Object pl = Reflections.getField(getTfm(), "pl");
        Method getPlayer = Reflections.getMethod(pl, "getPlayer", Player.class);
        try
        {
            Object fPlayer = getPlayer.invoke(pl, player);
            Method setTag = Reflections.getMethod(fPlayer, "setTag", String.class);

            setTag.invoke(fPlayer, (Object)null);
        }
        catch (IllegalAccessException | InvocationTargetException e)
        {
            e.printStackTrace();
        }
    }
}
