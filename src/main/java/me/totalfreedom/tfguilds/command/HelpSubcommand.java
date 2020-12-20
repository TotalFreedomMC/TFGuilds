package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.Common;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class HelpSubcommand extends Common implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        // Please keep the commands here & in GuildCommand.java in alphabetical order
        sender.sendMessage(tl("%s%[%p%TFGuilds%s%] %p%Command List"));
        sender.sendMessage(tl("%s% - %p%addmod <guild <player> | player>"));
        sender.sendMessage(tl("%s% - %p%chat [message]"));
        sender.sendMessage(tl("%s% - %p%createrank <name>"));
        sender.sendMessage(tl("%s% - %p%create <name>"));
        sender.sendMessage(tl("%s% - %p%deleterank <rank>"));
        sender.sendMessage(tl("%s% - %p%deletewarp <warp>"));
        sender.sendMessage(tl("%s% - %p%disband [name]"));
        sender.sendMessage(tl("%s% - %p%help"));
        sender.sendMessage(tl("%s% - %p%home [set]"));
        sender.sendMessage(tl("%s% - %p%info [guild | player]"));
        sender.sendMessage(tl("%s% - %p%invite <player>"));
        sender.sendMessage(tl("%s% - %p%join <guild>"));
        sender.sendMessage(tl("%s% - %p%kick <guild <player> | player>"));
        sender.sendMessage(tl("%s% - %p%leave"));
        sender.sendMessage(tl("%s% - %p%list [page]"));
        sender.sendMessage(tl("%s% - %p%motd <set <motd> | clear>"));
        sender.sendMessage(tl("%s% - %p%removemod <guild <name> | name>"));
        sender.sendMessage(tl("%s% - %p%rename <name>"));
        sender.sendMessage(tl("%s% - %p%roster [guild | player]"));
        sender.sendMessage(tl("%s% - %p%setdefaultrank <rank | none>"));
        sender.sendMessage(tl("%s% - %p%setmember <guild> <player>"));
        sender.sendMessage(tl("%s% - %p%setowner <guild <player> | player>"));
        sender.sendMessage(tl("%s% - %p%setrank <player> <rank | none>"));
        sender.sendMessage(tl("%s% - %p%setstate <open | invite | closed>"));
        sender.sendMessage(tl("%s% - %p%setwarp <name>"));
        sender.sendMessage(tl("%s% - %p%tag <set <tag> | clear [guild]>>"));
        sender.sendMessage(tl("%s% - %p%toggletags"));
        sender.sendMessage(tl("%s% - %p%toggletag [player]"));
        sender.sendMessage(tl("%s% - %p%tp <player>"));
        sender.sendMessage(tl("%s% - %p%warps"));
        sender.sendMessage(tl("%s% - %p%warp <name>"));
        return true;
    }
}