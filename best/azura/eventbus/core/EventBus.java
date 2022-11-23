package best.azura.eventbus.core;

import best.azura.eventbus.handler.EventExecutable;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;

public class EventBus {

    /**
     * List of all executables in the event system
     */
    private final ArrayList<EventExecutable> executables = new ArrayList<>();

    /**
     * Register an object in the event system
     *
     * @param object the object to register
     */
    public void register(final Object object) {
        if (isRegistered(object)) return;
        for (final Method method : object.getClass().getDeclaredMethods()) {
            if (!method.isAnnotationPresent(EventHandler.class)
                    || method.getParameterCount() <= 0) continue;
            executables.add(new EventExecutable(method, object, method.getDeclaredAnnotation(EventHandler.class).value()));
        }
        for (final Field field : object.getClass().getDeclaredFields()) {
            if (!field.isAnnotationPresent(EventHandler.class)
                    || !field.getType().isAssignableFrom(Listener.class)) continue;

            executables.add(new EventExecutable(field, object, field.getDeclaredAnnotation(EventHandler.class).value()));
        }
        executables.sort(Comparator.comparingInt(EventExecutable::getPriority));
    }

    /**
     * Method used for calling events
     *
     * @param event the event that should be called
     */
    public void call(final Event event) {
        try {
            for (final EventExecutable eventExecutable : executables) {
                try {
                    if (eventExecutable.getListener() != null)
                        eventExecutable.getListener().call(event);
                    if (eventExecutable.getMethod() != null)
                        eventExecutable.getMethod().call(event);
                } catch (Exception ignored) {
                }
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * Unregister an object from the event system
     *
     * @param object the object to unregister
     */
    public void unregister(final Object object) {
        if (!isRegistered(object)) return;
        executables.removeIf(e -> e.getParent().equals(object));
    }

    /**
     * Method used to check whether an object is registered in the event system
     *
     * @param object the object to check
     */
    public boolean isRegistered(final Object object) {
        return executables.stream().anyMatch(e -> e.getParent().equals(object));
    }

}