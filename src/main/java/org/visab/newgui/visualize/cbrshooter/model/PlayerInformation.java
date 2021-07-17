package org.visab.newgui.visualize.cbrshooter.model;

public class PlayerInformation {
    private String name;
    private String controlledBy;

    public PlayerInformation(String name, String controlledBy) {
        this.name = name;
        this.controlledBy = controlledBy;
    }

    public String getName() {
        return this.name;
    }

    public String getControlledBy() {
        return this.controlledBy;
    }

}
