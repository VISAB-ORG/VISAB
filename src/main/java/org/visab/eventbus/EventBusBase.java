package org.visab.eventbus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.util.VISABUtil;

/**
 * An abstract base implementation of the EventPus.
 * 
 * @param <T> The type of the interface of the events to publish by this bus.
 *            The bus will be able to publish any events that implement T.
 */
public abstract class EventBusBase<T extends IEvent> {

    /**
     * The log4j logger.
     */
    protected Logger logger = LogManager.getLogger(this.getClass());

    /**
     * The current subscribers.
     */
    protected Map<String, ArrayList<ISubscriber<?>>> subscribers = new HashMap<>();

    /**
     * Notifies all subscribers that are subscribed to TEvent of the given event.
     * Also notifies subscribers of the interface type T.
     * 
     * @param <TEvent> The type of the event
     * @param event    The event that subscribers will be notified with
     */
    public <TEvent extends T> void publish(TEvent event) {
        var concreteEventName = event.getClass().getName();

        // Gets the name of the first interfaces that the event implements.
        // We can safely assume that our event implements atleast IApiEvent due to the
        // generic constrict.
        var interfaceEventName = VISABUtil.getAllInterfaces(event.getClass()).get(0).getName();

        // Notify concrete subscribers.
        if (subscribers.containsKey(concreteEventName)) {
            // Make a copy, since the subscribers list will be modified if the event is of
            // type SessionClosedEvent.
            var concreteSubscribers = this.<TEvent>castSubscribers(subscribers.get(concreteEventName));
            for (var sub : concreteSubscribers)
                sub.notify(event);
        }

        // Notify interface subscribers.
        if (subscribers.containsKey(interfaceEventName)) {
            var interfaceSubscribers = this.<TEvent>castSubscribers(subscribers.get(interfaceEventName));
            for (var sub : interfaceSubscribers)
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

        // Only add if not already subscribed
        if (!subscribers.get(eventType).contains(subscriber))
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
    protected <TEvent extends T> List<ISubscriber<T>> castSubscribers(List<ISubscriber<?>> uncastedSubscribers) {
        var casted = new ArrayList<ISubscriber<T>>();

        for (var sub : uncastedSubscribers)
            casted.add((ISubscriber<T>) sub);

        return casted;
    }
}
