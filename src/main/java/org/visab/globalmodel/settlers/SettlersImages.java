package org.visab.globalmodel.settlers;

import org.visab.globalmodel.IImageContainer;

/**
 * This class represents the map image of Settlers of Catan which is sent by the
 * game.
 * 
 * @author leonr
 *
 */
public class SettlersImages implements IImageContainer {

    private byte[] cityImage;
    private String cityAnnotation;
    private byte[] streetImage;
    private String streetAnnotation;
    private byte[] villageImage;
    private String villageAnnotation;
    private byte[] mapImage;

    public byte[] getCityImage() {
        return this.cityImage;
    }

    public void setCityImage(byte[] cityImage) {
        this.cityImage = cityImage;
    }

    public byte[] getStreetImage() {
        return this.streetImage;
    }

    public void setStreetImage(byte[] streetImage) {
        this.streetImage = streetImage;
    }

    public byte[] getVillageImage() {
        return this.villageImage;
    }

    public void setVillageImage(byte[] villageImage) {
        this.villageImage = villageImage;
    }

    public byte[] getMapImage() {
        return this.mapImage;
    }

    public void setMapImage(byte[] mapImage) {
        this.mapImage = mapImage;
    }

    public String getCityAnnotation() {
        return cityAnnotation;
    }

    public void setCityAnnotation(String cityAnnotation) {
        this.cityAnnotation = cityAnnotation;
    }

    public String getStreetAnnotation() {
        return streetAnnotation;
    }

    public void setStreetAnnotation(String streetAnnotation) {
        this.streetAnnotation = streetAnnotation;
    }

    public String getVillageAnnotation() {
        return villageAnnotation;
    }

    public void setVillageAnnotation(String villageAnnotation) {
        this.villageAnnotation = villageAnnotation;
    }

}
