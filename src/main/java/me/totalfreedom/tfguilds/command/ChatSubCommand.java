package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.guild.Guild;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatSubCommand extends Common implements SubCommand
{

    @Override
    public void execute(CommandSender sender, Player playerSender, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(IN_GAME_ONLY);
            return;
        }

        Guild guild = Guild.getGuild(playerSender);
        if (guild == null)
        {
            sender.sendMessage(NOT_IN_GUILD);
            return;
        }

        if (args.length >= 2)
        {
            String message = StringUtils.join(args, " ", 1, args.length);
            guild.chat(playerSender, message);
            return;
        }

        if (GUILD_CHAT.contains(playerSender))
        {
            GUILD_CHAT.remove(playerSender);
            sender.sendMessage(PREFIX + "Guild chat " + ChatColor.GOLD + "disabled");
            return;
        }

        GUILD_CHAT.add(playerSender);
        sender.sendMessage(PREFIX + "Guild chat " + ChatColor.GOLD + "enabled");
    }
}
