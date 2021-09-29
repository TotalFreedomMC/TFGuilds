package me.totalfreedom.tfguilds.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface SubCommand
{

    void execute(CommandSender sender, Player playerSender, String[] args);
}
