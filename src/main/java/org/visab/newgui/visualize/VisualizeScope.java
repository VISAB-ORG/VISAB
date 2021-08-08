package org.visab.newgui.visualize;

import org.visab.globalmodel.IVISABFile;
import org.visab.newgui.GeneralScope;
import org.visab.processing.ILiveViewable;

import javafx.stage.Stage;

public class VisualizeScope extends GeneralScope {

    private Stage stage;
    private boolean isLive;
    private IVISABFile file;
    private ILiveViewable<?> sessionListener;

    public void setFile(IVISABFile file) {
        this.file = file;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean isLive) {
        this.isLive = isLive;
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
