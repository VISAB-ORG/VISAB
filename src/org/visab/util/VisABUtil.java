package org.visab.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javafx.scene.control.TableView;
import util.Settings;

public class VisABUtil {

    private static String[] acceptedExternalDataEndings = { ".txt" };

    @SuppressWarnings("rawtypes")
    public static void clearTable(TableView statisticsTable) {
	statisticsTable.getColumns().clear();
	statisticsTable.getItems().clear();
    }

    public static List<List<String>> convertStringToList(String content) {

	// Create List to store the data
	List<List<String>> data = new ArrayList<List<String>>();

	// Filter content of file & store in HashMap
	var m = Pattern.compile("\\[(.*?)=(.*?)\\]").matcher(content);
	while (m.find()) {
	    List<String> temp = new ArrayList<String>();
	    temp.add(m.group(1));
	    temp.add(m.group(2));
	    data.add(temp);
	}

	return data;

    }

    public static final boolean gameIsSupported(String game) {
	return Settings.ALLOWED_GAMES.contains(game);
    }

    public static String[] getAcceptedExternalDataEndings() {
	return acceptedExternalDataEndings;
    }

    public static String getAcceptedExternalDataEndingsAsString() {
	var output = new StringBuilder();
	for (String acceptedExternalDataEnding : acceptedExternalDataEndings)
	    output.append(acceptedExternalDataEnding);
	return output.toString();
    }

    public static String readFile(String filePath) {
	var content = "";
	try {
	    content = new String(Files.readAllBytes(Paths.get(filePath)));
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return content;
    }

    public static int sumIntegers(int... numbers) {

	var result = 0;
	for (int number : numbers)
	    result += number;
	return result;
    }

    public static void writeFileToDatabase(String fileName, String content) {
	var filePath = Paths.get("", "data\\" + fileName);
	var parent = filePath.getParent();
	System.out.println(filePath);
	System.out.println(parent);

	BufferedWriter writer;
	try {

	    writer = new BufferedWriter(new FileWriter(filePath.toString()));
	    writer.write(content);
	    writer.close();

	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

}
