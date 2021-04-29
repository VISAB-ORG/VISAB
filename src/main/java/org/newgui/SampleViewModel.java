package org.newgui;

import org.newgui.model.Person;
import org.newgui.model.PersonTableRow;

import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SampleViewModel implements ViewModel {
    
    private ObservableList<PersonTableRow> persons = FXCollections.observableArrayList();
    private ObjectProperty<PersonTableRow> selectedItem;

    public ObservableList<PersonTableRow> personsProperty() {
        return persons;
    }

    public ObjectProperty<PersonTableRow> selectedItemProperty() {
        return selectedItem;
    }

    public void addTableRow() {
        persons.add(new PersonTableRow(new Person(15)));
    }


    private StringProperty sampleMessage = new SimpleStringProperty("Im a sample message");

    public StringProperty sampleMessageProperty() {
        return sampleMessage;
    }

    public String getSampleMessage() {
        return sampleMessage.get();
    }

    public void setSampleMessage(String message) {
        sampleMessage.set(message);
    }

}
