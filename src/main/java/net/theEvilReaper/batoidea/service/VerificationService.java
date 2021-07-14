package net.theEvilReaper.batoidea.service;

import net.theEvilReaper.bot.api.service.Service;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/

public class VerificationService extends Service {

    private final int verifyGroup;

    public VerificationService(int verifyGroup) {
        super("VerficiationService", -1);
        this.verifyGroup = verifyGroup;
    }

    @Override
    protected void update() {

    }

}
