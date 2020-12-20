package me.totalfreedom.tfguilds.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import me.totalfreedom.tfguilds.TFGuilds;
import me.totalfreedom.tfguilds.guild.GuildWarp;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class SQLWarpData
{
    private static final TFGuilds plugin = TFGuilds.getPlugin();
    private static final String TABLE_NAME = "warps";

    private final String SELECT = "SELECT * FROM `" + TABLE_NAME + "` WHERE identifier=? AND warp_name=?";
    private final String SELECT_ALL = "SELECT * FROM `" + TABLE_NAME + "`";
    private final String UPDATE = "UPDATE `" + TABLE_NAME + "` WHERE identifier=? AND warp_name=? SET x=?, y=?, z=?, world=?";
    private final String DELETE = "DELETE FROM `" + TABLE_NAME + "` WHERE identifier=? AND warp_name=?";
    private final String INSERT = "INSERT INTO `" + TABLE_NAME + "` (`identifier`, `warp_name`, `x`, `y`, `z`, `world`) VALUES (?, ?, ?, ?, ?, ?);";

    public boolean exists(String identifier, String warpName)
    {
        try (Connection connection = plugin.sql.getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(SELECT);
            statement.setString(1, identifier);
            statement.setString(2, warpName);
            ResultSet set = statement.executeQuery();
            return set.next();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return false;
    }

    public GuildWarp get(String identifier, String warpName)
    {
        try (Connection connection = plugin.sql.getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(SELECT);
            statement.setString(1, identifier);
            statement.setString(2, warpName);
            ResultSet set = statement.executeQuery();
            double x = set.getDouble("x");
            double y = set.getDouble("y");
            double z = set.getDouble("z");
            World world = plugin.worldData.getWorld(set.getInt("world"));
            return new GuildWarp(identifier, warpName, x, y, z, world);
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    public List<GuildWarp> getAll()
    {
        try (Connection connection = plugin.sql.getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(SELECT_ALL);
            ResultSet set = statement.executeQuery();
            List<GuildWarp> warps = new ArrayList<>();
            while (set.next())
            {
                warps.add(get(set.getString("identifier"), set.getString("warp_name")));
            }
            return warps;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    public GuildWarp create(String identifier, String warpName, Player player)
    {
        try (Connection connection = plugin.sql.getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(INSERT);
            statement.setString(1, identifier);
            statement.setString(2, warpName);
            statement.setDouble(3, player.getLocation().getX());
            statement.setDouble(4, player.getLocation().getY());
            statement.setDouble(5, player.getLocation().getZ());
            statement.setInt(6, plugin.worldData.getWorldID(player.getWorld()));
            statement.execute();
            return new GuildWarp(identifier, warpName, player.getLocation().getX(), player.getLocation().getY(),
                    player.getLocation().getZ(), player.getWorld());
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    public void save(GuildWarp warp)
    {
        try (Connection connection = plugin.sql.getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(UPDATE);
            statement.setString(1, warp.getIguild());
            statement.setString(2, warp.getWarpName());
            statement.setDouble(3, warp.getX());
            statement.setDouble(4, warp.getY());
            statement.setDouble(5, warp.getZ());
            statement.setInt(6, plugin.worldData.getWorldID(warp.getWorld()));
            statement.execute();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    public void delete(GuildWarp warp)
    {
        try (Connection connection = plugin.sql.getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(DELETE);
            statement.setString(1, warp.getIguild());
            statement.setString(2, warp.getWarpName());
            statement.execute();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }
}