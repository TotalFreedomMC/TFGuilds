package totalfreedom.tfguilds.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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

        // this stupid shit

        /* List<String> members = plugin.guilds.getStringList("guilds." + args[1] + ".members");
        for (String players : members)
        {
            if (players.contains(player.getName()))
            {
             player.sendMessage(ChatColor.RED + "You are already in a guild.");
             return true;
            }
        } */

        GUtil.createGuild(sender, args[1]);
        sender.sendMessage(ChatColor.GREEN + "Successfully created a guild named " + args[1]);
        return true;
    }
}
