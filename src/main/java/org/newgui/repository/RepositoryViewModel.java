package org.newgui.repository;

import org.newgui.repository.model.FileRow;

import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class RepositoryViewModel implements ViewModel {

    public ObjectProperty<FileRow> selectedFileRow = new SimpleObjectProperty<>();

    public ObjectProperty<FileRow> selectedFileRowProperty() {
        return selectedFileRow;
    }

}
