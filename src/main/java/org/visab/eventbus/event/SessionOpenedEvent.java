package org.visab.eventbus.event;

import java.util.UUID;

import org.visab.globalmodel.IMetaInformation;
import org.visab.globalmodel.SessionStatus;

/**
 * The SessionOpenedEvent that occurs when a new VISAB transmission session is
 * started.
 *
 * @author moritz
 *
 */
public class SessionOpenedEvent extends ApiEventBase {

    private IMetaInformation metaInformation;

    public SessionOpenedEvent(UUID sessionId, SessionStatus status, IMetaInformation metaInformation) {
        super(sessionId, status);
        this.metaInformation = metaInformation;
    }

    public IMetaInformation getMetaInformation() {
        return this.metaInformation;
    }

}
