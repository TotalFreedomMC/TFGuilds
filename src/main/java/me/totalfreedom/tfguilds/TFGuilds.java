package me.totalfreedom.tfguilds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.totalfreedom.tfguilds.command.*;
import me.totalfreedom.tfguilds.config.Config;
import me.totalfreedom.tfguilds.guild.Guild;
import me.totalfreedom.tfguilds.guild.User;
import me.totalfreedom.tfguilds.listener.ChatListener;
import me.totalfreedom.tfguilds.listener.JoinListener;
import me.totalfreedom.tfguilds.sql.SQLDatabase;
import org.bukkit.plugin.java.JavaPlugin;

public class TFGuilds extends JavaPlugin
{

    private static TFGuilds plugin;
    private Config config;
    private SQLDatabase sqlDatabase;
    private TFMBridge tfmBridge;
    private Map<String, SubCommand> subCommands = new HashMap<>();

    public static TFGuilds getPlugin()
    {
        return plugin;
    }

    @Override
    public void onEnable()
    {
        this.plugin = this;
        config = new Config("config.yml");
        sqlDatabase = new SQLDatabase(this);
        User.loadAll();
        Guild.loadAll();
        tfmBridge = new TFMBridge();
        tfmBridge.getTfm();
        new JoinListener(this);
        new ChatListener(this);
        loadSubCommands();
        getCommand("tfguilds").setExecutor(new TFGuildsCommand());
        getCommand("guild").setExecutor(new GuildCommand());
        getCommand("guildchatspy").setExecutor(new GuildChatSpyCommand());
    }

    @Override
    public void onDisable()
    {
        config.save();
        this.plugin = null;
    }

    public Config getConfig()
    {
        return config;
    }

    public SQLDatabase getSQL()
    {
        return sqlDatabase;
    }

    public TFMBridge getTfmBridge()
    {
        return tfmBridge;
    }

    public SubCommand getSubCommand(String name)
    {
        return subCommands.get(name);
    }

    public List<String> getSubCommands()
    {
        List<String> commands = new ArrayList<>(subCommands.keySet());
        Collections.sort(commands);
        return commands;
    }

    private void loadSubCommands()
    {
        subCommands.put("create", new CreateSubCommand());
        subCommands.put("info", new InfoSubCommand());
        subCommands.put("disband", new DisbandSubCommand());
        subCommands.put("invite", new InviteSubCommand());
        subCommands.put("join", new JoinSubCommand());
        subCommands.put("leave", new LeaveSubCommand());
        subCommands.put("warps", new WarpsSubCommand());
        subCommands.put("setwarp", new SetWarpSubCommand());
        subCommands.put("addmod", new AddModSubCommand());
        subCommands.put("removemod", new RemoveModSubCommand());
        subCommands.put("createrank", new CreateRankSubCommand());
        subCommands.put("deleterank", new DeleteRankSubCommand());
        subCommands.put("warp", new WarpSubCommand());
        subCommands.put("setrank", new SetRankSubCommand());
        subCommands.put("deletewarp", new DeleteWarpSubCommand());
        subCommands.put("kick", new KickSubCommand());
        subCommands.put("setowner", new SetOwnerSubCommand());
        subCommands.put("setstate", new SetStateSubCommand());
        subCommands.put("setdefaultrank", new SetDefaultRankSubCommand());
        subCommands.put("home", new HomeSubCommand());
        subCommands.put("tp", new TpSubCommand());
        subCommands.put("roster", new RosterSubCommand());
        subCommands.put("toggletag", new ToggleTagSubCommand());
        subCommands.put("chat", new ChatSubCommand());
        subCommands.put("motd", new MotdSubCommand());
        subCommands.put("toggletags", new ToggleTagsSubCommand());
        subCommands.put("tag", new TagSubCommand());
        subCommands.put("list", new ListSubCommand());
        subCommands.put("help", new HelpSubCommand());
    }
}
