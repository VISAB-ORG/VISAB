package org.visab.eventbus.event;

import java.util.UUID;

import org.visab.eventbus.APIEventBase;
import org.visab.globalmodel.IMetaInformation;
import org.visab.globalmodel.SessionStatus;

/**
 * The SessionOpenedEvent that occurs when a new VISAB transmission session is
 * started.
 */
public class SessionOpenedEvent extends APIEventBase {

    private IMetaInformation metaInformation;

    public SessionOpenedEvent(UUID sessionId, SessionStatus status, IMetaInformation metaInformation) {
        super(sessionId, status);
        this.metaInformation = metaInformation;
    }

    public IMetaInformation getMetaInformation() {
        return this.metaInformation;
    }

}
