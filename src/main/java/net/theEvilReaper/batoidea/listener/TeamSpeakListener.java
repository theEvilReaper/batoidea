package net.theevilreaper.batoidea.listener;

import com.github.manevolent.ts3j.event.TS3Listener;
import com.github.manevolent.ts3j.event.TextMessageEvent;
import net.theevilreaper.batoidea.Batoidea;
import net.theevilreaper.batoidea.user.TeamSpeakUser;
import net.theevilreaper.bot.api.command.CommandManager;
import net.theevilreaper.bot.api.command.CommandParser;
import net.theevilreaper.bot.api.interaction.InteractionType;
import net.theevilreaper.bot.api.interaction.UserInteraction;
import net.theevilreaper.bot.api.provider.IClientProvider;
import net.theevilreaper.bot.api.user.IUserService;
import net.theevilreaper.bot.api.util.Conditions;
import org.jetbrains.annotations.NotNull;
import org.tinylog.Logger;


/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
public class TeamSpeakListener implements TS3Listener, CommandParser {

    private final int botID;
    private final CommandManager commandManager;
    private final IUserService<TeamSpeakUser> userService;
    private final IClientProvider clientProvider;
    private final UserInteraction clientInteraction;

    public TeamSpeakListener(@NotNull Batoidea batoidea, @NotNull CommandManager commandManager, @NotNull IUserService<TeamSpeakUser> userService) {
        this.botID = batoidea.getBotID();
        this.commandManager = commandManager;
        this.clientProvider = batoidea.getClientProvider();
        this.userService = userService;
        this.clientInteraction = batoidea.getInteractionFactory().getInteraction(InteractionType.CLIENT, UserInteraction.class);
    }

    @Override
    public void onTextMessage(@NotNull TextMessageEvent event) {
        if (event.getInvokerId() == botID) return; //Ignore our own sent messages
        if (!Conditions.hasPrivateChannel(event.getTargetMode())) return;

        var message = event.getMessage().trim();

        if (message.isEmpty()) return;

        var client = clientProvider.getClientById(event.getInvokerId());

        if (client == null) return;

        Logger.info("Received message {} from: {}", message, client.getNickname());

        if (!event.getMessage().startsWith(commandManager.getCommandPrefix())) {
            clientInteraction.sendPrivateMessage(client, "I could not find the command: " + message + "Please type !help for help");
            return;
        }

        var user = userService.getUser(client.getDatabaseId());

        if (user == null) {
            Logger.warn("A user missing an user object");
            clientInteraction.sendPrivateMessage(client, "An error occurred when executing command. Please report this. Code: (MISSING USER)");
            return;
        }

        this.parse(commandManager, user, message.replaceFirst(commandManager.getCommandPrefix(), ""));
    }
}