package totalfreedom.tfguilds.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import totalfreedom.tfguilds.util.GBase;
import totalfreedom.tfguilds.util.GUtil;

public class GuildCommand extends GBase implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (args.length != 2)
        {
            return false;
        }

        if (args[0].toLowerCase().equals("create"))
        {
            if (GUtil.isConsole(sender))
            {
                sender.sendMessage(ChatColor.RED + "You are not allowed to run this command.");
                return true;
            }

            GUtil.createGuild(sender, args[1]);
            sender.sendMessage(ChatColor.GREEN + "Successfully created a guild named " + args[1]);
            return true;
        }
        return false;
    }
}
