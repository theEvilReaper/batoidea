package net.theEvilReaper.bot.api.permission;

import org.jetbrains.annotations.NotNull;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/

public interface PermissionEntity {

    void addPermission(@NotNull String permission);

    void removePermission(@NotNull String permission);

    boolean hasPermission(@NotNull String permission);
}
