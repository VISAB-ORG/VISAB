package org.visab.eventbus;

/**
 * The APIPublisherBase class, that all api event publishers should inherit
 * from.
 *
 * @param <TEvent> The event type that will be published
 */
public class APIPublisherBase<TEvent extends IAPIEvent> implements IPublisher<TEvent> {

    @Override
    public void publish(TEvent event) {
        APIEventBus.getInstance().publish(event);
        // publishDifferentThread(event);
    }

    public void publishDifferentThread(TEvent event) {
        new Thread(() -> APIEventBus.getInstance()).start();
    }

}
