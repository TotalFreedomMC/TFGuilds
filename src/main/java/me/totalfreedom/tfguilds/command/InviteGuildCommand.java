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

public class InviteGuildCommand extends GBase implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length == 1)
        {
            Player player = (Player) sender;
            String guild = GUtil.getGuild(player);
            Player target = Bukkit.getPlayer(args[0]);

            if (GUtil.isConsole(player))
            {
                player.sendMessage(ChatColor.RED + "You are not allowed to run this command.");
                return true;
            }

            GUtil.invitePlayer(target, 60);

            if (!(target == null))
            {
                if (GUtil.invitedPlayers.containsKey(target.getName()))
                {
                    if (GUtil.isGuildMember(target, GUtil.getGuild(target)))
                    {
                        player.sendMessage(ChatColor.RED + "That player is already in a guild.");
                        return true;
                    }

                    player.sendMessage(GUtil.color("&aSent an invitation to " + target.getName()));

                    if (args[0].equalsIgnoreCase("accept"))
                    {
                        List<String> players = plugin.guilds.getStringList("guilds." + guild + ".members");
                        players.add(target.getName());
                        GUtil.invitedPlayers.remove(target.getName());
                        target.sendMessage(ChatColor.GREEN + "You have successfully joined " + guild);
                    }

                    if (args[0].equalsIgnoreCase("deny"))
                    {
                        GUtil.invitedPlayers.remove(target.getName());
                        target.sendMessage(ChatColor.GREEN + "You have declined to join " + guild);
                    }
                }
                else
                {
                    sender.sendMessage(ChatColor.RED + "You haven't received any guild invitations.");
                }
            }
            else
            {
                sender.sendMessage(ChatColor.RED + "Player not found.");
            }
        }
        return false;
    }
}