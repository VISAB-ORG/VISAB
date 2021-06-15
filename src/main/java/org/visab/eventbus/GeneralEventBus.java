package org.visab.eventbus;

public class GeneralEventBus extends EventBusBase<IEvent> {

    /**
     * Singelton instance
     */
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
