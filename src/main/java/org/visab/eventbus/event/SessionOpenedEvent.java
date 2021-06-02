package org.visab.eventbus.event;

import java.util.UUID;

import org.visab.newgui.webapi.model.TransmissionSessionStatus;

/**
 * The SessionOpenedEvent that occurs when a new VISAB transmission session is
 * started.
 *
 * @author moritz
 *
 */
public class SessionOpenedEvent extends ApiEventBase {

    private String game;

    public SessionOpenedEvent(UUID sessionId, TransmissionSessionStatus status, String game) {
        super(sessionId, status);
        this.game = game;
    }

    public String getGame() {
        return game;
    }

}
