package me.totalfreedom.tfguilds.guild;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import me.totalfreedom.tfguilds.TFGuilds;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class GuildRank
{
    private static final TFGuilds plugin = TFGuilds.getPlugin();

    @Getter
    private String iguild;

    // identifier
    @Getter
    private final String identifier;

    // name
    @Getter
    private final String name;

    // members of this rank
    @Getter
    private final List<UUID> members;

    public GuildRank(String iguild, String identifier, String name, List<UUID> members)
    {
        this.iguild = iguild;
        this.identifier = identifier;
        this.name = name;
        this.members = members;
    }

    public List<String> getMemberNames()
    {
        List<String> only = new ArrayList<>();
        for (UUID member : members)
        {
            OfflinePlayer player = Bukkit.getOfflinePlayer(member);
            only.add(player.getName());
        }
        return only;
    }

    public static GuildRank createGuildRank(String guildIdentifier, String identifier, String name)
    {
        return plugin.rankData.create(guildIdentifier, identifier, name);
    }

    public void save()
    {
        plugin.rankData.save(this);
    }

    public void updateGuildIdentifier(String newIdentifier)
    {
        plugin.rankData.updateGuildIdentifier(this, newIdentifier);
        this.iguild = newIdentifier;
    }

    public void delete()
    {
        plugin.rankData.delete(this);
    }
}