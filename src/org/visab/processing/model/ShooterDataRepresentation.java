package org.visab.processing.model;

public class ShooterDataRepresentation {
	
	/**
	 * Die Membervariablen der StatisticsForPathViewer-Klasse,
	 * welche jegliche Informationen darstellen.
	 */
	private String coordinatesCBRBot;
	private String coordinatesScriptBot;
	
	private String healthCBRBot;
	private String healthScriptBot;
	
	private String weaponCBRBot;
	private String weaponScriptBot;
	
	private String weaponMagAmmuCBRBot;
	private String weaponMagAmmuScriptBot;
	
	private String statisticCBRBot;
	private String statisticScriptBot;
	
	private String nameCBRBot;
	private String nameScriptBot;
	
	private String planCBRBot;
	private String planScriptBot;
	
	private String healthPosition;
	private String weaponPosition;
	private String ammuPosition;
	
	private String roundCounter;
	
	
	/**
	 * Getter und Setter Methoden für die Membervariablen
	 * 
	 */
	
	public String getHealthPosition() {
		return healthPosition;
	}

	public void setHealthPosition(String test) {
		this.healthPosition = healthPosition;
	}
	
	public String getRoundCounterPosition() {
		return roundCounter;
	}

	public void setRoundCounter(String test) {
		this.roundCounter = roundCounter;
	}
	
	public String getWeaponPosition() {
		return weaponPosition;
	}

	public void setWeaponPosition(String test) {
		this.weaponPosition = weaponPosition;
	}
	
	public String getAmmuPosition() {
		return ammuPosition;
	}

	public void setAmmuPosition(String test) {
		this.ammuPosition = ammuPosition;
	}
	
	public String getPlanCBRBot() {
		return planCBRBot;
	}

	public void setPlanCBRBot(String test) {
		this.planCBRBot = planCBRBot;
	}
	
	public String getPlanScriptBot() {
		return planScriptBot;
	}

	public void setPlanScriptBot(String test) {
		this.planScriptBot = planScriptBot;
	}
	
	public String getNameCBRBot() {
		return nameCBRBot;
	}

	public void setNameCBRBot(String test) {
		this.nameCBRBot = nameCBRBot;
	}
	
	public String getNameScriptBot() {
		return nameScriptBot;
	}

	public void setNameScriptBot(String test) {
		this.nameScriptBot = nameScriptBot;
	}
	
	public String getStatisticCBRBot() {
		return statisticCBRBot;
	}

	public void setStatisticCBRBot(String test) {
		this.statisticCBRBot = statisticCBRBot;
	}
	
	public String getStatisticScriptBot() {
		return statisticScriptBot;
	}

	public void setStatisticScriptBot(String test) {
		this.statisticScriptBot = statisticScriptBot;
	}
	public String getWeaponMagAmmuCBRBot() {
		return weaponMagAmmuCBRBot;
	}

	public void setWeaponMagAmmuCBRBot(String test) {
		this.weaponMagAmmuCBRBot = weaponMagAmmuCBRBot;
	}
	
	public String getWeaponMagAmmuScriptBot() {
		return weaponMagAmmuScriptBot;
	}

	public void setWeaponMagAmmuScriptBot(String test) {
		this.weaponMagAmmuScriptBot = weaponMagAmmuScriptBot;
	}
	
	public String getWeaponCBRBot() {
		return weaponCBRBot;
	}

	public void setWeaponCBRBot(String test) {
		this.weaponCBRBot = weaponCBRBot;
	}
	
	public String getWeaponScriptBot() {
		return weaponScriptBot;
	}

	public void setWeaponScriptBot(String test) {
		this.weaponScriptBot = weaponScriptBot;
	}
	
	public String getHealthCBRBot() {
		return healthCBRBot;
	}

	public void setHealthCBRBot(String test) {
		this.healthCBRBot = healthCBRBot;
	}
	
	public String getHealthScriptBot() {
		return healthScriptBot;
	}

	public void setHealthScriptBot(String test) {
		this.healthScriptBot = healthScriptBot;
	}

	public String getCoordinatesCBRBot() {
		return coordinatesCBRBot;
	}

	public void setCoordinatesCBRBot(String test) {
		this.coordinatesCBRBot = coordinatesCBRBot;
	}
	
	public String getCoordinatesScriptBot() {
		return coordinatesScriptBot;
	}

	public void setCoordinatesScriptBot(String test) {
		this.coordinatesScriptBot = coordinatesScriptBot;
	}
	
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
	public ShooterDataRepresentation(String coordinatesCBRBot, String coordinatesScriptBot, String healthScriptBot, String healthCBRBot, String weaponScriptBot, String weaponCBRBot, String statisticCBRBot, String statisticScriptBot, String nameScriptBot, String nameCBRBot, String planCBRBot, String weaponMagAmmuCBRBot, String weaponMagAmmuScriptBot, String healthPosition, String weaponPosition, String ammuPosition, String roundCounter, String planScriptBot) {
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

	@Override
	public String toString() {
		return "StatisticsForPathViewer [coordinatesCBRBot=" + coordinatesCBRBot + "]"
	            + "[coordinatesScriptBot = " + coordinatesScriptBot + "]"
	            + "[healthScriptBot = " + healthScriptBot + "]"
				+ "[healthCBRBot = " + healthCBRBot + "]"
				+ "[weaponScriptBot = " + weaponScriptBot + "]" 
				+ "[weaponCBRBot = " + weaponCBRBot + "]"
				+ "[statisticScriptBot = " + statisticScriptBot + "]" 
				+ "[statisticCBRBot = " + statisticCBRBot + "]"
				+ "[nameScriptBot = " + nameScriptBot + "]" 
				+ "[nameCBRBot = " + nameCBRBot + "]"
				+ "[planCBRBot = " + planCBRBot + "]"
				+ "[weaponMagAmmuCBRBot = " + weaponMagAmmuCBRBot + "]" 
				+ "[weaponMagAmmuScriptBot = " + weaponMagAmmuScriptBot + "]"
				+ "[healthPosition = " + healthPosition + "]"
				+ "[weaponPosition = " + weaponPosition + "]" 
				+ "[ammuPosition = " + ammuPosition + "]"
				+ "[roundCounter = " + roundCounter + "]"
				+ "[planScriptBot = " + planScriptBot + "]";
	}
	
}
