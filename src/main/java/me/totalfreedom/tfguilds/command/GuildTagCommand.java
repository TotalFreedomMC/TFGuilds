package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.util.GBase;
import me.totalfreedom.tfguilds.util.GUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GuildTagCommand extends GBase implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length == 0 || args.length > 2)
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

        if (args.length == 2)
        {
            if (args[0].equalsIgnoreCase("set"))
            {
                if (!args[1].toLowerCase().contains(guild))
                {
                    sender.sendMessage(ChatColor.RED + "Your guild tag must contain your guild name.");
                    return true;
                }

                GUtil.setTag(GUtil.color(StringUtils.join(args, " ")), guild);
                sender.sendMessage(ChatColor.GREEN + "Guild tag set to \"" + GUtil.color(args[1]) + ChatColor.GREEN + "\"");
                return true;
            }
            return false;
        }

        if (!args[0].equalsIgnoreCase("clear"))
        {
            return false;
        }
        GUtil.setTag(GUtil.color("&8[&7" + guild + "&8]&r "), guild);
        sender.sendMessage(ChatColor.GRAY + "Removed your guild's tag.");
        return true;
    }
}