package org.visab.newgui.visualize;

import org.visab.globalmodel.IVISABFile;
import org.visab.newgui.ViewModelBase;

import de.saxsys.mvvmfx.InjectScope;

public abstract class VisualizeViewModelBase<TFile extends IVISABFile> extends ViewModelBase
        implements IVisualizeViewModel {

    /**
     * The scope that is injected by the DynamicViewLoader.
     */
    @InjectScope
    protected VisualizeScope scope;

    /**
     * The file to visualize. The reference stored in this variable should not be
     * changed.
     */
    protected TFile file;

    /**
     * Initializer for non live view.
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
