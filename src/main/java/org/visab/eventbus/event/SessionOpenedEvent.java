package org.visab.eventbus.event;

import java.util.UUID;

/**
 * The SessionOpenedEvent that occurs when a new VISAB transmission session is
 * started.
 *
 * @author moritz
 *
 */
public class SessionOpenedEvent extends EventBase {

    private String game;
    private String remoteCallerIp = "";
    private String remoteCallerHostName = "";

    public SessionOpenedEvent(UUID sessionId, String game) {
        super(sessionId);
        this.game = game;
    }

    public SessionOpenedEvent(UUID sessionId, String game, String remoteCallerIp, String remoteCallerHostName) {
        super(sessionId);
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
