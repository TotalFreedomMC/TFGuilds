package me.totalfreedom.tfguilds.command;

import java.util.List;
import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.guild.Guild;
import me.totalfreedom.tfguilds.util.PaginationList;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ListSubcommand extends Common implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length > 2)
        {
            sender.sendMessage(tl(PREFIX + "Proper usage: /g list [page]"));
            return true;
        }

        List<String> guilds = Guild.getGuildList();
        PaginationList<String> paged = new PaginationList<>(10);

        if (guilds.isEmpty())
        {
            sender.sendMessage(ChatColor.RED + "Nobody has made a guild yet.");
            return true;
        }

        paged.addAll(guilds);

        int pageIndex = 1;

        if (args.length >= 2)
        {
            try
            {
                pageIndex = Integer.parseInt(args[1]);
            }
            catch (NumberFormatException e)
            {
                sender.sendMessage(ChatColor.RED + "Invalid number");
            }
        }

        if (pageIndex < 1 || pageIndex > paged.getPageCount())
        {
            sender.sendMessage(ChatColor.RED + "Not a valid page number");
            return true;
        }

        paged.getPage(pageIndex);
        List<String> page = paged.getPage(pageIndex);

        sender.sendMessage(tl(PREFIX + "%s%Guild List (%p%" + guilds.size() + " total%s%) [%p%Page " + pageIndex + "%s%/%p%" + paged.getPageCount() + "%s%]"));

        for (String guild : page)
        {
            sender.sendMessage(tl("%s%- %p%" + guild));
        }
        return true;
    }
}