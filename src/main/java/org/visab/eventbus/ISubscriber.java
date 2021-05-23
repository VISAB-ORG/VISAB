package org.visab.eventbus;

/**
 * The ISubscriber interface that all subscribers have to implement. This
 * interface is needed, so that the EventBus can publish the exact event.
 *
 * @author moritz
 *
 * @param <TEvent> The event that will be subscribed to.
 */
public interface ISubscriber<TEvent extends IEvent> {

    /**
     * Called when a TEvent is published at the EventBus subscribed to. The
     * subscriber is notified with the concrete event.
     * 
     * @param event THe event published to the EventBus
     */
    void notify(TEvent event);

    /**
     * Gets the subscribed events simple name.
     * 
     * @return The subscribed events simple name
     */
    String getSubscribedEventType();

}
