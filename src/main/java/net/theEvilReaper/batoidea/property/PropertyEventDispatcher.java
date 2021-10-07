package net.theEvilReaper.batoidea.property;

import net.theEvilReaper.bot.api.property.PropertyEventCall;
import net.theEvilReaper.bot.api.util.Conditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/

public class PropertyEventDispatcher implements PropertyEventCall {

    private final PropertyChangeSupport propertyChangeSupport;

    private final Object source;

    public PropertyEventDispatcher(Object source) {
        this.source = source;
        this.propertyChangeSupport = createSupport();
    }

    private PropertyChangeSupport createSupport() {
        return new PropertyChangeSupport(source);
    }

    @Override
    public void addListener(@NotNull PropertyChangeListener listener) {
        this.propertyChangeSupport.addPropertyChangeListener(listener);
    }

    @Override
    public void addListener(@NotNull String source, @NotNull PropertyChangeListener listener) {
        Conditions.checkForEmpty(source);
        this.propertyChangeSupport.addPropertyChangeListener(source, listener);
    }

    @Override
    public void removeListener(@NotNull PropertyChangeListener listener) {
        this.propertyChangeSupport.removePropertyChangeListener(listener);
    }

    @Override
    public void call(@NotNull Object oldSource, @NotNull Object newSource) {
        this.propertyChangeSupport.firePropertyChange(String.valueOf(source), oldSource, newSource);
    }

    @Override
    public void call(@NotNull String propertyName, @NotNull Object oldSource, @NotNull Object newSource) {
        Conditions.checkForEmpty(propertyName);
        this.propertyChangeSupport.firePropertyChange(propertyName, oldSource, newSource);
    }

    @Override
    public void call(@NotNull PropertyChangeEvent event) {
        this.propertyChangeSupport.firePropertyChange(event);
    }

    @Override
    public boolean hasListener(@NotNull String source) {
        Conditions.checkForEmpty(source);
        return this.propertyChangeSupport.hasListeners(source);
    }

    @Override
    @Nullable
    public PropertyChangeListener[] getListeners(@NotNull String source) {
        Conditions.checkForEmpty(source);
        return this.propertyChangeSupport.getPropertyChangeListeners(source);
    }

    @Override
    public PropertyChangeListener[] getListeners() {
        return this.propertyChangeSupport.getPropertyChangeListeners();
    }

    public PropertyChangeSupport getPropertyChangeSupport() {
        return propertyChangeSupport;
    }
}
