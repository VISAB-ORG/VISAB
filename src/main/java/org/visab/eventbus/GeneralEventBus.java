package org.visab.eventbus;

/**
 * The GeneralEventBus used for publish API unrelated events.
 */
public class GeneralEventBus extends EventBusBase<IEvent> {

    private static GeneralEventBus instance;

    /**
     * Gets the singelton instance
     * 
     * @return The instance
     */
    public static GeneralEventBus getInstance() {
        if (instance == null)
            instance = new GeneralEventBus();

        return instance;
    }

}
