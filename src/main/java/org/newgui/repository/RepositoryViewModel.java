package org.newgui.repository;

import java.io.File;

import org.newgui.repository.model.FileRow;

import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.visab.repository.VISABRepository;
import org.visab.util.AssignByGame;

public class RepositoryViewModel implements ViewModel {

    private VISABRepository repo = new VISABRepository();

    public ObjectProperty<FileRow> selectedFileRow = new SimpleObjectProperty<>();

    public ObjectProperty<FileRow> selectedFileRowProperty() {
        return selectedFileRow;
    }

    private ReadOnlyIntegerProperty fileChanges = new SimpleIntegerProperty();

    public ReadOnlyIntegerProperty fileChangesProperty() {
        return fileChanges;
    }

    public void addFile(File file) {
        try {
            var json = repo.readFile(file.getAbsolutePath());
            var baseFile = repo.loadBaseFile(file.getAbsolutePath());

            var concreteFile = AssignByGame.getDeserializedFile(json, baseFile.getGame());
            if (repo.saveFile(concreteFile)) {
                fileChanges.add(1);
                System.out.println("add file");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void filesRefreshed() {
        if (fileChanges != null)
            fileChanges.subtract(fileChanges.get());
    }

}
