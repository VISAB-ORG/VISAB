package org.visab.workspace.config.model;

public class MappingConfig {

    private String game;

    private String listener;

    private String statistics;

    private String file;

    private String image;

    /**
     * For deserialization
     */
    public MappingConfig() {
    }

    public String getGame() {
        return this.game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getListener() {
        return this.listener;
    }

    public void setListener(String listener) {
        this.listener = listener;
    }

    public String getStatistics() {
        return this.statistics;
    }

    public void setStatistics(String statistics) {
        this.statistics = statistics;
    }

    public String getFile() {
        return this.file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
