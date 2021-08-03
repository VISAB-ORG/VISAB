package org.visab.processing.starter;

import java.util.UUID;

import org.visab.globalmodel.starter.DefaultFile;
import org.visab.globalmodel.starter.DefaultMetaInformation;
import org.visab.globalmodel.starter.DefaultStatistics;
import org.visab.processing.SessionListenerBase;

/**
 * Serves as a session listener default class, that can be used for when games
 * are allowed in VISAB settings but there is no implemenatation for them yet.
 */
public class DefaultSessionListener extends SessionListenerBase<DefaultMetaInformation, DefaultStatistics> {

    private DefaultFile file;

    public DefaultSessionListener(String game, UUID sessionId) {
        super(game, sessionId);
    }

    @Override
    public void onSessionClosed() {
        manager.saveFile(file, sessionId.toString(), sessionId);
    }

    @Override
    public void onSessionStarted(DefaultMetaInformation metaInformation) {
        file = new DefaultFile(game);
        // TODO: Do something with the meta information
    }

    @Override
    public void processStatistics(DefaultStatistics statistics) {
        file.getStatistics().add(statistics.getJson());
    }

}
