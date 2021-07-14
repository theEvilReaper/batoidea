package net.theEvilReaper.batoidea.command.console;

import net.theEvilReaper.bot.api.command.ConsoleCommand;
import net.theEvilReaper.bot.api.console.Console;
import net.theEvilReaper.bot.api.provider.IClientProvider;
import net.theEvilReaper.bot.api.user.IUserService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/

public class UserCommand extends ConsoleCommand {

    private final IUserService iUserService;
    private final IClientProvider iClientProvider;

    public UserCommand(IUserService iUserService, IClientProvider iClientProvider) {
        this.iUserService = iUserService;
        this.iClientProvider = iClientProvider;
    }

    @Override
    public void execute(@NotNull Console console, @NotNull String command, @Nullable String... args) {
        var users = iUserService.getUser();

        console.sendMessage("Clients.");

        try {
            iClientProvider.getClients().forEach((integer, client) -> console.sendMessage("" + client.getNickname()));
        }catch (Exception exception) {
            exception.printStackTrace();
        }

        console.sendMessage("TeamSpeakUser");

        users.forEach((integer, user) -> console.sendMessage(user.getClient().getNickname()));

    }
}
