package org.visab.eventbus.event;

import org.visab.eventbus.IEvent;
import org.visab.globalmodel.IVISABFile;

public class VISABFileViewedEvent implements IEvent {

    private IVISABFile file;

    public VISABFileViewedEvent(IVISABFile file) {
        this.file = file;
    }

    public IVISABFile getFile() {
        return file;
    }

}
