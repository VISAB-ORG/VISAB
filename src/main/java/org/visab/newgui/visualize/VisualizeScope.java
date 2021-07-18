package org.visab.newgui.visualize;

import java.util.ArrayList;
import java.util.List;

import org.visab.globalmodel.IVISABFile;
import org.visab.processing.ILiveViewable;

import de.saxsys.mvvmfx.Scope;

public class VisualizeScope implements Scope {

    private boolean isLive;
    private IVISABFile file;
    private ILiveViewable<?> sessionListener;
    private List<ILiveViewModel<?>> viewModels = new ArrayList<>();

    public void setFile(IVISABFile file) {
        this.file = file;
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

    public void registerForStageClosing(ILiveViewModel<?> viewModel) {
        viewModels.add(viewModel);
    }

    public void notifyStageClosing() {
        for (var viewModel : viewModels)
            viewModel.onSessionClosed();
    }

}
