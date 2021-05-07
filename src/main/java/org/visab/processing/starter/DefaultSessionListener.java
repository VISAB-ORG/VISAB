package org.visab.processing.starter;

import java.util.UUID;

import org.visab.processing.SessionListenerBase;
import org.visab.processing.starter.model.DefaultStatistics;

/**
 * This is a session listener default class, used for when games are allowed
 * (added in settings) but there is no SessionListener implementation for them
 * yet.
 */
public class DefaultSessionListener extends SessionListenerBase<DefaultStatistics> {

    private DefaultFile file;

    public DefaultSessionListener(String game, UUID sessionId) {
        super(game, sessionId);
    }

    @Override
    public void onSessionClosed() {
        repo.saveFile(file);
    }

    @Override
    public void onSessionStarted() {
        file = new DefaultFile(game, sessionId.toString());
    }

    @Override
    public void processStatistics(DefaultStatistics statistics) {
        file.getStatistics().add(statistics.getJson());
    }

}
