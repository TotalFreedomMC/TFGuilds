package me.totalfreedom.tfguilds.user;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import me.totalfreedom.tfguilds.TFGuilds;

@Getter
public class User
{
    private final int id;
    private final UUID uuid;
    @Setter
    private boolean tag;

    public User(int id, UUID uuid, boolean tag)
    {
        this.id = id;
        this.uuid = uuid;
        this.tag = tag;
    }

    public void save()
    {
        TFGuilds.getPlugin().userData.save(this);
    }
}
