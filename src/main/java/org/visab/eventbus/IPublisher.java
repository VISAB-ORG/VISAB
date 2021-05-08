package org.visab.eventbus;

/**
 * The IPublisher interface, that all publishers have to implement.
 *
 * @author moritz
 *
 * @param <TEvent> The event that will be published.
 */
public interface IPublisher<TEvent extends IEvent> {

    void publish(TEvent event);
}
