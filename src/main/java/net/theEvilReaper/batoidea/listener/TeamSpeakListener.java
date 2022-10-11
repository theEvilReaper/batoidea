package net.theevilreaper.batoidea.listener;

import com.github.manevolent.ts3j.event.TS3Listener;
import com.github.manevolent.ts3j.event.TextMessageEvent;
import net.theevilreaper.batoidea.Batoidea;
import net.theevilreaper.bot.api.command.CommandManager;
import net.theevilreaper.bot.api.command.CommandParser;
import net.theevilreaper.bot.api.interaction.InteractionType;
import net.theevilreaper.bot.api.interaction.UserInteraction;
import net.theevilreaper.bot.api.provider.IClientProvider;
import net.theevilreaper.bot.api.user.IUserService;

import java.util.logging.Logger;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public class TeamSpeakListener implements TS3Listener, CommandParser {

    private static final Logger logger = Logger.getLogger("BotLogger");

    private final int botID;
    private final CommandManager commandManager;
    private final IUserService userService;
    private final IClientProvider clientProvider;
    private final UserInteraction clientInteraction;

    public TeamSpeakListener(Batoidea batoidea, CommandManager commandManager, IUserService userService) {
        this.botID = batoidea.getBotID();
        this.commandManager = commandManager;
        this.clientProvider = batoidea.getClientProvider();
        this.userService = userService;
        this.clientInteraction = batoidea.getInteractionFactory().getInteraction(InteractionType.CLIENT, UserInteraction.class);
    }

    @Override
    public void onTextMessage(TextMessageEvent event) {
        if (event.getInvokerId() == botID) return; //Ignore our own sent messages
        //TODO: FIX that method
        //if (!Conditions.isPrivatChannel(event.getTargetMode())) return;

        var message = event.getMessage().trim();

        if (message.isEmpty()) return;

        var client = clientProvider.getClientById(event.getInvokerId());

        if (client == null) return;

        logger.info("Received message " + message + " from: " + client.getNickname());

        if (!event.getMessage().startsWith(commandManager.getCommandPrefix())) {
            clientInteraction.sendPrivateMessage(client, "I could not find the command: " + message + "Please type !help for help");
            return;
        }

        var user = userService.getUser(client.getDatabaseId());

        if (user == null) {
            logger.warning("A user missing an user object");
            clientInteraction.sendPrivateMessage(client, "An error occurred when executing command. Please report this. Code: (MISSING USER)");
            return;
        }

        this.parse(commandManager, user, message.replaceFirst(commandManager.getCommandPrefix(), ""));
    }
}