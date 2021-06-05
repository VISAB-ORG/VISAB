package org.visab.workspace.config.model;

public class ViewConfig {

    private String identifier;
    private String classPath;

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

    public String getClassPath() {
        return classPath;
    }

    public void setClassPath(String classPath) {
        this.classPath = classPath;
    }

    public ViewConfig(String identifier, String classPath) {
        this.setIdentifier(identifier);
        this.setClassPath(classPath);
    }

}
