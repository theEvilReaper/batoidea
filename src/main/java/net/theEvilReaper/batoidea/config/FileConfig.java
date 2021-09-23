package net.theEvilReaper.batoidea.config;

import net.theEvilReaper.bot.api.config.BotConfig;
import net.theEvilReaper.bot.api.config.Config;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public class FileConfig extends Config implements BotConfig {

    @Override
    public void load() {
      /* if (!Files.exists(path)) {

        } else {
            try (InputStream inputStream = Files.newInputStream(path)) {
                this.properties.load(inputStream);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }*/
    }

    @Override
    public void save() {
        /*try (OutputStream outputStream = Files.newOutputStream(path)) {
            this.properties.store(outputStream, null);
        } catch (Exception exception) {
            exception.printStackTrace();
        }*/
    }
}