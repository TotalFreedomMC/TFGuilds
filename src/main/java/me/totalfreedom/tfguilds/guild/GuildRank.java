package me.totalfreedom.tfguilds.guild;

import lombok.Getter;
import me.totalfreedom.tfguilds.TFGuilds;
import me.totalfreedom.tfguilds.util.GLog;

import java.util.List;

public class GuildRank
{
    private static TFGuilds plugin = TFGuilds.getPlugin();

    // owning guild's identifier
    private String iguild;

    // identifier
    @Getter
    private String identifier;

    // name
    @Getter
    private String name;

    // members of this rank
    @Getter
    private List<String> members;

    public GuildRank(String iguild, String identifier, String name, List<String> members)
    {
        this.identifier = identifier;
        this.iguild = iguild;
        this.name = name;
        this.members = members;
    }

    public void set()
    {
        plugin.guilds.set(iguild + ".ranks." + identifier + ".name", name);
        plugin.guilds.set(iguild + ".ranks." + identifier + ".members", members);
    }

    public void delete()
    {
        plugin.guilds.set(iguild + ".ranks." + identifier, null);
    }
}