package net.theEvilReaper.batoidea.identity;

import com.github.manevolent.ts3j.identity.LocalIdentity;
import net.theEvilReaper.bot.api.identity.Identity;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.logging.Logger;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public class BatoideaIdentity implements Identity {

    private static final Path path = Paths.get("src/main/resources/", "identity.txt");

    private LocalIdentity localIdentity;

    public BatoideaIdentity(Logger logger, int securityLevel) {
        if (!Files.exists(path)) {
            logger.info("Can't find identity file. Creating new one");

            try {
                LocalIdentity newIdentity = LocalIdentity.generateNew(securityLevel);
                newIdentity.save(Files.newOutputStream(path));
                localIdentity = newIdentity;
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            } catch (IOException exception) {
                logger.warning("FATAL: Can not write identity file...");
                System.exit(-1);
            }
        } else {
            logger.info("Loading identity file");
            try {
              this.localIdentity = LocalIdentity.read(path.toFile());
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    @Override
    public void setLocalIdentity(@NotNull LocalIdentity localIdentity) {
        this.localIdentity = localIdentity;
    }

    @Override
    public LocalIdentity getIdentity() {
        return localIdentity;
    }
}
