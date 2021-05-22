package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.guild.Guild;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomeSubCommand extends Common implements SubCommand
{

    @Override
    public void execute(CommandSender sender, Player playerSender, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(IN_GAME_ONLY);
            return;
        }

        if (args.length > 2)
        {
            sender.sendMessage(USAGE + "/g home [set]");
            return;
        }

        Guild guild = Guild.getGuild(playerSender);
        if (guild == null)
        {
            sender.sendMessage(NOT_IN_GUILD);
            return;
        }

        if (args.length == 2)
        {
            if (args[1].equalsIgnoreCase("set"))
            {
                if (!guild.isModerator(playerSender))
                {
                    sender.sendMessage(PREFIX + "Only guild moderators can set guild's home location");
                    return;
                }

                guild.setHome(playerSender.getLocation());
                sender.sendMessage(PREFIX + "Successfully set guild's home to your location");
            }
            else
            {
                sender.sendMessage(USAGE + "/g home [set]");
            }
            return;
        }

        playerSender.teleport(guild.getHome());
        sender.sendMessage(PREFIX + "Teleported to your guild's home!");
    }
}
