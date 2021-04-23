package org.visab.eventbus.publisher;

import org.visab.api.WebApi;
import org.visab.eventbus.IPublisher;

/**
 * The base publisher class, that all publishers should inherit from.
 *
 * @author moritz
 *
 * @param <TEvent> The event type that will be published
 */
public class PublisherBase<TEvent> implements IPublisher<TEvent> {

    @Override
    public void publish(TEvent event) {
	WebApi.getEventBus().publish(event);
    }

}