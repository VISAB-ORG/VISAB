package org.visab.newgui.visualize;

import org.visab.globalmodel.IVISABFile;
import org.visab.processing.ILiveViewable;

import de.saxsys.mvvmfx.Scope;

public class VisualizeScope implements Scope {

    private IVISABFile file;
    private ILiveViewable<?> sessionListener;

    public void setFile(IVISABFile file) {
        this.file = file;
    }

    public ILiveViewable<?> getSessionListener() {
        return sessionListener;
    }

    public void setSessionListener(ILiveViewable<?> sessionListener) {
        this.sessionListener = sessionListener;
    }

    public IVISABFile getFile() {
        return file;
    }

}
