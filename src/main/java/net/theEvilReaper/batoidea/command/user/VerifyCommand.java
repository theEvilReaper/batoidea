package net.theEvilReaper.batoidea.command.user;

import com.github.manevolent.ts3j.api.Client;
import net.theEvilReaper.bot.api.command.Command;
import net.theEvilReaper.bot.api.command.CommandSender;
import net.theEvilReaper.bot.api.interaction.AbstractInteractionFactory;
import net.theEvilReaper.bot.api.user.IUserService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public class VerifyCommand extends Command {

    private final IUserService iUserService;

    public VerifyCommand(AbstractInteractionFactory factory, IUserService iUserService) {
        super(factory, "verify");
        this.iUserService = iUserService;
    }

//!verify theEvilReaper


    @Override
    public void apply(@NotNull CommandSender sender, @NotNull String command, @Nullable String... args) {
        if (args == null || args.length != 1) {
            sender.sendMessage("Wrong syntax. Use !verify <code>");
            return;
        }


       /* var tsUser = iUserService.getUser(executor.getId());

        if (tsUser != null && tsUser.isVerified()) {
            userInteraction.sendPrivateMessage(executor, "You are already verified");
            return;
        }

        var code = args[0];*/
    }
}
