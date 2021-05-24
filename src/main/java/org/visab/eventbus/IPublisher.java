package org.visab.eventbus;

/**
 * The IPublisher interface, that all publishers have to implement.
 *
 * @param <TEvent> The event that will be published.
 */
public interface IPublisher<TEvent extends IEvent> {

    /**
     * Publishes a new event to the WebApi ApiEventBus.
     * 
     * @param event The event to publish
     */
    void publish(TEvent event);
}
