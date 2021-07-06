package net.theEvilReaper.bot.api.command;

import net.theEvilReaper.bot.api.interaction.Interaction;
import org.jetbrains.annotations.Nullable;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public abstract class Command implements CommandExecutor {

    private int minimumGroup;
    private String usage;

    public Command(Interaction... interaction) {
        this.minimumGroup = -1;
    }

    public void setMinimumGroup(int minimumGroup) {
        this.minimumGroup = minimumGroup;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public boolean isDefault() {
        return minimumGroup == -1;
    }

    public boolean hasMinimumGroup(int minimumGroup) {
        return minimumGroup >= this.minimumGroup;
    }

    @Nullable
    public String getUsage() {
        return usage;
    }
}
