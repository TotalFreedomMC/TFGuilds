package me.totalfreedom.tfguilds.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import me.totalfreedom.tfguilds.TFGuilds;
import me.totalfreedom.tfguilds.config.ConfigEntry;
import org.bukkit.Bukkit;

public class SQLDatabase
{

    private Connection connection;

    public SQLDatabase(TFGuilds plugin)
    {
        String password = ConfigEntry.MYSQL_PASSWORD.getString();
        if (password == null)
        {
            password = "";
        }
        try
        {
            connection = DriverManager.getConnection(String.format("jdbc:mysql://%s:%d/%s",
                    ConfigEntry.MYSQL_HOST.getString(),
                    ConfigEntry.MYSQL_PORT.getInteger(),
                    ConfigEntry.MYSQL_DATABASE.getString()),
                    ConfigEntry.MYSQL_USERNAME.getString(),
                    password);
            createTables();
            plugin.getLogger().info("Connection to the MySQL server established!");
        }
        catch (SQLException ex)
        {
            plugin.getLogger().severe("Could not connect to MySQL server!");
        }
    }

    public Connection getConnection()
    {
        return connection;
    }

    private void createTables() throws SQLException
    {
        connection.prepareStatement("CREATE TABLE IF NOT EXISTS `users` (" +
                "`uuid` TEXT," +
                "`id` INT," +
                "`tag` BOOLEAN," +
                "`rowid` INTEGER AUTO_INCREMENT PRIMARY KEY)")
                .execute();
        connection.prepareStatement("CREATE TABLE IF NOT EXISTS `warps` (" +
                "`guild_id` TEXT," +
                "`name` TEXT," +
                "`x` DOUBLE," +
                "`y` DOUBLE," +
                "`z` DOUBLE," +
                "`world` TEXT," +
                "`rowid` INTEGER AUTO_INCREMENT PRIMARY KEY)")
                .execute();
        connection.prepareStatement("CREATE TABLE IF NOT EXISTS `guilds` (" +
                "`id` TEXT," +
                "`name` TEXT," +
                "`owner` INT," +
                "`moderators` TEXT," +
                "`members` TEXT," +
                "`tag` TEXT," +
                "`default_rank` TEXT," +
                "`state` INT," +
                "`motd` TEXT," +
                "`x` DOUBLE," +
                "`y` DOUBLE," +
                "`z` DOUBLE," +
                "`world` TEXT," +
                "`creation` LONG," +
                "`rowid` INTEGER AUTO_INCREMENT PRIMARY KEY)")
                .execute();
        connection.prepareStatement("CREATE TABLE IF NOT EXISTS `ranks` (" +
                "`guild_id` TEXT," +
                "`name` TEXT," +
                "`members` TEXT," +
                "`rowid` INTEGER AUTO_INCREMENT PRIMARY KEY)")
                .execute();
    }
}
