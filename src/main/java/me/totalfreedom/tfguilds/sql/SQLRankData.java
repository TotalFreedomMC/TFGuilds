package me.totalfreedom.tfguilds.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import me.totalfreedom.tfguilds.TFGuilds;
import me.totalfreedom.tfguilds.guild.GuildRank;
import org.apache.commons.lang.StringUtils;

public class SQLRankData
{
    private static final TFGuilds plugin = TFGuilds.getPlugin();
    private static final String TABLE_NAME = "ranks";

    private final String SELECT = "SELECT * FROM `" + TABLE_NAME + "` WHERE guild_identifier=? AND identifier=?";
    private final String UPDATE = "UPDATE `" + TABLE_NAME + "` SET name=?, members=? WHERE guild_identifier=? AND identifier=?";
    private final String UPDATE_GUILD = "UPDATE `" + TABLE_NAME + "` SET guild_identifier=? WHERE guild_identifier=? AND identifier=?";
    private final String DELETE = "DELETE FROM `" + TABLE_NAME + "` WHERE guild_identifier=? AND identifier=?";
    private final String INSERT = "INSERT INTO `" + TABLE_NAME + "` (`guild_identifier`, `identifier`, `name`, `members`) " +
            "VALUES (?, ?, ?, ?);";

    public boolean exists(String guildIdentifier, String identifier)
    {
        try (Connection connection = plugin.sql.getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(SELECT);
            statement.setString(1, guildIdentifier);
            statement.setString(2, identifier);
            ResultSet set = statement.executeQuery();
            return set.next();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return false;
    }

    public GuildRank get(String guildIdentifier, String identifier)
    {
        try (Connection connection = plugin.sql.getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(SELECT);
            statement.setString(1, guildIdentifier);
            statement.setString(2, identifier);
            ResultSet set = statement.executeQuery();
            set.next();
            String name = set.getString("name");
            List<UUID> members = new ArrayList<>();
            if (set.getString("members") != null)
            {
                for (String stringMember : set.getString("members").split(","))
                    members.add(plugin.userData.get(Integer.parseInt(stringMember)).getUuid());
            }
            return new GuildRank(guildIdentifier, identifier, name, members);
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    public GuildRank create(String guildIdentifier, String identifier, String name)
    {
        try (Connection connection = plugin.sql.getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(INSERT);
            statement.setString(1, guildIdentifier);
            statement.setString(2, identifier);
            statement.setString(3, name);
            statement.setString(4, null);
            statement.execute();
            return new GuildRank(guildIdentifier, identifier, name, new ArrayList<>());
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    public void save(GuildRank rank)
    {
        try (Connection connection = plugin.sql.getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(UPDATE);
            statement.setString(1, rank.getName());
            List<String> members = new ArrayList<>();
            for (UUID member : rank.getMembers())
                members.add("" + plugin.userData.get(member).getId());
            statement.setString(2, members.size() == 0 ? null : StringUtils.join(members, ","));
            statement.setString(3, rank.getIguild());
            statement.setString(4, rank.getIdentifier());
            statement.execute();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    public void updateGuildIdentifier(GuildRank rank, String newIdentifier)
    {
        try (Connection connection = plugin.sql.getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(UPDATE_GUILD);
            statement.setString(1, newIdentifier);
            statement.setString(2, rank.getIguild());
            statement.setString(3, rank.getIdentifier());
            statement.execute();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    public void delete(GuildRank rank)
    {
        try (Connection connection = plugin.sql.getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(DELETE);
            statement.setString(1, rank.getIguild());
            statement.setString(2, rank.getIdentifier());
            statement.execute();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }
}