package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.Common;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class GuildCommand extends Common implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length >= 1)
        {
            switch (args[0].toLowerCase())
            {
                case "list": return new ListSubcommand().onCommand(sender, command, label, args);
                case "help": return new HelpSubcommand().onCommand(sender, command, label, args);
                case "create": return new CreateSubcommand().onCommand(sender, command, label, args);
                case "disband": return new DisbandSubcommand().onCommand(sender, command, label, args);
                case "invite": return new InviteSubcommand().onCommand(sender, command, label, args);
                case "addmod": return new AddModSubcommand().onCommand(sender, command, label, args);
                case "removemod": return new RemoveModSubcommand().onCommand(sender, command, label, args);
                case "setowner": return new SetOwnerSubcommand().onCommand(sender, command, label, args);
                case "setstate": return new SetStateSubcommand().onCommand(sender, command, label, args);
                case "kick": return new KickSubcommand().onCommand(sender, command, label, args);
                case "leave": return new LeaveSubcommand().onCommand(sender, command, label, args);
                case "tp": return new TPSubcommand().onCommand(sender, command, label, args);
                case "info": return new InfoSubcommand().onCommand(sender, command, label, args);
                case "tag": return new TagSubcommand().onCommand(sender, command, label, args);
                case "chat": return new ChatSubcommand().onCommand(sender, command, label, args);
                case "join": return new JoinSubcommand().onCommand(sender, command, label, args);
                case "rename": return new RenameSubcommand().onCommand(sender, command, label, args);
                case "createrank": return new CreateRankSubcommand().onCommand(sender, command, label, args);
                case "deleterank": return new DeleteRankSubcommand().onCommand(sender, command, label, args);
                case "setrank": return new SetRankSubcommand().onCommand(sender, command, label, args);
                case "motd": return new MOTDSubcommand().onCommand(sender, command, label, args);
                case "home": return new HomeSubcommand().onCommand(sender, command, label, args);
                case "roster": return new RosterSubcommand().onCommand(sender, command, label, args);
            }
            return false;
        }
        return new HelpSubcommand().onCommand(sender, command, label, args);
    }
}