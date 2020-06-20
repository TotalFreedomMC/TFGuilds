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

public class LeaveGuildCommand extends GBase implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
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
        if (owner.equalsIgnoreCase(player.getName()))
        {
            sender.sendMessage(ChatColor.RED + "You may not leave your guild. However, if you want to delete it run /disbandguild");
            return true;
        }

        if (args.length == 0)
        {
            sender.sendMessage(ChatColor.RED + "Are you sure you want to leave your guild? Type 'CONFIRM' to continue.");
            return true;
        }

        if (args[0].toLowerCase().equalsIgnoreCase("confirm"))
        {
            List<String> players = plugin.guilds.getStringList("guilds." + guild + ".members");
            players.remove(player.getName());
            plugin.guilds.set("guilds." + guild + ".members", players);
            plugin.guilds.save();
            for (Player p : Bukkit.getOnlinePlayers())
            {
                if (GUtil.isGuildMember(p, guild))
                {
                    p.sendMessage(ChatColor.RED + player.getName() + " has left the guild");
                }
            }
            sender.sendMessage(ChatColor.GREEN + "Successfully left " + guild + ".");
            return true;
        }
        return true;
    }
}
