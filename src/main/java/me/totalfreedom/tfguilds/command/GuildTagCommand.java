package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.util.GBase;
import me.totalfreedom.tfguilds.util.GMessage;
import me.totalfreedom.tfguilds.util.GUtil;
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

        if (args.length == 2)
        {
            if (args[0].equalsIgnoreCase("set"))
            {
                if (!args[1].toLowerCase().contains(guild))
                {
                    player.sendMessage(ChatColor.RED + "Your guild tag must contain your guild name.");
                    return true;
                }

                GUtil.setTag(GUtil.color(args[1]) + " ", guild);
                player.sendMessage(ChatColor.GREEN + "Guild tag set to \"" + GUtil.color(args[1]) + ChatColor.GREEN + "\"");
                return true;
            }
            return false;
        }

        if (!args[0].equalsIgnoreCase("clear"))
        {
            return false;
        }
        GUtil.setTag(GUtil.color("&8[&7" + guild + "&8]&r "), guild);
        player.sendMessage(ChatColor.GRAY + "Removed your guild's tag.");
        return true;
    }
}