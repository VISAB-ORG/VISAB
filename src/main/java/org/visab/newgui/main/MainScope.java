package org.visab.newgui.main;

import org.visab.globalmodel.IVISABFile;

import de.saxsys.mvvmfx.Scope;

public class MainScope implements Scope {

    private IVISABFile file;

    public IVISABFile getAddedFile() {
        return file;
    }

    public void setAddedFile(IVISABFile file) {
        this.file = file;
    }

}
