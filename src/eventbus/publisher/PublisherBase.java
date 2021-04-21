package eventbus.publisher;

import api.WebApi;
import eventbus.IPublisher;

public class PublisherBase<TEvent> implements IPublisher<TEvent> {

    @Override
    public void publish(TEvent event) {
	WebApi.getEventBus().Publish(event);
    }

}
