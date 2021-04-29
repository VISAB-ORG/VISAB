package org.newgui.model;

public class PersonTableRow {
    
    private Person person;

    public PersonTableRow(Person person) {
        this.person = person;
    }

    public int getAge() {
        return person.getAge();
    }
}
