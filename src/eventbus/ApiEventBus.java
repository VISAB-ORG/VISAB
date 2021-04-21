package eventbus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ApiEventBus {

    private Map<String, ArrayList<ISubscriber>> subscribers = new HashMap<>();

    public <TEvent> void Publish(TEvent event) {
	var eventType = event.getClass().toString();

	if (subscribers.containsKey(eventType))
	    for (var subscriber : subscribers.get(eventType)) {
		@SuppressWarnings("unchecked")
		var correctSubscriber = ((ISubscriberWithEvent<TEvent>) subscriber);
		correctSubscriber.invoke(event);
	    }
    }

    public void subscribe(ISubscriber subscriber) {
	var eventType = subscriber.getSubscribedEventType();

	if (!subscribers.containsKey(eventType))
	    subscribers.put(eventType, new ArrayList<ISubscriber>());
	subscribers.get(eventType).add(subscriber);
    }

    public void unsubscribe(ISubscriber subscriber) {
	var eventType = subscriber.getSubscribedEventType();

	if (subscribers.containsKey(eventType))
	    subscribers.get(eventType).remove(subscriber);
	else {
	    // Throw some exception
	}
    }
}
