package net.theEvilReaper.batoidea.provider;

import com.github.manevolent.ts3j.api.Channel;
import net.theEvilReaper.bot.api.provider.IChannelProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ChannelProvider implements IChannelProvider {

    private final Map<Integer, Channel> channels;
    private final Lock lock;

    public ChannelProvider() {
        this.channels = new HashMap<>();
        this.lock = new ReentrantLock();
    }

    @Override
    public void add(@NotNull Channel channel) {
        this.channels.putIfAbsent(channel.getId(), channel);
    }

    @Override
    public void removeChannel(int channelID) {
        try {
            lock.lock();
            this.channels.remove(channelID);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Channel recognizeChannel(@NotNull Channel channel) {
        try {
            lock.lock();
            var teamSpeakChannel = this.channels.get(channel.getId());

            if (teamSpeakChannel == null) {
                channels.put(channel.getId(), channel);
            }

            return teamSpeakChannel;
        } finally {
             lock.unlock();
        }
    }

    @Override
    public @Nullable Channel getChannelById(int channelId) {
        try {
            lock.lock();
            return channels.get(channelId);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Map<Integer, Channel> getChannels() {
        return Collections.unmodifiableMap(channels);
    }
}
