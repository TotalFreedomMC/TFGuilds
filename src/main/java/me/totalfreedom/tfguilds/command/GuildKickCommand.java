package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.util.GBase;
import me.totalfreedom.tfguilds.util.GUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class GuildKickCommand extends GBase implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length == 0)
        {
            return false;
        }

        Player player = (Player) sender;
        if (GUtil.isConsole(player))
        {
            sender.sendMessage(ChatColor.RED + "You are not allowed to run this command.");
            return true;
        }

        String guild = GUtil.getGuild(player);
        if (guild == null)
        {
            sender.sendMessage(ChatColor.RED + "You aren't in a guild!");
            return true;
        }

        String owner = GUtil.getOwner(guild);
        if (!owner.equalsIgnoreCase(player.getName()))
        {
            sender.sendMessage(ChatColor.RED + "You aren't the owner of your guild!");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null)
        {
            sender.sendMessage(ChatColor.RED + "Player not found");
            return true;
        }

        if (target == player)
        {
            sender.sendMessage(ChatColor.RED + "You may not kick yourself.");
            return true;
        }

        List<String> players = plugin.guilds.getStringList("guilds." + guild + ".members");
        players.remove(target.getName());
        plugin.guilds.set("guilds." + guild + ".members", players);
        plugin.guilds.save();
        for (Player p : Bukkit.getOnlinePlayers())
        {
            if (GUtil.isGuildMember(p, guild))
            {
                p.sendMessage(ChatColor.RED + target.getName() + " has been kicked from the guild");
            }
        }
        sender.sendMessage(ChatColor.GREEN + "Successfully kicked " + target.getName() + " from the guild");
        target.sendMessage(ChatColor.RED + "You have been kicked from guild " + guild);
        return true;
    }
}