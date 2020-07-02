package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.util.GBase;
import me.totalfreedom.tfguilds.util.GMessage;
import me.totalfreedom.tfguilds.util.GUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Objects;

public class CreateGuildCommand extends GBase implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length == 0)
        {
            return false;
        }

        if (GUtil.isConsole(sender))
        {
            sender.sendMessage(GMessage.PLAYER_ONLY);
            return true;
        }

        Player player = (Player) sender;

        ConfigurationSection guildMembers = plugin.guilds.getConfigurationSection("guilds");

        if (guildMembers != null)
        {
            try
            {
                for (String guild : guildMembers.getKeys(false))
                {
                    if (Objects.requireNonNull(plugin.guilds.getString("guilds." + guild + ".members")).contains(player.getName()))
                    {
                        player.sendMessage(GMessage.IN_GUILD);
                        return true;
                    }

                    if (guild.equals(args[0].toLowerCase()))
                    {
                        player.sendMessage(ChatColor.RED + "A guild with that name already exists.");
                        return true;
                    }

                    if (args[0].toLowerCase().length() > 24)
                    {
                        player.sendMessage(ChatColor.RED + "Guild name must not be over 24 characters.");
                        return true;
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        for (String blacklisted : GUtil.BLACKLISTED_NAMES_AND_TAGS)
        {
            if (args[0].equalsIgnoreCase(blacklisted))
            {
                if (!plugin.tfmb.isAdmin(player))
                {
                    player.sendMessage(ChatColor.RED + "You may not use that name.");
                    return true;
                }
            }
        }

        GUtil.createGuild(player, args[0].toLowerCase());
        Bukkit.broadcastMessage(GUtil.color("&a" + player.getName() + " has created guild &a&l" + args[0]));
        player.sendMessage(ChatColor.GREEN + "Successfully created a guild named " + args[0]);
        return true;
    }
}