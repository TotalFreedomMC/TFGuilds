package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.util.GBase;
import me.totalfreedom.tfguilds.util.GLog;
import me.totalfreedom.tfguilds.util.GMessage;
import me.totalfreedom.tfguilds.util.GUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class GuildAdminCommand extends GBase implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        Player player = (Player) sender;
        if (!plugin.tfmb.isAdmin(player))
        {
            player.sendMessage(GMessage.NO_PERMISSION);
            return true;
        }

        if (args.length != 2)
        {
            return false;
        }

        String guild = GUtil.getGuild(args[1]);
        if (guild == null)
        {
            player.sendMessage(GMessage.GUILD_NOT_FOUND);
            return true;
        }

        if (args[0].equalsIgnoreCase("cleartag"))
        {
            GUtil.setTag(GUtil.color("&8[&7" + guild + "&8]&r "), guild);
            player.sendMessage(ChatColor.GREEN + "Successfully cleared tag for guild " + guild);
            return true;
        }

        if (args[0].equalsIgnoreCase("disbandguild"))
        {
            GUtil.deleteGuild(guild);
            Bukkit.broadcastMessage(GUtil.color("&c&l" + guild + " &chas been disbanded"));
            player.sendMessage(ChatColor.GREEN + "Successfully deleted and cleared data for " + guild + ".");
            GLog.info(player.getName() + " deleted guild " + guild);
            return true;
        }

        if (args[0].equalsIgnoreCase("join"))
        {
            if (GUtil.isConsole(sender))
            {
                sender.sendMessage(GMessage.PLAYER_ONLY);
                return true;
            }

            if (GUtil.isGuildMember(player, GUtil.getGuild(player)))
            {
                player.sendMessage(GMessage.IN_GUILD);
                return true;
            }

            List<String> players = plugin.guilds.getStringList("guilds." + guild + ".members");
            players.add(player.getName());
            plugin.guilds.set("guilds." + guild + ".members", players);
            plugin.guilds.save();
            player.sendMessage(ChatColor.GREEN + "You have successfully joined " + guild);
            for (Player p : Bukkit.getOnlinePlayers())
            {
                if (GUtil.isGuildMember(p, guild))
                {
                    p.sendMessage(ChatColor.GREEN + player.getName() + " has joined the guild");
                }
            }
        }
        return true;
    }
}