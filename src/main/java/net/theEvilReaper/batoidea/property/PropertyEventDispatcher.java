package net.theEvilReaper.batoidea.property;

import net.theEvilReaper.bot.api.property.PropertyEventCall;
import net.theEvilReaper.bot.api.util.Conditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * The class is the implementation of the {@link PropertyEventCall}.
 * The class is bound to an object for the usage. So you need several instances for several events.
 * But this way it is directly recognizable which instance is for which event.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public class PropertyEventDispatcher implements PropertyEventCall {

    private final PropertyChangeSupport propertyChangeSupport;

    private final Object source;

    /**+
     * Creates a new instance from the {@link PropertyEventDispatcher}.
     * @param source The source that will be used for the {@link PropertyChangeSupport}
     */

    public PropertyEventDispatcher(@NotNull Object source) {
        this.source = source;
        this.propertyChangeSupport = createSupport();
    }

    /**
     * Creates a new {@link PropertyChangeSupport} instance
     * @return the created instance
     */

    private PropertyChangeSupport createSupport() {
        return new PropertyChangeSupport(source);
    }

    /**
     * Add a {@link PropertyChangeListener} to the instance.
     * @param listener The listener to add
     */

    @Override
    public void addListener(@NotNull PropertyChangeListener listener) {
        this.propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Add a {@link PropertyChangeListener} to a given source
     * @param source The source where the listener should be register
     * @param listener The listener to register
     */

    @Override
    public void addListener(@NotNull String source, @NotNull PropertyChangeListener listener) {
        Conditions.checkForEmpty(source);
        this.propertyChangeSupport.addPropertyChangeListener(source, listener);
    }

    /**
     * Removes a {@link PropertyChangeListener}.
     * @param listener The listner to remove
     */

    @Override
    public void removeListener(@NotNull PropertyChangeListener listener) {
        this.propertyChangeSupport.removePropertyChangeListener(listener);
    }

    /**
     * Calls a new {@link PropertyChangeEvent} with the given values.
     * The event will be fired to the main property
     * @param oldSource The old object
     * @param newSource The new object
     */

    @Override
    public void call(@NotNull Object oldSource, @NotNull Object newSource) {
        this.propertyChangeSupport.firePropertyChange(String.valueOf(source), oldSource, newSource);
    }

    /**
     * Calls a new {@link PropertyChangeEvent} with the given value,
     * @param propertyName The name of the property that was changed
     * @param oldSource The old object
     * @param newSource The new object
     */

    @Override
    public void call(@NotNull String propertyName, @NotNull Object oldSource, @NotNull Object newSource) {
        Conditions.checkForEmpty(propertyName);
        this.propertyChangeSupport.firePropertyChange(propertyName, oldSource, newSource);
    }

    /**
     * Call a new instance of an {@link PropertyChangeEvent}.
     * @param event The event to call
     */

    @Override
    public void call(@NotNull PropertyChangeEvent event) {
        this.propertyChangeSupport.firePropertyChange(event);
    }

    /**
     * Check if a given source has {@link PropertyChangeListener}.
     * @param source The source to check if the source has listener
     * @return True when the source has some {@link PropertyChangeListener} otherwise false
     */

    @Override
    public boolean hasListener(@NotNull String source) {
        Conditions.checkForEmpty(source);
        return this.propertyChangeSupport.hasListeners(source);
    }

    /**
     * Get all listener for a given source.
     * @param source The source to determine the listeners
     * @return All listeners that matches with the specified source.
     */

    @Override
    @Nullable
    public PropertyChangeListener[] getListeners(@NotNull String source) {
        Conditions.checkForEmpty(source);
        return this.propertyChangeSupport.getPropertyChangeListeners(source);
    }

    /**
     * Get all current registered {@link PropertyChangeListener}.
     * @return all registered listener
     */

    @Override
    public PropertyChangeListener[] getListeners() {
        return this.propertyChangeSupport.getPropertyChangeListeners();
    }

}