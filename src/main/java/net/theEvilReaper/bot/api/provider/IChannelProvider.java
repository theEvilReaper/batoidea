package net.theEvilReaper.bot.api.provider;

import com.github.manevolent.ts3j.api.Channel;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/

public interface IChannelProvider {

    Iterator<Channel> getChannels();

    @Nullable
    Channel getChannelById(int channelId);
}
