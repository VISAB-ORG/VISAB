package org.visab.eventbus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The ApiEventBus, used for notifying subscribers of new messages to the api.
 * Subscribers will be notified, when their subscribed event occurs.
 *
 * @author moritz
 *
 */
public class ApiEventBus {

    // Logger needs .class for each class to use for log traces
    private static Logger logger = LogManager.getLogger(ApiEventBus.class);

    /**
     * The current subscribers.
     */
    private Map<String, ArrayList<ISubscriber<?>>> subscribers = new HashMap<>();

    /**
     * Notifies all subscribers that are subscribed to TEvent of the given event.
     * 
     * @param <TEvent> The type of the event
     * @param event    The event that subscribers will be notified with
     */
    public <TEvent extends IEvent> void publish(TEvent event) {
        var eventType = event.getClass().getSimpleName().toString();

        if (subscribers.containsKey(eventType)) {
            // Make a copy, since the subscribers list will be modified if the event is of
            // type SessionClosedEvent.
            var _subscribers = this.<TEvent>castSubscribers(subscribers.get(eventType));
            for (var sub : _subscribers)
                sub.notify(event);
        }
    }

    /**
     * Adds a subscriber to the busses subscribers.
     * 
     * @param subscriber The subscriber to add
     */
    public void subscribe(ISubscriber<?> subscriber) {
        var eventType = subscriber.getSubscribedEventType();

        if (!subscribers.containsKey(eventType))
            subscribers.put(eventType, new ArrayList<ISubscriber<?>>());
        subscribers.get(eventType).add(subscriber);
    }

    /**
     * Removes a subscriber from the busses subscribers.
     * 
     * @param subscriber The subscriber to remove
     */
    public void unsubscribe(ISubscriber<?> subscriber) {
        var eventType = subscriber.getSubscribedEventType();

        if (subscribers.containsKey(eventType))
            subscribers.get(eventType).remove(subscriber);
        else
            logger.warn("Tried to remove subscriber that wasnt subscribed.");
    }

    /**
     * Casts the subscribers to their concrete EventType. If this throws an error,
     * your subscriber class was passed the wrong event class at initialization.
     * 
     * @param <TEvent>            The type of event to cast to
     * @param uncastedSubscribers The uncasted subscribers
     * @return A list of subscribers casted to TEvent
     */
    @SuppressWarnings("unchecked")
    private <TEvent extends IEvent> List<ISubscriber<TEvent>> castSubscribers(
            List<ISubscriber<?>> uncastedSubscribers) {
        var casted = new ArrayList<ISubscriber<TEvent>>();

        for (var sub : uncastedSubscribers)
            casted.add((ISubscriber<TEvent>) sub);

        return casted;
    }
}
