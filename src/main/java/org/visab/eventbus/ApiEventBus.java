package org.visab.eventbus;

/**
 * The ApiEventBus, used for notifying subscribers of new messages to the api.
 * Subscribers will be notified, when their subscribed event occurs.
 *
 * @author moritz
 *
 */
public class ApiEventBus extends EventBusBase<IApiEvent> {

    /**
     * Singelton instance
     */
    private static ApiEventBus instance;

    /**
     * Gets the singelton instance
     * 
     * @return The instance
     */
    public static ApiEventBus getInstance() {
        if (instance == null)
            instance = new ApiEventBus();

        return instance;
    }

}
