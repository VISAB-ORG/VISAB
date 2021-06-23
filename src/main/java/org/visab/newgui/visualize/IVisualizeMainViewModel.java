package org.visab.newgui.visualize;

import org.visab.globalmodel.IVISABFile;
import org.visab.processing.ILiveViewable;

public interface IVisualizeMainViewModel {

    void setFile(IVISABFile file);

    void setListener(ILiveViewable<?> listener);

    void setLive(boolean isLive);

}
