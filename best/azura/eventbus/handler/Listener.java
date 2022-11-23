package best.azura.eventbus.handler;

import best.azura.eventbus.core.Event;

/**
 * @author Solastis
 * DATE:19.12.21
 */
public interface Listener<T extends Event> {

    /**
     * Method for calling an event
     *
     * @param event event that should be called
     */
    void call(final T event);
}
