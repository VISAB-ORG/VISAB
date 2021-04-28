package org.visab.eventbus;

/**
 * The ISubscriber interface that all subscribers have to implement.
 *
 * @author moritz
 *
 * @param <TEvent> The event that will be subscribed to.
 */
public interface ISubscriberWithEvent<TEvent> {

    void invoke(TEvent event);

}
