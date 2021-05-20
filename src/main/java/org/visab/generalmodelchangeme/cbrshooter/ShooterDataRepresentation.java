package org.visab.generalmodelchangeme.cbrshooter;

/**
 * Renamed from "StatisticsForPathViewer to ShooterDataRepresentation
 * 
 * Represents all information that is contained in a message string sent from
 * the unity game to VISAB.
 * 
 * @author VISAB 1.0 Group
 *
 */

public class ShooterDataRepresentation {

    private String ammuPosition;
    /**
     * Die Membervariablen der StatisticsForPathViewer-Klasse, welche jegliche
     * Informationen darstellen.
     */
    private String coordinatesCBRBot;

    private String coordinatesScriptBot;
    private String healthCBRBot;

    private String healthPosition;
    private String healthScriptBot;

    private String nameCBRBot;
    private String nameScriptBot;

    private String planCBRBot;
    private String planScriptBot;

    private String roundCounter;
    private String statisticCBRBot;

    private String statisticScriptBot;
    private String weaponCBRBot;

    private String weaponMagAmmuCBRBot;
    private String weaponMagAmmuScriptBot;
    private String weaponPosition;

    private String weaponScriptBot;

    /**
     * Default-Konstruktor, der fuer die JSON Serialisierung.
     */
    public ShooterDataRepresentation() {
        this("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
    }

    /**
     * Konstruktor zur Erzeugung eines StatisticsForPathViewer Objekt.
     *
     * Die Parameter sind die Strings der Membervariablen.
     */
    public ShooterDataRepresentation(String coordinatesCBRBot, String coordinatesScriptBot, String healthScriptBot,
            String healthCBRBot, String weaponScriptBot, String weaponCBRBot, String statisticCBRBot,
            String statisticScriptBot, String nameScriptBot, String nameCBRBot, String planCBRBot,
            String weaponMagAmmuCBRBot, String weaponMagAmmuScriptBot, String healthPosition, String weaponPosition,
            String ammuPosition, String roundCounter, String planScriptBot) {
        this.coordinatesCBRBot = coordinatesCBRBot;
        this.coordinatesScriptBot = coordinatesScriptBot;
        this.healthCBRBot = healthCBRBot;
        this.healthScriptBot = healthScriptBot;
        this.weaponCBRBot = weaponCBRBot;
        this.weaponScriptBot = weaponScriptBot;
        this.statisticCBRBot = statisticCBRBot;
        this.statisticScriptBot = statisticScriptBot;
        this.nameCBRBot = nameCBRBot;
        this.nameScriptBot = nameScriptBot;
        this.planCBRBot = planCBRBot;
        this.weaponMagAmmuCBRBot = weaponMagAmmuCBRBot;
        this.weaponMagAmmuScriptBot = weaponMagAmmuScriptBot;
        this.healthPosition = healthPosition;
        this.ammuPosition = ammuPosition;
        this.weaponPosition = weaponPosition;
        this.roundCounter = roundCounter;
        this.planScriptBot = planScriptBot;
    }

    public String getAmmuPosition() {
        return ammuPosition;
    }

    public String getCoordinatesCBRBot() {
        return coordinatesCBRBot;
    }

    public String getCoordinatesScriptBot() {
        return coordinatesScriptBot;
    }

    public String getHealthCBRBot() {
        return healthCBRBot;
    }

    /**
     * Getter und Setter Methoden f�r die Membervariablen
     *
     */

    public String getHealthPosition() {
        return healthPosition;
    }

    public String getHealthScriptBot() {
        return healthScriptBot;
    }

    public String getNameCBRBot() {
        return nameCBRBot;
    }

    public String getNameScriptBot() {
        return nameScriptBot;
    }

    public String getPlanCBRBot() {
        return planCBRBot;
    }

    public String getPlanScriptBot() {
        return planScriptBot;
    }

    public String getRoundCounterPosition() {
        return roundCounter;
    }

    public String getStatisticCBRBot() {
        return statisticCBRBot;
    }

    public String getStatisticScriptBot() {
        return statisticScriptBot;
    }

    public String getWeaponCBRBot() {
        return weaponCBRBot;
    }

    public String getWeaponMagAmmuCBRBot() {
        return weaponMagAmmuCBRBot;
    }

    public String getWeaponMagAmmuScriptBot() {
        return weaponMagAmmuScriptBot;
    }

    public String getWeaponPosition() {
        return weaponPosition;
    }

    public String getWeaponScriptBot() {
        return weaponScriptBot;
    }

    @Override
    public String toString() {
        return "StatisticsForPathViewer [coordinatesCBRBot=" + coordinatesCBRBot + "]" + "[coordinatesScriptBot = "
                + coordinatesScriptBot + "]" + "[healthScriptBot = " + healthScriptBot + "]" + "[healthCBRBot = "
                + healthCBRBot + "]" + "[weaponScriptBot = " + weaponScriptBot + "]" + "[weaponCBRBot = " + weaponCBRBot
                + "]" + "[statisticScriptBot = " + statisticScriptBot + "]" + "[statisticCBRBot = " + statisticCBRBot
                + "]" + "[nameScriptBot = " + nameScriptBot + "]" + "[nameCBRBot = " + nameCBRBot + "]"
                + "[planCBRBot = " + planCBRBot + "]" + "[weaponMagAmmuCBRBot = " + weaponMagAmmuCBRBot + "]"
                + "[weaponMagAmmuScriptBot = " + weaponMagAmmuScriptBot + "]" + "[healthPosition = " + healthPosition
                + "]" + "[weaponPosition = " + weaponPosition + "]" + "[ammuPosition = " + ammuPosition + "]"
                + "[roundCounter = " + roundCounter + "]" + "[planScriptBot = " + planScriptBot + "]";
    }

}
