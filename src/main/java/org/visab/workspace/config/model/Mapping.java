package org.visab.workspace.config.model;

/**
 * Pojo representing a mapping configuration. Each of these fields has the fully
 * qualifying name of a java class as their value.
 */
public class Mapping {

    private String game;

    private String metaInformation;

    private String listener;

    private String statistics;

    private String file;

    private String image;

    private String visualizer;

    public String getVisualizer() {
        return visualizer;
    }

    public void setVisualizer(String visualizerView) {
        this.visualizer = visualizerView;
    }

    public String getMetaInformation() {
        return metaInformation;
    }

    public void setMetaInformation(String metaInformation) {
        this.metaInformation = metaInformation;
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
