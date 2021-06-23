package org.visab.workspace.config.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Pojo representing a mapping configuration. Each of these fields has the fully
 * qualifying name of the class to initialize / deserialize into as their value.
 */
public class Mapping {

    private String game;

    private String metaInformation;

    private String listener;

    private String statistics;

    private String file;

    private String image;

    private List<ViewConfig> viewConfigurations = new ArrayList<>();

    /**
     * For deserialization
     */
    public Mapping() {
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

    public List<ViewConfig> getViewConfigurations() {
        return this.viewConfigurations;
    }

    public String getVisualizeView() {
        // TODO
        return "org.visab.newgui.visualize.cbrshooter.view.CBRShooterMainView";
    }

}
