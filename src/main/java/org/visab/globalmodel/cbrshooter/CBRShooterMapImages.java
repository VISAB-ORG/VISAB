package org.visab.globalmodel.cbrshooter;

import java.util.HashMap;
import java.util.Map;

import org.visab.globalmodel.IImage;

public class CBRShooterMapImages implements IImage {

    private Map<String, byte[]> staticObjects = new HashMap<>();
    private Map<String, byte[]> moveableObjects = new HashMap<>();

    public Map<String,byte[]> getStaticObjects() {
        return this.staticObjects;
    }

    public void setStaticObjects(Map<String,byte[]> staticObjects) {
        this.staticObjects = staticObjects;
    }

    public Map<String,byte[]> getMoveableObjects() {
        return this.moveableObjects;
    }

    public void setMoveableObjects(Map<String,byte[]> moveableObjects) {
        this.moveableObjects = moveableObjects;
    }

    public byte[] getMap() {
        return this.map;
    }

    public void setMap(byte[] map) {
        this.map = map;
    }
    
    private byte[] map;

}
