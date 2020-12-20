package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.guild.Guild;
import me.totalfreedom.tfguilds.guild.GuildWarp;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class WarpSubcommand extends Common implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length < 2)
        {
            sender.sendMessage(tl(PREFIX + "Proper usage: /g warp <name>"));
            return true;
        }

        if (sender instanceof ConsoleCommandSender)
        {
            sender.sendMessage(NO_PERMS);
            return true;
        }

        Player player = (Player)sender;
        Guild guild = Guild.getGuild(player);
        if (guild == null)
        {
            sender.sendMessage(NG);
            return true;
        }

        String warpName = StringUtils.join(args, " ", 1, args.length);
        if (!Guild.warpExists(guild.getIdentifier(), warpName))
        {
            sender.sendMessage(ChatColor.RED + "Warp not found.");
            return true;
        }

        GuildWarp warp = plugin.warpData.get(guild.getIdentifier(), warpName);
        Location warpLoc = new Location(warp.getWorld(), warp.getX(), warp.getY(), warp.getZ());
        player.teleport(warpLoc);
        sender.sendMessage(tl(PREFIX + "Warping to \"" + warpName + "\"."));
        return true;
    }
}