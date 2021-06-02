package org.visab.eventbus.publisher;

import org.visab.eventbus.ApiEventBus;
import org.visab.eventbus.IApiEvent;
import org.visab.eventbus.IPublisher;

/**
 * The base publisher class, that all api event publishers should inherit from.
 *
 * @author moritz
 *
 * @param <TEvent> The event type that will be published
 */
public class ApiPublisherBase<TEvent extends IApiEvent> implements IPublisher<TEvent> {

    @Override
    public void publish(TEvent event) {
        ApiEventBus.getInstance().publish(event);
    }

    // TOOD: Maybe add publish on different thread since we dont know how much time
    // subscribers will need.

}
