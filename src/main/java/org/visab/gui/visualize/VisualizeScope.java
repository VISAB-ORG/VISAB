package org.visab.gui.visualize;

import org.visab.globalmodel.IVISABFile;
import org.visab.gui.GeneralScope;
import org.visab.processing.ILiveViewable;

/**
 * Visualize scope that can be injected into viewmodels whose views are loaded
 * by the DynamicViewLoader. If the view was loaded as a live view, the session
 * listener should be used as a data source. If the view was loaded from a file,
 * the file should be used as a data source. The VisualizeScope is also a
 * GeneralScope and therefore exposes the stage that the view of the viewmodel
 * is living in.
 */
public class VisualizeScope extends GeneralScope {

    private boolean isLive;
    private IVISABFile file;
    private ILiveViewable<?> sessionListener;

    /**
     * Sets the file. This should only be called by the DynamicViewLoader.
     * 
     * @param file The file
     */
    public void setFile(IVISABFile file) {
        this.file = file;
    }

    /**
     * Sets whether the view was loaded a live view or not. Should only be called by
     * the DynamicViewLoader.
     * 
     * @param isLive True if the view was loaded as a live view
     */
    public void setLive(boolean isLive) {
        this.isLive = isLive;
    }

    /**
     * Sets the session listener. Should only be called by the DynamicViewLoader.
     * 
     * @param sessionListener The session listener
     */
    public void setSessionListener(ILiveViewable<?> sessionListener) {
        this.sessionListener = sessionListener;
    }

    /**
     * True if the view was loaded as a live view.
     */
    public boolean isLive() {
        return isLive;
    }

    /**
     * The VISAB file.
     */
    public IVISABFile getFile() {
        return file;
    }

    /**
     * The session listener of the session. Returns null if the view was not loaded
     * as a live view, else the session listener.
     */
    public ILiveViewable<?> getSessionListener() {
        return sessionListener;
    }

}
