package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.guild.Guild;
import me.totalfreedom.tfguilds.guild.GuildState;
import me.totalfreedom.tfguilds.util.GUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class JoinSubcommand extends Common implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof ConsoleCommandSender)
        {
            sender.sendMessage(NO_PERMS);
            return true;
        }

        if (args.length < 2)
        {
            sender.sendMessage(tl(PREFIX + "Proper usage: /g join <guild>"));
            return true;
        }

        Player player = (Player)sender;
        if (Guild.getGuild(player) != null)
        {
            sender.sendMessage(ChatColor.RED + "You are already in a guild!");
            return true;
        }

        Guild guild = Guild.getGuild(GUtil.flatten(StringUtils.join(args, " ", 1, args.length)));
        if (guild == null)
        {
            sender.sendMessage(ChatColor.RED + "That guild doesn't exist!");
            return true;
        }

        if (guild.getState() == GuildState.CLOSED)
        {
            sender.sendMessage(ChatColor.RED + "That guild is currently closed!");
            return true;
        }

        if (guild.getState() == GuildState.INVITE_ONLY)
        {
            if (!INVITES.containsKey(player))
            {
                sender.sendMessage(ChatColor.RED + "You have not been invited to this guild!");
                return true;
            }
            INVITES.remove(player);
        }

        guild.addMember(player.getName());
        guild.broadcast(tl("%s%" + player.getName() + " %p%has joined your guild!"));
        guild.save();
        return true;
    }
}