package me.totalfreedom.tfguilds;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import me.totalfreedom.tfguilds.util.ReflectionsHelper;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class TFMBridge
{
    private final TFGuilds plugin = TFGuilds.getPlugin();
    private /*TotalFreedomMod*/ Plugin tfm = null;

    public /*TotalFreedomMod*/ Plugin getTFM()
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
        if (getTFM() == null)
        {
            Bukkit.getLogger().warning("TotalFreedomMod not detected, checking operator status instead.");
            return player.isOp();
        }
        Object adminList = ReflectionsHelper.getField(getTFM(), "adminList");
        Method isAdmin = ReflectionsHelper.getMethod(adminList, "isAdmin", Player.class);
        try
        {
            return (boolean)isAdmin.invoke(adminList, player) /*getTfm().al.isAdmin(player)*/;
        }
        catch (IllegalAccessException | InvocationTargetException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isAdmin(CommandSender sender)
    {
        if (getTFM() == null)
        {
            Bukkit.getLogger().warning("TotalFreedomMod not detected, checking operator status instead.");
            return sender.isOp();
        }
        Object adminList = ReflectionsHelper.getField(getTFM(), "adminList");
        Method isAdmin = ReflectionsHelper.getMethod(adminList, "isAdmin", CommandSender.class);
        try
        {
            return (boolean)isAdmin.invoke(adminList, sender) /*getTfm().al.isAdmin(player)*/;
        }
        catch (IllegalAccessException | InvocationTargetException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isVanished(Player player)
    {
        if (getTFM() == null)
        {
            Bukkit.getLogger().warning("TotalFreedomMod not detected, vanish will return false.");
            return false;
        }
        Object adminList = ReflectionsHelper.getField(getTFM(), "adminList");
        Method isVanished = ReflectionsHelper.getMethod(adminList, "isVanished", String.class);
        try
        {
            return (boolean)isVanished.invoke(adminList, player.getName()) /*getTfm().adminList.isVanished(player.getName)*/;
        }
        catch (IllegalAccessException | InvocationTargetException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public String getTag(Player player)
    {
        if (getTFM() == null)
        {
            return null;
        }

        Object playerList = ReflectionsHelper.getField(getTFM(), "playerList");
        Method getPlayer = ReflectionsHelper.getMethod(playerList, "getPlayer", Player.class);
        try
        {
            Object fPlayer = getPlayer.invoke(playerList, player);
            Method getTag = ReflectionsHelper.getMethod(fPlayer, "getTag");

            return (String)getTag.invoke(fPlayer);
        }
        catch (IllegalAccessException | InvocationTargetException e)
        {
            e.printStackTrace();
        }

        return "" /*ChatColor.stripColor(getTfm().playerList.getPlayer(player).getTag())*/;
    }

    public void clearTag(Player player)
    {
        if (getTFM() == null)
        {
            return;
        }
//        getTfm().playerList.getPlayer(player).setTag(null);
        Object playerList = ReflectionsHelper.getField(getTFM(), "playerList");
        Method getPlayer = ReflectionsHelper.getMethod(playerList, "getPlayer", Player.class);
        try
        {
            Object fPlayer = getPlayer.invoke(playerList, player);
            Method setTag = ReflectionsHelper.getMethod(fPlayer, "setTag", String.class);

            setTag.invoke(fPlayer, (Object)null);
        }
        catch (IllegalAccessException | InvocationTargetException e)
        {
            e.printStackTrace();
        }
    }
}
