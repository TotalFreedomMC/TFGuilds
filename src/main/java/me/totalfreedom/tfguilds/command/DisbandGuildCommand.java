package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.util.GLog;
import me.totalfreedom.tfguilds.util.GUtil;
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
        Player player = (Player) sender;
        String guild = GUtil.getGuild(player);

        if (args.length == 0)
        {
            if (GUtil.isConsole(player))
            {
                sender.sendMessage(ChatColor.RED + "You are not allowed to run this command.");
                return true;
            }

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

            sender.sendMessage(ChatColor.RED + "Are you sure you want to delete your guild? Type 'CONFIRM' to continue.");
            return true;
        }

        if (args[0].toLowerCase().equalsIgnoreCase("confirm"))
        {
            GUtil.deleteGuild(player);
            sender.sendMessage(ChatColor.GREEN + "Successfully deleted and cleared data for " + guild + ".");
            GLog.info(player.getName() + " deleted guild " + guild);
            return true;
        }
        return false;
    }
}