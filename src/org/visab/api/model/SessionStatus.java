package org.visab.api.model;

import java.util.UUID;

/**
 * A POJO for sending responses including session status.
 *
 * @author moritz
 *
 */
public class SessionStatus {

    private boolean active;
    private String game;
    private UUID sessionId;

    public SessionStatus(boolean active, UUID sessionId, String game) {
	this.active = active;
	this.sessionId = sessionId;
	this.game = game;
    }

    public String getGame() {
	return game;
    }

    public UUID getSessionId() {
	return sessionId;
    }

    public boolean isActive() {
	return active;
    }
}
