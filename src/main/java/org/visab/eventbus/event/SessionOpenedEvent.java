package org.visab.eventbus.event;

import java.util.UUID;

import org.visab.globalmodel.TransmissionSessionStatus;

/**
 * The SessionOpenedEvent that occurs when a new VISAB transmission session is
 * started.
 *
 * @author moritz
 *
 */
public class SessionOpenedEvent extends ApiEventBase {

    private String game;
    private String remoteCallerIp = "";
    private String remoteCallerHostName = "";

    public SessionOpenedEvent(UUID sessionId, TransmissionSessionStatus status, String game) {
        super(sessionId, status);
        this.game = game;
    }

    public SessionOpenedEvent(UUID sessionId, TransmissionSessionStatus status, String game, String remoteCallerIp, String remoteCallerHostName) {
        super(sessionId,status);
        this.game = game;
        this.remoteCallerIp = remoteCallerIp;
        this.remoteCallerHostName = remoteCallerHostName;
    }

    public String getGame() {
        return game;
    }

    public String getRemoteCallerIp() {
        return this.remoteCallerIp;
    }

    public String getRemoteCallerHostName() {
        return this.remoteCallerHostName;
    }
}
