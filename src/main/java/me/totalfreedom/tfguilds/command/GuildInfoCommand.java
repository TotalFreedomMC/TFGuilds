package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.util.GBase;
import me.totalfreedom.tfguilds.util.GMessage;
import me.totalfreedom.tfguilds.util.GUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class GuildInfoCommand extends GBase implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length == 0)
        {
            if (GUtil.isConsole(sender))
            {
                sender.sendMessage(GUtil.color("&cProvide a guild name."));
                return true;
            }

            Player player = (Player) sender;
            String guild = GUtil.getGuild(player);

            if (guild == null)
            {
                player.sendMessage(GMessage.NOT_IN_GUILD);
                return true;
            }

            String owner = GUtil.getOwner(guild);
            String tag = GUtil.getTag(guild);
            String creation = GUtil.getTimeCreated(guild);
            List<String> members = GUtil.getMember(guild);

            player.sendMessage(GUtil.color("&2-=-=-=- &aGuild Information &2-=-=-=-"));
            player.sendMessage(GUtil.color("&2Guild Name: &a" + guild));
            player.sendMessage(GUtil.color("&2Guild Owner: &a" + owner));
            player.sendMessage(GUtil.color("&2Guild Tag: &a" + tag));
            player.sendMessage(GUtil.color("&2Guild Creation Date: &a" + creation));
            player.sendMessage(GUtil.color("&2Member Count: &a" + members.size()));
            player.sendMessage(GUtil.color("&2Members: &a" + members));
            player.sendMessage(GUtil.color("&2-=-=-=-=-=-=-=-=-=-=-=-=-=-=-"));
            return true;
        }

        String guild = GUtil.getGuild(args[0]);
        if (guild == null)
        {
            sender.sendMessage(GMessage.GUILD_NOT_FOUND);
            return true;
        }

        String owner = GUtil.getOwner(guild);
        String tag = GUtil.getTag(guild);
        String creation = GUtil.getTimeCreated(guild);
        List<String> members = GUtil.getMember(guild);

        sender.sendMessage(GUtil.color("&2-=-=-=- &aGuild Information &2-=-=-=-"));
        sender.sendMessage(GUtil.color("&2Guild Name: &a" + guild));
        sender.sendMessage(GUtil.color("&2Guild Owner: &a" + owner));
        sender.sendMessage(GUtil.color("&2Guild Tag: &a" + tag));
        sender.sendMessage(GUtil.color("&2Guild Creation Date: &a" + creation));
        sender.sendMessage(GUtil.color("&2Member Count: &a" + members.size()));
        sender.sendMessage(GUtil.color("&2Members: &a" + members));
        sender.sendMessage(GUtil.color("&2-=-=-=-=-=-=-=-=-=-=-=-=-=-=-"));
        return true;
    }
}