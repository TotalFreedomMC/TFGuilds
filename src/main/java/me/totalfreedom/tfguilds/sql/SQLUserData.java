package me.totalfreedom.tfguilds.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import me.totalfreedom.tfguilds.TFGuilds;
import me.totalfreedom.tfguilds.guild.Guild;
import me.totalfreedom.tfguilds.guild.GuildRank;
import me.totalfreedom.tfguilds.guild.GuildState;
import me.totalfreedom.tfguilds.user.User;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class SQLUserData
{
    private static final TFGuilds plugin = TFGuilds.getPlugin();
    private static final String TABLE_NAME = "users";

    private final String SELECT = "SELECT * FROM `" + TABLE_NAME + "` WHERE uuid=?";
    private final String SELECT_ID = "SELECT * FROM `" + TABLE_NAME + "` WHERE id=?";
    private final String UPDATE = "UPDATE `" + TABLE_NAME + "` SET tag=? WHERE uuid=?";
    private final String INSERT = "INSERT INTO `" + TABLE_NAME + "` (`id`, `uuid`, `tag`) " +
            "VALUES (?, ?, ?);";
    private final String COUNT = "SELECT COUNT(*) AS rows FROM `" + TABLE_NAME + "`";

    public boolean exists(UUID uuid)
    {
        try (Connection connection = plugin.sql.getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(SELECT);
            statement.setString(1, uuid.toString());
            ResultSet set = statement.executeQuery();
            return set.next();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean existsID(int id)
    {
        try (Connection connection = plugin.sql.getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(SELECT_ID);
            statement.setInt(1, id);
            ResultSet set = statement.executeQuery();
            return set.next();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return false;
    }

    public User get(UUID uuid)
    {
        if (!exists(uuid)) create(uuid);
        try (Connection connection = plugin.sql.getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(SELECT);
            statement.setString(1, uuid.toString());
            ResultSet set = statement.executeQuery();
            set.next();
            int id = set.getInt("id");
            boolean tag = set.getBoolean("tag");
            return new User(id, uuid, tag);
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    public User get(int id)
    {
        try (Connection connection = plugin.sql.getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(SELECT_ID);
            statement.setInt(1, id);
            ResultSet set = statement.executeQuery();
            set.next();
            UUID uuid = UUID.fromString(set.getString("uuid"));
            boolean tag = set.getBoolean("tag");
            return new User(id, uuid, tag);
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    public User create(UUID uuid)
    {
        try (Connection connection = plugin.sql.getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(INSERT);
            int id = getUserCount() + 1;
            statement.setInt(1, id);
            statement.setString(2, uuid.toString());
            statement.setBoolean(3, true);
            statement.execute();
            return new User(id, uuid, true);
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    public void save(User user)
    {
        try (Connection connection = plugin.sql.getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(UPDATE);
            statement.setBoolean(1, user.isTag());
            statement.setString(2, user.getUuid().toString());
            statement.execute();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    public int getUserCount()
    {
        try (Connection connection = plugin.sql.getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(COUNT);
            ResultSet set = statement.executeQuery();
            set.next();
            int count = set.getInt("rows");
            set.close();
            return count;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return 0;
    }
}