package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.util.GLog;
import me.totalfreedom.tfguilds.util.GMessage;
import me.totalfreedom.tfguilds.util.GUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DisbandGuildCommand implements CommandExecutor
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
        if (!owner.equalsIgnoreCase(player.getName()))
        {
            player.sendMessage(GMessage.NOT_OWNER);
            return true;
        }

        if (args.length == 0)
        {
            player.sendMessage(ChatColor.RED + "Are you sure you want to delete your guild? Type '/disbandguild CONFIRM' or '/deleteguild CONFIRM' to continue.");
            return true;
        }

        if (args[0].equalsIgnoreCase("confirm"))
        {
            GUtil.deleteGuild(player, guild);
            Bukkit.broadcastMessage(GUtil.color("&c&l" + guild + " &chas been disbanded"));
            player.sendMessage(ChatColor.GREEN + "Successfully deleted and cleared data for " + guild + ".");
            GLog.info(player.getName() + " deleted guild " + guild);
            return true;
        }
        return false;
    }
}