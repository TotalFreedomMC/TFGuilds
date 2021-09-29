package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.guild.Guild;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RosterSubCommand extends Common implements SubCommand
{

    @Override
    public void execute(CommandSender sender, Player playerSender, String[] args)
    {
        Guild guild;
        if (args.length >= 2)
        {
            Player player = Bukkit.getPlayer(args[1]);
            if (player != null && !tfmBridge.isVanished(player))
            {
                guild = Guild.getGuild(player);
                if (guild == null)
                {
                    sender.sendMessage(PLAYER_NOT_IN_GUILD);
                    return;
                }

                sender.sendMessage(guild.getRoster());
                return;
            }

            guild = Guild.getGuild(StringUtils.join(args, " ", 1, args.length));
            if (guild == null)
            {
                sender.sendMessage(PREFIX + "That guild does not exist.");
                return;
            }

            sender.sendMessage(guild.getRoster());
            return;
        }

        if (!(sender instanceof Player))
        {
            sender.sendMessage(USAGE + "/g roster <name>");
            return;
        }

        guild = Guild.getGuild(playerSender);
        if (guild == null)
        {
            sender.sendMessage(NOT_IN_GUILD);
            return;
        }

        sender.sendMessage(guild.getRoster());
    }
}
