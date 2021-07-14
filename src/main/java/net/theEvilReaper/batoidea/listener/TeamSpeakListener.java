package net.theEvilReaper.batoideas.listener;

import com.github.manevolent.ts3j.api.TextMessageTargetMode;
import com.github.manevolent.ts3j.event.TS3Listener;
import com.github.manevolent.ts3j.event.TextMessageEvent;
import net.theEvilReaper.batoidea.Batoidea;
import net.theEvilReaper.batoidea.command.UserCommandProvider;
import net.theEvilReaper.batoideas.interaction.ClientInteraction;
import net.theEvilReaper.bot.api.interaction.InteractionType;

import java.util.logging.Level;
import java.util.regex.Pattern;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public class TeamSpeakListener implements TS3Listener {

    private static final Pattern SPLIT_PATTERN = Pattern.compile(" ");

    private final Batoidea batoidea;
    private final UserCommandProvider userCommandProvider;
    private final ClientInteraction clientInteraction;

    public TeamSpeakListener(Batoidea batoidea) {
        this.batoidea = batoidea;
        this.clientInteraction = batoidea.getInteractionFactory().getInteraction(InteractionType.CLIENT, ClientInteraction.class);
        this.userCommandProvider = new UserCommandProvider(batoidea);
    }

    @Override
    public void onTextMessage(TextMessageEvent event) {
        if (event.getInvokerId() == batoidea.getBotID())
            return; //Ignore our own sent messages

        if (event.getTargetMode() == TextMessageTargetMode.CHANNEL || event.getTargetMode() == TextMessageTargetMode.SERVER)
            return;

        var client = batoidea.getClientProvider().getClientById(event.getInvokerId());

        if (client == null) return;

        batoidea.getLogger().log(Level.INFO, "Received message " + event.getMessage() + " from: " + client.getNickname());
        if (event.getMessage().startsWith("!")) {
            var split = SPLIT_PATTERN.split(event.getMessage().replaceFirst("!", ""));
            String command = split[0];
            String[] args = new String[split.length - 1];
            if (split.length > 1) {
                System.arraycopy(split, 1, args, 0, split.length - 1);
            }
            userCommandProvider.dispatch(client, command, args);
        } else {
            batoidea.getLogger().log(Level.INFO, "Received message without the right syntax");
            clientInteraction.sendPrivateMessage(client, "I could not find the command: " + event.getMessage() + "Please type !help for help");
        }
    }
}