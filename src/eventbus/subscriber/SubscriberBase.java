package eventbus.subscriber;

import eventbus.ISubscriber;
import eventbus.ISubscriberWithEvent;

public abstract class SubscriberBase<TEvent> implements ISubscriber, ISubscriberWithEvent<TEvent> {

    private String subscribedEventType;

    public SubscriberBase(String subscribedEventType) {
	this.subscribedEventType = subscribedEventType;
	// Taken from
	// https://stackoverflow.com/questions/3403909/get-generic-type-of-class-at-runtime
	// this.persistentClass = (Class<TEvent>) ((ParameterizedType) getClass()
	// .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public String getSubscribedEventType() {
	return subscribedEventType;
    }
}
