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

public class GuildKickCommand extends GBase implements CommandExecutor
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
        String guild = GUtil.getGuild(player);

        if (guild == null)
        {
            player.sendMessage(GMessage.NOT_IN_GUILD);
            return true;
        }

        String owner = GUtil.getOwner(guild);
        if (!owner.equalsIgnoreCase(player.getName()))
        {
            player.sendMessage(GMessage.NOT_OWNER);
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null)
        {
            player.sendMessage(GMessage.PLAYER_NOT_FOUND);
            return true;
        }

        if (!GUtil.isGuildMember(target, GUtil.getGuild(player)))
        {
            player.sendMessage(ChatColor.RED + "That player isn't in your guild.");
            return true;
        }

        if (target == player)
        {
            player.sendMessage(ChatColor.RED + "You may not kick yourself.");
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
        player.sendMessage(ChatColor.GREEN + "Successfully kicked " + target.getName() + " from the guild");
        target.sendMessage(ChatColor.RED + "You have been kicked from guild " + guild);
        return true;
    }
}