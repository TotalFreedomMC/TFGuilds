package me.totalfreedom.tfguilds.guild;

import lombok.Getter;

public enum GuildState
{
    OPEN("Open"),
    INVITE_ONLY("Invite-only"),
    CLOSED("Closed");

    @Getter
    private final String display;

    GuildState(String display)
    {
        this.display = display;
    }

    public static GuildState findState(String string)
    {
        if (string == null)
            return null;
        switch (string.toLowerCase())
        {
            case "open":
            case "opened":
                return OPEN;
            case "invite":
            case "inviteonly":
            case "invite_only":
                return INVITE_ONLY;
            case "closed":
            case "close":
                return CLOSED;
        }
        return null;
    }
}