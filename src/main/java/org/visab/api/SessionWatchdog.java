package org.visab.api;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.eventbus.event.SessionClosedEvent;
import org.visab.eventbus.publisher.ApiPublisherBase;
import org.visab.globalmodel.SessionStatus;
import org.visab.workspace.Workspace;

/**
 * The SessionWatchdog watches the currently active tranmission sessions, checks
 * if they should be timeouted and timeouts them if needed.
 */
public class SessionWatchdog extends ApiPublisherBase<SessionClosedEvent> {

    private Logger logger = LogManager.getLogger(SessionWatchdog.class);

    private List<SessionStatus> statuses;

    private boolean checkTimeouts;

    /**
     * @param statuses The list instance were statuses will be added to. Only
     *                 statuses contained in this list will be checked for timeout.
     */
    public SessionWatchdog(List<SessionStatus> statuses) {
        this.statuses = statuses;
    }

    /**
     * Checks whether a tranmission session should be timeouted.
     * 
     * @param status The status of the tranmission session, that is used to check
     *               for timeout
     * @return True if should be timeouted
     */
    private boolean shouldTimeout(SessionStatus status) {
        if (!status.isActive())
            return false;

        var elapsedSeconds = Duration.between(status.getLastRequest(), LocalTime.now()).toSeconds();
        // If the only request so far was to open a session, wait 30 more seconds until
        // timeout.
        var timeoutSeconds = Workspace.getInstance().getConfigManager().getSessionTimeout().get(status.getGame());
        if (status.getTotalRequests() == 1)
            timeoutSeconds += 30;

        return elapsedSeconds >= timeoutSeconds;
    }

    /**
     * Starts the infinite timeout checking loop on a seperate thread. This loop
     * will be stopped upong calling stopTimeoutLoop.
     */
    public void StartTimeoutLoop() {
        logger.info("Starting timeout loop for SessionWatchdog.");
        checkTimeouts = true;
        new Thread(() -> {
            try {
                while (checkTimeouts) {
                    for (var status : statuses) {
                        if (shouldTimeout(status)) {
                            logger.info("Closing session "
                                    + status.getSessionId() + " due to timeout of " + Workspace.getInstance()
                                            .getConfigManager().getSessionTimeout().get(status.getGame())
                                    + " seconds.");
                            ;
                            status.setIsActive(false);
                            status.setStatusType("timeouted");
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
     * Stops the timeout loop.
     */
    public void stopTimeoutLoop() {
        logger.info("Stopping timeout loop of SessionWatchdog.");
        checkTimeouts = false;
    }

}
