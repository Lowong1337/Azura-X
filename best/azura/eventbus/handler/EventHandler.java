package best.azura.eventbus.handler;

import best.azura.eventbus.core.EventPriority;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandler {
    EventPriority value() default EventPriority.DEFAULT;
}