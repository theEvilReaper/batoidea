package net.theEvilReaper.batoidea.command.console;

import net.theEvilReaper.bot.api.command.Command;
import net.theEvilReaper.bot.api.command.CommandSender;
import net.theEvilReaper.bot.api.provider.IClientProvider;
import net.theEvilReaper.bot.api.user.IUserService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/

public class UserCommand extends Command {

    private final IUserService iUserService;
    private final IClientProvider iClientProvider;

    public UserCommand(IUserService iUserService, IClientProvider iClientProvider) {
        super("user");
        this.iUserService = iUserService;
        this.iClientProvider = iClientProvider;
    }

    @Override
    public void apply(@NotNull CommandSender sender, @NotNull String command, @Nullable String... args) {
        var users = iUserService.getUser();

        sender.sendMessage("Clients.");

        try {
            iClientProvider.getClients().forEach((integer, client) -> sender.sendMessage("" + client.getNickname()));
        }catch (Exception exception) {
            exception.printStackTrace();
        }

        sender.sendMessage("TeamSpeakUser");

        users.forEach((integer, user) -> sender.sendMessage(user.getClient().getNickname()));
    }
}
