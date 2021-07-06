package net.theEvilReaper.bot.api.command;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public enum TeamSpeakCommand {

    EDIT_CHANNEL("channeledit", "cid", "channel_name");

    private final String name;
    private final String[] subArgs;

    TeamSpeakCommand(String name, String... subArgs) {
        this.name = name;
        this.subArgs = subArgs;
    }

    public String getName() {
        return name;
    }

    public String[] getSubArgs() {
        return subArgs;
    }
}
