package eventbus.subscribers;

import eventbus.ISubscriber;

public abstract class SubscriberBase<TEvent> implements ISubscriber<TEvent> {

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
