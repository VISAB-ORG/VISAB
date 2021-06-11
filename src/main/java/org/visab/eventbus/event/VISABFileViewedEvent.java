package org.visab.eventbus.event;

import java.time.LocalDateTime;

import org.visab.eventbus.IEvent;
import org.visab.globalmodel.IVISABFile;

public class VISABFileViewedEvent implements IEvent {

    private IVISABFile file;

    private LocalDateTime viewedDate = LocalDateTime.now();

    public VISABFileViewedEvent(IVISABFile file) {
        this.file = file;
    }

    public IVISABFile getFile() {
        return file;
    }

    public LocalDateTime getViewedDate() {
        return viewedDate;
    }

}
