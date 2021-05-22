package org.visab.processing.starter;

import java.util.UUID;

import org.visab.globalmodel.starter.DefaultFile;
import org.visab.globalmodel.starter.DefaultStatistics;
import org.visab.processing.SessionListenerBase;

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
        repo.saveFile(file, sessionId.toString());
    }

    @Override
    public void onSessionStarted() {
        file = new DefaultFile(game);
    }

    @Override
    public void processStatistics(DefaultStatistics statistics) {
        file.getStatistics().add(statistics.getJson());
    }

}
