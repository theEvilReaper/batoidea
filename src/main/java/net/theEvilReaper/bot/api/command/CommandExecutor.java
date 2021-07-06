package net.theEvilReaper.bot.api.command;

import com.github.manevolent.ts3j.api.Client;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Logger;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public interface CommandExecutor {

    void onCommand(@NotNull Client executor, @NotNull String command, @Nullable String... args);
}
