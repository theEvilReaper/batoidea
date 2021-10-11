package net.theEvilReaper.batoidea.interaction;

import com.github.manevolent.ts3j.protocol.socket.client.LocalTeamspeakClientSocket;
import net.theEvilReaper.bot.api.interaction.AbstractInteractionFactory;
import net.theEvilReaper.bot.api.interaction.Interaction;
import net.theEvilReaper.bot.api.interaction.InteractionType;
import org.jetbrains.annotations.NotNull;

/**
 * The {@link InteractionFactory} is the implementation of the {@link AbstractInteractionFactory}.
 * The factory handles the instance of each interaction class from the project. It also reduces the instances from the
 * given interactions to one per typ. The factory provides a method to get the right {@link Interaction} for a use case.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public class InteractionFactory extends AbstractInteractionFactory {

    private final Interaction[] interactions;

    /**
     * Creates all instances for the given implementations for the {@link Interaction}.
     * @param socket The socket from the {@link LocalTeamspeakClientSocket}
     */

    public InteractionFactory(LocalTeamspeakClientSocket socket) {
        this.interactions = new Interaction[InteractionType.getValues().length];
        this.interactions[0] = new ClientInteraction(socket);
        this.interactions[1] = new ServerGroupInteraction(socket);
        this.interactions[2] = new ServerInteraction(socket);
        this.interactions[3] = new ServerChannelInteraction(socket);
    }

    /**
     * Get a specific implementation of the {@link Interaction} from the factory.
     * Note that a wrong clazzType throws an {@link IllegalArgumentException}
     * @param type The type which implementation should be determined
     * @param clazzType The implementation class from the {@link Interaction}
     * @param <T> The value must extend from the {@link Interaction}. Other types are not allowed
     * @return The determined {@link Interaction} implementation.
     */

    @Override
    public <T extends Interaction> T getInteraction(@NotNull InteractionType type, Class<T> clazzType) {
        var interaction = interactions[type.getId()];

        if (interaction == null) {
            throw new IllegalArgumentException("Can't find a implementation for the given " + type.name());
        }

        var interfaces = interaction.getClass().getInterfaces();
        if (interfaces.length == 0 ) {
            throw new IllegalArgumentException("The fetched class and the given clazz are not similar");
        }

        Class<?> clazz = null;
        for (int i = 0; i < interfaces.length && clazz == null; i++) {
            var currentInterface = interfaces[i];
            if (currentInterface == clazzType) {
                clazz = currentInterface;
            }
        }

        if (clazz == null) {
            throw new IllegalArgumentException("The fetched class and the given clazz are not similar");
        }

        return clazzType.cast(interaction);
    }
}
