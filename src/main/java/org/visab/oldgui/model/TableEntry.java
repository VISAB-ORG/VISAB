package org.visab.oldgui.model;

/**
 * Base TableEntry used for generically displaying visab files in the
 * StatisticsView.
 * 
 * @author VISAB 1.0 group
 *
 */
public class TableEntry {

    private String name;
    private String value;

    public TableEntry() {
    }

    public TableEntry(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
