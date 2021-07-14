package net.theEvilReaper.batoidea.impl.interaction;

import com.github.manevolent.ts3j.protocol.socket.client.LocalTeamspeakClientSocket;
import net.theEvilReaper.bot.api.interaction.AbstractInteractionFactory;
import net.theEvilReaper.bot.api.interaction.Interaction;
import net.theEvilReaper.bot.api.interaction.InteractionType;
import org.jetbrains.annotations.NotNull;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public class InteractionFactory extends AbstractInteractionFactory {

    private final Interaction[] interactions;

    public InteractionFactory(LocalTeamspeakClientSocket socket) {
        this.interactions = new Interaction[InteractionType.getValues().length];
        this.interactions[0] = new ClientInteraction(socket);
        this.interactions[1] = new GroupInteraction(socket);
        this.interactions[2] = new ServerInteraction(socket);
    }

    @Override
    public <T extends Interaction> T getInteraction(@NotNull InteractionType type, Class<T> clazzType) {
        var interaction = interactions[type.getId()];
        /*
        if (!interaction.getClass().getSimpleName().equals(clazzType.getSimpleName())) {
            throw new IllegalArgumentException("The given type and the clazz are not similar");
        }*/

        return clazzType.cast(interaction);
    }
}
