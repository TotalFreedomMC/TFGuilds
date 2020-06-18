package totalfreedom.tfguilds.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import totalfreedom.tfguilds.util.GBase;
import totalfreedom.tfguilds.util.GUtil;

public class CreateGuildCommand extends GBase implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length == 0)
        {
            return false;
        }

        Player player = (Player) sender;

        if (GUtil.isConsole(player))
        {
            sender.sendMessage(ChatColor.RED + "You are not allowed to run this command.");
            return true;
        }

        ConfigurationSection guildMembers = plugin.guilds.getConfigurationSection("guilds");

        if (guildMembers != null)
        {
            try
            {
                for (String guild : guildMembers.getKeys(false))
                {
                    if (plugin.guilds.getString("guilds." + guild + ".members").contains(player.getName()))
                    {
                        player.sendMessage(ChatColor.RED + "You are already in a guild.");
                        return true;
                    }

                    if (guild.equals(args[0].toLowerCase()))
                    {
                        player.sendMessage(ChatColor.RED + "A guild with that name already exists.");
                        return true;
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }


        for (String blacklisted : GUtil.BLACKLISTED_NAMES_AND_TAGS)
        {
            if (args[0].toLowerCase().contains(blacklisted))
            {
                sender.sendMessage(ChatColor.RED + "You may not use that name.");
                return true;
            }
        }

        GUtil.createGuild(sender, args[0]);
        sender.sendMessage(ChatColor.GREEN + "Successfully created a guild named " + args[0]);
        return true;
    }
}
