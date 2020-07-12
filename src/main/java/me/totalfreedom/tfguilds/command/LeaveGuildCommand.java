package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.util.GBase;
import me.totalfreedom.tfguilds.util.GMessage;
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
        if (GUtil.isConsole(sender))
        {
            sender.sendMessage(GMessage.PLAYER_ONLY);
            return true;
        }

        Player player = (Player) sender;
        String guild = GUtil.getGuild(player);

        if (guild == null)
        {
            player.sendMessage(GMessage.NOT_IN_GUILD);
            return true;
        }

        String owner = GUtil.getOwner(guild);
        if (owner.equalsIgnoreCase(player.getName()))
        {
            player.sendMessage(ChatColor.RED + "You may not leave your guild. However, if you want to delete it run /disbandguild");
            return true;
        }

        if (args.length == 0)
        {
            player.sendMessage(ChatColor.RED + "Are you sure you want to leave your guild? Type '/leaveguild CONFIRM' or '/guildleave CONFIRM' to continue.");
            return true;
        }

        if (args[0].equalsIgnoreCase("confirm"))
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
            player.sendMessage(ChatColor.GREEN + "Successfully left " + guild + ".");
            return true;
        }
        return true;
    }
}
