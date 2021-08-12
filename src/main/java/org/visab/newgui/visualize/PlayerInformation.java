package org.visab.newgui.visualize;

/**
 * Helper class for displaying player information in meta views.
 */
public class PlayerInformation {
    
    private String name;
    private String controlledBy;
    private String color;

    public PlayerInformation(String name, String controlledBy, String color) {
        this.name = name;
        this.controlledBy = controlledBy;
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public String getName() {
        return this.name;
    }

    public String getControlledBy() {
        return this.controlledBy;
    }

}
