package me.totalfreedom.tfguilds.command;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.guild.Guild;
import me.totalfreedom.tfguilds.util.GUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class GuildCommand extends Common implements CommandExecutor, TabCompleter
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length >= 1)
        {
            switch (args[0].toLowerCase())
            {
                case "list":
                    return new ListSubcommand().onCommand(sender, command, label, args);
                case "help":
                    return new HelpSubcommand().onCommand(sender, command, label, args);
                case "create":
                    return new CreateSubcommand().onCommand(sender, command, label, args);
                case "disband":
                    return new DisbandSubcommand().onCommand(sender, command, label, args);
                case "invite":
                    return new InviteSubcommand().onCommand(sender, command, label, args);
                case "addmod":
                    return new AddModSubcommand().onCommand(sender, command, label, args);
                case "removemod":
                    return new RemoveModSubcommand().onCommand(sender, command, label, args);
                case "setowner":
                    return new SetOwnerSubcommand().onCommand(sender, command, label, args);
                case "setstate":
                    return new SetStateSubcommand().onCommand(sender, command, label, args);
                case "kick":
                    return new KickSubcommand().onCommand(sender, command, label, args);
                case "leave":
                    return new LeaveSubcommand().onCommand(sender, command, label, args);
                case "tp":
                    return new TPSubcommand().onCommand(sender, command, label, args);
                case "info":
                    return new InfoSubcommand().onCommand(sender, command, label, args);
                case "tag":
                    return new TagSubcommand().onCommand(sender, command, label, args);
                case "chat":
                    return new ChatSubcommand().onCommand(sender, command, label, args);
                case "join":
                    return new JoinSubcommand().onCommand(sender, command, label, args);
                case "rename":
                    return new RenameSubcommand().onCommand(sender, command, label, args);
                case "createrank":
                    return new CreateRankSubcommand().onCommand(sender, command, label, args);
                case "deleterank":
                    return new DeleteRankSubcommand().onCommand(sender, command, label, args);
                case "setrank":
                    return new SetRankSubcommand().onCommand(sender, command, label, args);
                case "motd":
                    return new MOTDSubcommand().onCommand(sender, command, label, args);
                case "home":
                    return new HomeSubcommand().onCommand(sender, command, label, args);
                case "roster":
                    return new RosterSubcommand().onCommand(sender, command, label, args);
                case "setdefaultrank":
                    return new SetDefaultRankSubcommand().onCommand(sender, command, label, args);
                case "toggletags":
                    return new ToggleTagsSubcommand().onCommand(sender, command, label, args);
                case "toggletag":
                    return new ToggleTagSubcommand().onCommand(sender, command, label, args);
                case "setmember":
                    return new SetMemberSubcommand().onCommand(sender, command, label, args);
            }
            sender.sendMessage(tl(PREFIX + "Unknown command - Run /g help if you need help"));
            return true;
        }
        return new HelpSubcommand().onCommand(sender, command, label, args);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length == 1)
        {
            return Arrays.asList("addmod", "chat", "createrank", "create",
                    "deleterank", "disband", "help", "home", "info", "invite",
                    "join", "kick", "leave", "list", "motd", "removemod", "rename",
                    "roster", "setowner", "setrank", "setstate", "tag", "tp", "setdefaultrank",
                    "toggletags", "toggletag", "setmember");
        }
        else if (args.length == 2)
        {
            switch (args[0])
            {
                case "home":
                {
                    return Arrays.asList("set");
                }

                case "info":
                case "join":
                case "roster":
                case "setmember":
                {
                    return Guild.getGuildList();
                }

                case "toggletag":
                {
                    if (!plugin.bridge.isAdmin(sender))
                    {
                        return Collections.emptyList();
                    }

                    return GUtil.getPlayerList();
                }

                case "motd":
                case "tag":
                {
                    return Arrays.asList("set", "clear");
                }

                case "setstate":
                {
                    return Arrays.asList("OPEN", "INVITE", "CLOSED");
                }

                case "invite":
                {
                    return GUtil.getPlayerList();
                }

                case "deleterank":
                case "setdefaultrank":
                {
                    if (sender instanceof Player)
                    {
                        Player player = (Player) sender;
                        Guild guild = Guild.getGuild(player);
                        if (guild.getOwner().equals(player.getUniqueId()))
                        {
                            return guild.getRankNames();
                        }
                    }
                }

                case "tp":
                {
                    if (sender instanceof Player)
                    {
                        Player player = (Player) sender;
                        Guild guild = Guild.getGuild(player);
                        return guild.getOnlyMembers();
                    }
                }

                case "disband":
                {
                    if (!plugin.bridge.isAdmin(sender))
                    {
                        return Collections.emptyList();
                    }

                    return Guild.getGuildList();
                }

                case "kick":
                {
                    if (sender instanceof Player)
                    {
                        Player player = (Player) sender;
                        Guild guild = Guild.getGuild(player);
                        if (guild.hasModerator(player.getUniqueId()))
                        {
                            return guild.getOnlyMembers();
                        }
                    }
                }

                case "removemod":
                case "addmod":
                case "setowner":
                {
                    if (sender instanceof Player)
                    {
                        Player player = (Player) sender;
                        Guild guild = Guild.getGuild(player);
                        if (guild.getOwner().equals(player.getUniqueId()))
                        {
                            return guild.getOnlyMembers();
                        }
                    }
                }
            }
        }
        return Collections.emptyList();
    }
}