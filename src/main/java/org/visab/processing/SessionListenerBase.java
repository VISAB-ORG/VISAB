package org.visab.processing;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.eventbus.APISubscriberBase;
import org.visab.eventbus.APIEventBus;
import org.visab.eventbus.ISubscriber;
import org.visab.eventbus.event.SessionClosedEvent;
import org.visab.eventbus.event.StatisticsReceivedEvent;
import org.visab.globalmodel.IMetaInformation;
import org.visab.globalmodel.IStatistics;
import org.visab.util.StringFormat;
import org.visab.workspace.DatabaseManager;
import org.visab.workspace.Workspace;

/**
 * The base SessionListener class, that should be implemented by all session
 * listeners.
 * 
 * @param <TStatistics> The statistics type that will be processed by the
 *                      listener
 */
public abstract class SessionListenerBase<TMeta extends IMetaInformation, TStatistics extends IStatistics>
        implements ISessionListener {

    /**
     * The SessionClosedSubscriber, that subscribes to the SessionClosedEvent event.
     */
    private class SessionClosedSubscriber extends APISubscriberBase<SessionClosedEvent> {

        public SessionClosedSubscriber() {
            super(SessionClosedEvent.class);
        }

        @Override
        public void notify(SessionClosedEvent event) {
            if (event.getSessionId().equals(sessionId)) {
                SessionListenerAdministration.removeListener(SessionListenerBase.this);

                // Unsubscribe all subscribers
                for (var sub : subscribers)
                    APIEventBus.getInstance().unsubscribe(sub);

                isActive = false;
                onSessionClosed();
            }
        }
    }

    /**
     * The StatisticsSubscriber, that subscribes to the StatisticsReceivedEvent.
     */
    private class StatisticsSubscriber extends APISubscriberBase<StatisticsReceivedEvent> {

        public StatisticsSubscriber() {
            super(StatisticsReceivedEvent.class);
        }

        @Override
        @SuppressWarnings("unchecked")
        public void notify(StatisticsReceivedEvent event) {
            if (event.getSessionId().equals(sessionId)) {
                lastReceived = LocalTime.now();

                var statistics = event.getStatistics();
                if (statistics == null)
                    writeLog(Level.INFO, "Received Statistics was null!");
                else
                    processStatistics((TStatistics) statistics);
            }
        }
    }

    /**
     * The logger.
     */
    protected Logger logger = LogManager.getLogger(this.getClass());

    /**
     * The game of the listener.
     */
    protected String game;

    /**
     * Whether the listener is still actively listening to events.
     */
    protected boolean isActive = true;

    /**
     * The time at which the last statistics object was received for the session.
     */
    protected LocalTime lastReceived = LocalTime.now();

    /**
     * The DatabaseManager used for saving files.
     */
    protected DatabaseManager manager = Workspace.getInstance().getDatabaseManager();

    /**
     * The sessionId of the corresponding transmission session.
     */
    protected UUID sessionId;

    /**
     * List of all subscribers. All subscribers in this list will be unsubscribed on
     * the SessionClosedEvent.
     */
    protected List<ISubscriber<?>> subscribers = new ArrayList<>();

    public SessionListenerBase(String game, UUID sessionId) {
        this.game = game;
        this.sessionId = sessionId;

        var statisticsSubscriber = new StatisticsSubscriber();
        var sessionClosedSubscriber = new SessionClosedSubscriber();

        subscribers.add(statisticsSubscriber);
        subscribers.add(sessionClosedSubscriber);
    }

    @Override
    public String getGame() {
        return game;
    }

    @Override
    public LocalTime getLastReceived() {
        return lastReceived;
    }

    @Override
    public UUID getSessionId() {
        return sessionId;
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void initialize(IMetaInformation metaInformation) {
        onSessionStarted((TMeta) metaInformation);
    }

    /**
     * Writes to the log using a prefix containing session information.
     * 
     * @param logLevel The logLevel of the message to log
     * @param message  The message to log
     */
    protected void writeLog(Level logLevel, String message) {
        var prefix = StringFormat.niceString("[{0}: {1}]> ", game, sessionId);

        logger.log(logLevel, prefix + message);
    }

    /**
     * Called when the listeners corresponding session is closed.
     */
    protected abstract void onSessionClosed();

    /**
     * Called when the listeners is started.
     * 
     * @param metaInformation The meta information object using which the session
     *                        was opened.
     */
    protected abstract void onSessionStarted(TMeta metaInformation);

    /**
     * Called upon reciving statistics for the current session. Is only called if
     * the received Statistics object was not null.
     * 
     * @param statistics The received TStatistics object
     */
    protected abstract void processStatistics(TStatistics statistics);

}
