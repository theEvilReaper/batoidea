package net.theEvilReaper.bot.impl.command;

import com.github.manevolent.ts3j.api.Client;
import com.github.manevolent.ts3j.command.CommandException;
import net.theEvilReaper.bot.api.command.Command;
import net.theEvilReaper.bot.api.interaction.Interaction;
import net.theEvilReaper.bot.api.interaction.UserInteraction;
import net.theEvilReaper.bot.impl.Batoidea;
import net.theEvilReaper.bot.service.FollowService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/

public class FollowCommand extends Command {

    private final UserInteraction userInteraction;
    private final Batoidea batoidea;
    private final FollowService followService;

    public FollowCommand(Batoidea batoidea, FollowService followService, Interaction... interactions) {
        this.batoidea = batoidea;
        this.followService = followService;
        this.userInteraction = (UserInteraction) interactions[0];
    }

    @Override
    public void onCommand(@NotNull Client executor, @NotNull String command, @Nullable String... args) {
        if (!followService.isFollowing()) {
            if (executor.getNickname().contains(args[0])) {

                var grups = executor.getServerGroups();

                for (int i = 0; i < grups.length; i++) {
                    System.out.println("Group: " + grups[i]);
                }

                var name = getName(executor);
                this.followService.setFollowing(name);
                batoidea.getBotInteraction().joinChannel(executor.getChannelId());
            } else {
                List<Client> clientList = new ArrayList<>();
                try {
                    for (Client listClient : batoidea.getTeamspeakClient().listClients()) {
                        if (listClient.getChannelId() == executor.getChannelId()) {
                            clientList.add(listClient);
                        }
                    }

                } catch (IOException | TimeoutException | InterruptedException | CommandException exception) {
                    exception.printStackTrace();
                }

                for (int i = 0; i < clientList.size(); i++) {
                    System.out.println(clientList.get(i).getNickname());
                }

                if (clientList.isEmpty()) {
                    userInteraction.sendPrivateMessage(executor, "No user found to " + args[0]);
                    return;
                }

                Client client = null;

                for (int i = 0; i < clientList.size() && client == null; i++) {
                    if (!clientList.get(i).getNickname().contains(args[0])) continue;
                    client = clientList.get(i);
                }

                if (client == null) {
                    userInteraction.sendPrivateMessage(executor, "No user found to " + args[0]);
                    return;
                }
                var name = getName(client);
                this.followService.setFollowing(name);
                batoidea.getBotInteraction().joinChannel(executor.getChannelId());
                var grups = executor.getServerGroups();

                for (int i = 0; i < grups.length; i++) {
                    System.out.println("Group: " + grups[i]);
                }
            }
        } else {
            userInteraction.sendPrivateMessage(executor, "I am currently following someone");
        }
    }

    private String getName(Client client) {
        var name = client.getClientURI().split("~")[1];

        if (name.contains("+")) {
            name = name.split("\\+")[0];
        }

        return name;
    }
}
