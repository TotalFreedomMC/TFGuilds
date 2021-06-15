package me.totalfreedom.tfguilds.command;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.TFGuilds;
import me.totalfreedom.tfguilds.guild.Guild;
import me.totalfreedom.tfguilds.util.GUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class GuildCommand extends Common implements CommandExecutor, TabCompleter
{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        Player playerSender = null;
        if (sender instanceof Player)
        {
            playerSender = (Player)sender;
        }

        if (args.length >= 1)
        {
            String name = args[0].toLowerCase();
            SubCommand command = TFGuilds.getPlugin().getSubCommand(name);
            if (command != null)
            {
                command.execute(sender, playerSender, args);
            }
            else
            {
                sender.sendMessage(PREFIX + "Unknown subcommand, do " + ChatColor.GOLD + "/g help" + ChatColor.GRAY + " for help.");
            }
            return true;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args)
    {
        Guild guild = null;
        if (sender instanceof Player)
        {
            guild = Guild.getGuild((Player)sender);
        }
        if (args.length == 1)
        {
            return TFGuilds.getPlugin().getSubCommands();
        }
        else if (args.length == 2)
        {
            switch (args[0])
            {
                case "home":
                {
                    return Collections.singletonList("set");
                }

                case "info":
                case "join":
                case "roster":
                {
                    return Guild.getGuildNames();
                }

                case "toggletag":
                {
                    if (!tfmBridge.isAdmin(sender))
                    {
                        return Collections.emptyList();
                    }

                    return GUtil.getPlayerNames();
                }

                case "motd":
                case "tag":
                {
                    return Arrays.asList("set", "clear");
                }

                case "setstate":
                {
                    return Arrays.asList("OPEN", "CLOSED", "INVITE_ONLY");
                }

                case "invite":
                {
                    return GUtil.getPlayerNames();
                }

                case "deleterank":
                case "setdefaultrank":
                {
                    if (guild != null && guild.getOwner().equals(((Player)sender).getUniqueId()))
                    {
                        return guild.getRankNames();
                    }
                }

                case "tp":
                {
                    return guild != null ? guild.getMemberNames() : Collections.emptyList();
                }

                case "disband":
                {
                    if (!tfmBridge.isAdmin(sender))
                    {
                        return Collections.emptyList();
                    }

                    return Guild.getGuildNames();
                }

                case "kick":
                {
                    if (guild != null && guild.isModerator((Player)sender))
                    {
                        return guild.getMemberOnlyNames();
                    }
                }

                case "removemod":
                case "addmod":
                case "setowner":
                {
                    if (guild != null && guild.getOwner().equals(((Player)sender).getUniqueId()))
                    {
                        return guild.getMemberNames();
                    }
                }

                default:
                {
                    return Collections.emptyList();
                }
            }
        }
        return Collections.emptyList();
    }
}
