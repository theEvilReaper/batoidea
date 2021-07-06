package net.theEvilReaper.bot.service;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.theEvilReaper.bot.api.service.IService;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/

public class RanKService implements IService {

    private final Logger logger;
    private final BiMap<Integer, Integer> watchedGroup;
    private final Lock lock;

    private boolean active;

    public RanKService() {
        this.logger = Logger.getLogger("BotLogger");
        this.watchedGroup = HashBiMap.create();
        this.lock =new ReentrantLock();
    }

    public void updateRanks() {
        logger.log(Level.INFO, "Updating ranks");
        lock.lock();
        try {
            watchedGroup.clear();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void setReady(boolean ready) {

    }

    @Override
    public boolean isReady() {
        return false;
    }

    public void changeRank() {

    }

    public void checkRank() {

    }

    @Override
    public String getName() {
        return "RankService";
    }
}
