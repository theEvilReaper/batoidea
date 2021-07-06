package net.theEvilReaper.bot.service;

import com.github.manevolent.ts3j.api.Channel;
import net.theEvilReaper.bot.api.provider.IChannelProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

public class ChannelProvider implements IChannelProvider {

    @Override
    public Iterator<Channel> getChannels() {
        return null;
    }

    @Override
    public @Nullable Channel getChannelById(int channelId) {
        return null;
    }
}
