package me.totalfreedom.tfguilds.command;

import java.util.List;
import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.guild.Guild;
import me.totalfreedom.tfguilds.util.PaginationList;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ListSubCommand extends Common implements SubCommand
{

    @Override
    public void execute(CommandSender sender, Player playerSender, String[] args)
    {
        if (args.length > 2)
        {
            sender.sendMessage(USAGE + "/g list [page]");
            return;
        }

        List<String> guilds = Guild.getGuildNames();
        if (guilds.isEmpty())
        {
            sender.sendMessage(PREFIX + "There are no guilds.");
            return;
        }

        PaginationList<String> pagination = new PaginationList<>(10);
        pagination.addAll(guilds);
        int index = 1;

        if (args.length == 2)
        {
            try
            {
                index = Integer.parseInt(args[1]);
            }
            catch (NumberFormatException ex)
            {
                sender.sendMessage(PREFIX + ChatColor.GOLD + args[1] + ChatColor.GRAY + " is not a valid number.");
                return;
            }
        }

        if (index < 1 || index > pagination.getPageCount())
        {
            sender.sendMessage(PREFIX + "Please pick a number between 1 and " + pagination.getPageCount());
            return;
        }

        List<String> page = pagination.getPage(index);
        sender.sendMessage(PREFIX + "Guild list (" + ChatColor.GOLD + index + "/" + pagination.getPageCount() + ChatColor.GRAY + ")");
        for (String guild : page)
        {
            sender.sendMessage(ChatColor.GRAY + " - " + ChatColor.GOLD + guild);
        }
    }
}
