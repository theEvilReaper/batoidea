package net.theEvilReaper.batoidea.listener;

import com.github.manevolent.ts3j.event.TS3Listener;
import com.github.manevolent.ts3j.event.TextMessageEvent;
import net.theEvilReaper.batoidea.Batoidea;
import net.theEvilReaper.batoidea.command.UserCommandProvider;
import net.theEvilReaper.batoidea.interaction.ClientInteraction;
import net.theEvilReaper.bot.api.interaction.InteractionType;
import net.theEvilReaper.bot.api.provider.IClientProvider;
import net.theEvilReaper.bot.api.util.Conditions;

import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public class TeamSpeakListener implements TS3Listener {

    private static final Logger logger = Logger.getLogger("BotLogger");

    private static final Pattern SPLIT_PATTERN = Pattern.compile(" ");
    private static final String COMMAND_SYMBOL = "!";

    private final int botID;
    private final UserCommandProvider userCommandProvider;
    private final IClientProvider clientProvider;
    private final ClientInteraction clientInteraction;

    public TeamSpeakListener(Batoidea batoidea, UserCommandProvider userCommandProvider) {
        this.botID = batoidea.getBotID();
        this.userCommandProvider = userCommandProvider;
        this.clientProvider = batoidea.getClientProvider();
        this.clientInteraction = batoidea.getInteractionFactory().getInteraction(InteractionType.CLIENT, ClientInteraction.class);
    }

    @Override
    public void onTextMessage(TextMessageEvent event) {
        if (event.getInvokerId() == botID) return; //Ignore our own sent messages
        if (!Conditions.isPrivatChannel(event.getTargetMode())) return;

        var client = clientProvider.getClientById(event.getInvokerId());

        if (client == null) return;

        var message = event.getMessage();

        logger.info("Received message " + message + " from: " + client.getNickname());

        if (event.getMessage().startsWith(COMMAND_SYMBOL)) {
            var split = SPLIT_PATTERN.split(message.replaceFirst(COMMAND_SYMBOL, ""));
            String command = split[0];
            String[] args = new String[split.length - 1];
            if (split.length > 1) {
                System.arraycopy(split, 1, args, 0, split.length - 1);
            }
            userCommandProvider.dispatch(client, command, args);
        } else {
            clientInteraction.sendPrivateMessage(client, "I could not find the command: " + message + "Please type !help for help");
        }
    }
}