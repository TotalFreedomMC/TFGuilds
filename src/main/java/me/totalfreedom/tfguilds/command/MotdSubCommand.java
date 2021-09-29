package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.guild.Guild;
import me.totalfreedom.tfguilds.util.GUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MotdSubCommand extends Common implements SubCommand
{

    @Override
    public void execute(CommandSender sender, Player playerSender, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(IN_GAME_ONLY);
            return;
        }

        if (args.length == 1)
        {
            sender.sendMessage(USAGE + "/g motd <set <message> | clear>");
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
            sender.sendMessage(PREFIX + "Only guild moderators can modify the guild's MOTD");
            return;
        }

        if (args.length >= 3)
        {
            if (args[1].equalsIgnoreCase("set"))
            {
                String message = StringUtils.join(args, " ", 2, args.length);
                guild.setMotd(message);
                sender.sendMessage(PREFIX + "Set the guild's MOTD to " + ChatColor.GOLD + GUtil.colorize(message));
            }
            else
            {
                sender.sendMessage(USAGE + "/g motd <set <message> | clear>");
            }
            return;
        }

        if (!args[1].equalsIgnoreCase("clear"))
        {
            sender.sendMessage(USAGE + "/g motd <set <message> | clear>");
            return;
        }

        guild.setMotd(null);
        sender.sendMessage(PREFIX + "Successfully cleared the guild's MOTD");
    }
}
