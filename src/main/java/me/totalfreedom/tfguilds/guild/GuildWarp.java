package me.totalfreedom.tfguilds.guild;

import lombok.Getter;
import me.totalfreedom.tfguilds.TFGuilds;
import org.bukkit.World;
import org.bukkit.entity.Player;

@Getter
public class GuildWarp
{
    private static final TFGuilds plugin = TFGuilds.getPlugin();

    private final String iguild;
    private final String warpName;
    private final double x;
    private final double y;
    private final double z;
    private final World world;

    public GuildWarp(String iguild, String warpName, double x, double y, double z, World world)
    {
        this.iguild = iguild;
        this.warpName = warpName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
    }

    public static GuildWarp createGuildWarp(String identifier, String name, Player player)
    {
        return plugin.warpData.create(identifier, name, player);
    }

    public void save()
    {
        plugin.warpData.save(this);
    }

    public void delete()
    {
        plugin.warpData.delete(this);
    }
}