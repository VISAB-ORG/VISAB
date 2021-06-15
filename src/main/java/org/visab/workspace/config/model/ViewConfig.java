package org.visab.workspace.config.model;

public class ViewConfig {

    private String identifier;
    private String viewClass;
    private String viewModelClass;

    /**
     * For deserialization
     */
    public ViewConfig() {
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getViewClass() {
        return viewClass;
    }

    public String getViewModelClass() {
        return viewModelClass;
    }

    public void setViewModelClass(String viewModelClass) {
        this.viewModelClass = viewModelClass;
    }

    public void setClassPath(String viewClass) {
        this.viewClass = viewClass;
    }

}
