package me.totalfreedom.tfguilds.command;

import me.totalfreedom.tfguilds.Common;
import me.totalfreedom.tfguilds.config.ConfigEntry;
import me.totalfreedom.tfguilds.guild.Guild;
import me.totalfreedom.tfguilds.util.GUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TagSubCommand extends Common implements SubCommand
{

    @Override
    public void execute(CommandSender sender, Player playerSender, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(IN_GAME_ONLY);
            return;
        }

        if (args.length < 2)
        {
            sender.sendMessage(USAGE + "/g tag <set <tag> | clear [guild]>");
            return;
        }

        Guild guild = Guild.getGuild(playerSender);
        if (guild == null)
        {
            sender.sendMessage(NOT_IN_GUILD);
            return;
        }

        if (!guild.getOwner().equals(playerSender.getUniqueId()))
        {
            sender.sendMessage(PREFIX + "Only the guild owner can edit the guild's tag.");
            return;
        }

        if (!guild.canUseTag())
        {
            sender.sendMessage(PREFIX + "You may not edit or clear your guild tag, please contact an admin if this is an error.");
            return;
        }

        if (args.length >= 3)
        {
            if (args[1].equalsIgnoreCase("set"))
            {
                int length = ConfigEntry.GUILD_TAG_MAX_LENGTH.getInteger();
                String tag = StringUtils.join(args, " ", 2, args.length);
                tag = tag.replace("%tag%", guild.getName());
                if (GUtil.removeColorCodes(tag).length() > length && length > 0)
                {
                    sender.sendMessage(PREFIX + "The guild tag cannot go over " + length + " characters limit.");
                    return;
                }

                if (GUtil.containsBlacklistedWord(tag) && tfmBridge.isAdmin(sender))
                {
                    sender.sendMessage(PREFIX + "The guild tag contains forbidden word(s).");
                    return;
                }

                guild.setTag(tag);
                sender.sendMessage(PREFIX + "The guild tag has been changed to " + ChatColor.GOLD + GUtil.colorize(tag).replace("%rank%", "Guild Owner"));
            }
            else if (args[1].equalsIgnoreCase("clear"))
            {
                if (!tfmBridge.isAdmin(sender))
                {
                    sender.sendMessage(PREFIX + "You do not have the permission to use this command.");
                    return;
                }

                guild = Guild.getGuild(StringUtils.join(args, " ", 2, args.length));
                if (guild == null)
                {
                    sender.sendMessage(PREFIX + "That guild does not exist.");
                    return;
                }

                guild.setTag(null);
                sender.sendMessage(PREFIX + "Successfully cleared the guild tag for " + ChatColor.GOLD + guild.getName());
                return;
            }
            else
            {
                sender.sendMessage(USAGE + "/g tag <set <tag> | clear [guild]>");
            }
            return;
        }

        if (args[1].equalsIgnoreCase("clear"))
        {
            guild.setTag(null);
            sender.sendMessage(PREFIX + "Successfully cleared the guild tag.");
        }
        else
        {
            sender.sendMessage(USAGE + "/g tag <set <tag> | clear [guild]>");
        }
    }
}
