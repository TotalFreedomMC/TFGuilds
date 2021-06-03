package me.totalfreedom.tfguilds.guild;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import me.totalfreedom.tfguilds.TFGuilds;
import org.bukkit.entity.Player;

public class User
{

    private static Map<Integer, User> users = new HashMap<>();
    //
    private final int id;
    private final UUID uuid;
    private boolean tag;

    public User(int id, UUID uuid, boolean tag)
    {
        this.id = id;
        this.uuid = uuid;
        this.tag = tag;
    }

    public static User create(Player player)
    {
        int id = users.size() + 1;
        User user = new User(id, player.getUniqueId(), true);
        users.put(id, user);
        user.save(true);
        return user;
    }

    public static void loadAll()
    {
        Connection connection = TFGuilds.getPlugin().getSQL().getConnection();
        try
        {
            ResultSet set = connection.prepareStatement("SELECT * FROM users").executeQuery();
            while (set.next())
            {
                int id = set.getInt("id");
                UUID uuid = UUID.fromString(set.getString("uuid"));
                boolean tag = set.getBoolean("tag");
                users.put(id, new User(id, uuid, tag));
            }
            TFGuilds.getPlugin().getLogger().info(users.size() + " users loaded!");
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    public static boolean hasUser(int id)
    {
        return users.get(id) != null;
    }

    public static User getUserFromId(int id)
    {
        if (hasUser(id))
        {
            return users.get(id);
        }
        return null;
    }

    public static User getUserFromUuid(UUID uuid)
    {
        for (User user : users.values())
        {
            if (user.getUuid().equals(uuid))
            {
                return user;
            }
        }
        return null;
    }

    public static User getUserFromPlayer(Player player)
    {
        return getUserFromUuid(player.getUniqueId());
    }

    public int getId()
    {
        return id;
    }

    public UUID getUuid()
    {
        return uuid;
    }

    public boolean displayTag()
    {
        return tag;
    }

    public void setDisplayTag(boolean tag)
    {
        this.tag = tag;
        save();
    }

    public void save(boolean newSave)
    {
        Connection connection = TFGuilds.getPlugin().getSQL().getConnection();
        try
        {
            PreparedStatement statement = newSave ? connection.prepareStatement("INSERT INTO users (`uuid`, `id`, `tag`) VALUES (?, ?, ?)")
                    : connection.prepareStatement("UPDATE users SET tag=? WHERE id=?");
            if (newSave)
            {
                statement.setString(1, uuid.toString());
                statement.setInt(2, id);
                statement.setBoolean(3, tag);
            }
            else
            {
                statement.setBoolean(1, tag);
                statement.setInt(2, id);
            }
            statement.execute();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void save()
    {
        save(false);
    }
}
