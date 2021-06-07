package org.visab.eventbus.event;

import org.visab.eventbus.IEvent;
import org.visab.globalmodel.IVISABFile;

public class VISABFileSavedEvent implements IEvent {

    private IVISABFile file;
    private boolean bySessionListener;

    public VISABFileSavedEvent(IVISABFile file, boolean bySessionListener) {
        this.file = file;
        this.bySessionListener = bySessionListener;
    }

    public IVISABFile getFile() {
        return file;
    }

    public boolean isBySessionListener() {
        return bySessionListener;
    }

}
