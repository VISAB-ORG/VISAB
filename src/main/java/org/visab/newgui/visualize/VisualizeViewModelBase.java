package org.visab.newgui.visualize;

import org.visab.globalmodel.IVISABFile;
import org.visab.newgui.ViewModelBase;

import de.saxsys.mvvmfx.InjectScope;

/**
 * The base class for any visualizer view model. If your view model should not
 * be live viewable and isnt dependant on images, extend this. Otherwise, use
 * one of the more specific base implementations.
 */
public abstract class VisualizeViewModelBase<TFile extends IVISABFile> extends ViewModelBase {

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
