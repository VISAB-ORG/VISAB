package org.visab.api.model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.eventbus.event.SessionClosedEvent;
import org.visab.eventbus.publisher.ApiPublisherBase;
import org.visab.newgui.webapi.model.TransmissionSessionStatus;
import org.visab.util.Settings;

public class NewSessionWatchdog extends ApiPublisherBase<SessionClosedEvent> {

    // Logger needs .class for each class to use for log traces
    private Logger logger = LogManager.getLogger(NewSessionWatchdog.class);

    private List<TransmissionSessionStatus> statuses;

    private boolean checkTimeouts;

    /**
     * 
     * @param statusesReference The reference to the list were status will be added
     *                        to. Lists added to this collection will be checked for
     *                        timeout.
     */
    public NewSessionWatchdog(List<TransmissionSessionStatus> statusesReference) {
        this.statuses = statusesReference;
    }

    /**
     * Starts the infinite timeout checking loop on a different thread. Can only be
     * terminated by calling stopTimeoutLoop.
     */
    public void StartTimeoutLoop() {
        checkTimeouts = true;
        new Thread(() -> {
            try {
                while (checkTimeouts) {
                    for (var status : statuses) {
                        if (shouldTimeout(status)) {
                            status.setIsActive(false);
                            status.setSessionClosed(LocalTime.now());

                            var event = new SessionClosedEvent(status.getSessionId(), status, true);
                            publish(event);
                        }
                    }
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.info("Exception in Session timeout loop: " + e.getMessage());
            }
        }).start();
    }

    /**
     * Stops the timeout loop
     */
    public void stopTimeoutLoop() {
        checkTimeouts = false;
    }

    /**
     * Checks whether the current session should be timeouted.
     * 
     * @param status The status to check for timeout.
     * @return True if should be timeouted
     */
    private boolean shouldTimeout(TransmissionSessionStatus status) {
        if (!status.isActive())
            return false;

        var elapsedSeconds = Duration.between(status.getLastRequest(), LocalTime.now()).toSeconds();
        // If nothing was sent yet (only session openend) wait 30 more seconds until
        // timeout.
        var timeoutSeconds = Settings.SESSION_TIMEOUT;
        if (status.getTotalRequests() == 1)
            timeoutSeconds = Settings.SESSION_TIMEOUT + 30;

        return elapsedSeconds >= timeoutSeconds;
    }

}
