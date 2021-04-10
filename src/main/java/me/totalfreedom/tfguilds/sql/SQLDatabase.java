package me.totalfreedom.tfguilds.sql;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import me.totalfreedom.tfguilds.TFGuilds;

public class SQLDatabase
{
    private static final String DATABASE_FILENAME = "database.db";
    private Connection connection;

    public SQLDatabase()
    {
        File file = new File(TFGuilds.getPlugin().getDataFolder(), DATABASE_FILENAME);
        if (!file.exists())
        {
            try
            {
                file.createNewFile();
                TFGuilds.getPlugin().saveResource(DATABASE_FILENAME, false);
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
        try
        {
            connection = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
            createTables();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    public Connection getConnection()
    {
        return connection;
    }

    private void createTables() throws SQLException
    {
        connection.prepareStatement("CREATE TABLE IF NOT EXISTS `users` (\n" +
                "\t`id` INT,\n" +
                "\t`uuid` TINYTEXT,\n" +
                "\t`tag` BOOL\n" +
                ");").execute();
        connection.prepareStatement("CREATE TABLE IF NOT EXISTS `worlds` (\n" +
                "\t`id` SMALLINT,\n" +
                "\t`name` TINYTEXT\n" +
                ");").execute();
        connection.prepareStatement("CREATE TABLE IF NOT EXISTS `ranks` (\n" +
                "\t`guild_identifier` TEXT,\n" +
                "\t`identifier` TEXT,\n" +
                "\t`name` TEXT,\n" +
                "\t`members` TEXT\n" +
                ");").execute();
        connection.prepareStatement("CREATE TABLE IF NOT EXISTS `guilds` (\n" +
                "\t`identifier` TEXT,\n" +
                "\t`name` TEXT,\n" +
                "\t`owner` INT,\n" +
                "\t`moderators` TEXT,\n" +
                "\t`members` TEXT,\n" +
                "\t`tag` TEXT,\n" +
                "\t`state` TINYINT,\n" +
                "\t`ranks` TEXT,\n" +
                "\t`motd` TEXT,\n" +
                "\t`x` DOUBLE,\n" +
                "\t`y` DOUBLE,\n" +
                "\t`z` DOUBLE,\n" +
                "\t`world` SMALLINT,\n" +
                "\t`default_rank` TEXT,\n" +
                "\t`creation` BIGINT\n" +
                ");").execute();
        connection.prepareStatement("CREATE TABLE IF NOT EXISTS `warps` (\n" +
                "\t`identifier` TEXT,\n" +
                "\t`warp_name` TEXT,\n" +
                "\t`x` DOUBLE,\n" +
                "\t`y` DOUBLE,\n" +
                "\t`z` DOUBLE,\n" +
                "\t`world` SMALLINT\n" +
                ");").execute();
    }
}