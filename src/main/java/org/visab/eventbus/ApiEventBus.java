package org.visab.eventbus;

/**
 * The APIEventBus, used for notifying subscribers of new messages to the api.
 * Subscribers will be notified when their subscribed event occurs.
 */
public class APIEventBus extends EventBusBase<IAPIEvent> {

    private static APIEventBus instance;
    
    /**
     * Gets the singelton instance.
     * 
     * @return The instance
     */
    public static APIEventBus getInstance() {
        if (instance == null)
            instance = new APIEventBus();

        return instance;
    }

}
