package org.visab.newgui.visualize;

import org.visab.globalmodel.IVISABFile;
import org.visab.newgui.ViewModelBase;

public abstract class ReplayViewModelBase<TFile extends IVISABFile> extends ViewModelBase {

    /**
     * The file to visualize
     */
    protected TFile file;

    /**
     * Initializer for replay view.
     * 
     * @param file The file to visualize
     */
    @SuppressWarnings("unchecked")
    public void initialize(IVISABFile file) {
        if (file == null)
            throw new RuntimeException("File was null!");

        this.file = (TFile) file;
    }

}
