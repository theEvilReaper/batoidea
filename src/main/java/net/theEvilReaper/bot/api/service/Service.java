package net.theEvilReaper.bot.api.service;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public abstract class Service implements Runnable, IService {

    private final String name;
    private final long interval;

    protected final Object lock;

    private Logger logger;
    private boolean ready;

    protected boolean running;
    protected boolean stopping;

    public Service(String name, long interval) {
        this.name = name;
        this.interval = interval;
        this.lock = new Object();
    }

    protected void init() {}

    protected abstract void update();

    protected void onStop() {}

    public void stopService() {
        synchronized (lock) {
            stopping = true;
            lock.notifyAll();
        }
    }

    @Override
    public void run() {
        if (running)
            return;

        running = true;

        init();

        while (!stopping && !Thread.interrupted()) {
            long now = System.currentTimeMillis();

            try {
                update();
            }catch(Exception | NoSuchMethodError e) {
                logger.log(Level.SEVERE, "An error occurred whilst calling the update function, terminating service...", e);
                break;
            }

            if (stopping || Thread.interrupted()) {
                break;
            }

            synchronized (lock) {
                try {
                    lock.wait(Math.max(10, interval - (System.currentTimeMillis() - now)));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        onStop();

        running = false;
        stopping = false;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void setReady(boolean ready) {
        this.ready = ready;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Service service = (Service) o;
        return Objects.equals(name, service.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean isReady() {
        return ready;
    }

    @Override
    public String getName() {
        return name;
    }
}