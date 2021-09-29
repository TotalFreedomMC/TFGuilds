package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.guild.Guild;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetStateSubCommand extends Common implements SubCommand
{

    @Override
    public void execute(CommandSender sender, Player playerSender, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(IN_GAME_ONLY);
            return;
        }

        if (args.length != 2)
        {
            sender.sendMessage(USAGE + "/g setstate <state>");
            return;
        }

        Guild guild = Guild.getGuild(playerSender);
        if (guild == null)
        {
            sender.sendMessage(NOT_IN_GUILD);
            return;
        }

        if (!guild.isModerator(playerSender))
        {
            sender.sendMessage(PREFIX + "Only guild moderators can change the state.");
            return;
        }

        Guild.State state = Guild.State.fromString(args[1]);
        if (state == null)
        {
            sender.sendMessage(PREFIX + ChatColor.GOLD + args[1] + ChatColor.GRAY + " is not a valid state. Available options: "
                    + ChatColor.GOLD + StringUtils.join(Guild.State.values(), ", "));
            return;
        }

        guild.setState(state);
        sender.sendMessage(PREFIX + "Successfully set the guild state to " + ChatColor.GOLD + state.name());
        guild.broadcast(PREFIX + ChatColor.GOLD + sender.getName() + ChatColor.GRAY + " has set the guild state to " + ChatColor.GOLD + state.name());
    }
}
