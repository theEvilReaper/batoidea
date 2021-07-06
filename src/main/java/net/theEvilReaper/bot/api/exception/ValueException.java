package net.theEvilReaper.bot.api.exception;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public class ValueException extends Exception {

    private final String object;

    public ValueException(String object, String message) {
        super(message);
        this.object = object;
    }

    public ValueException(String object, String message, Throwable cause) {
        super(message, cause);
        this.object = object;
    }

    public ValueException(String object, Throwable cause) {
        super(cause);
        this.object = object;
    }

    public String getObject() {
        return object;
    }
}
