package util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.control.TableView;

public class VisABUtil {
	
	private static String[] acceptedExternalDataEndings = {".txt"};

	public static String[] getAcceptedExternalDataEndings() {
		return acceptedExternalDataEndings;
	}
	
	public static String getAcceptedExternalDataEndingsAsString() {
		StringBuilder output = new StringBuilder();
		for (int i = 0; i < acceptedExternalDataEndings.length; i++) {
			output.append(acceptedExternalDataEndings[i]);
		}
		return output.toString();
	}

	public static String readFile(String filePath) 
	{
	    String content = "";
	    try
	    {
	        content = new String ( Files.readAllBytes( Paths.get(filePath) ) );
	    } 
	    catch (IOException e) 
	    {
	        e.printStackTrace();
	    }
	    return content;
	}
	
	public static List<List<String>> convertStringToList(String content) {
		
		// Create List to store the data
		List<List<String>> data = new ArrayList<List<String>>();
		
		// Filter content of file & store in HashMap
		Matcher m = Pattern.compile("\\[(.*?)=(.*?)\\]").matcher(content);
		while(m.find()) {
			List<String> temp = new ArrayList<String>();
			temp.add(m.group(1));
			temp.add(m.group(2));
			data.add(temp);
		}
		
		return data;
		
	}
	
	public static void writeFileToDatabase(String fileName, String content) {
		Path filePath = Paths.get("", "data\\" + fileName);	
		Path parent = filePath.getParent();
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
	
	@SuppressWarnings("rawtypes")
	public static void clearTable( TableView statisticsTable) {
		statisticsTable.getColumns().clear();
		statisticsTable.getItems().clear();
	}
	
	public static int sumIntegers(int...numbers){

	    int result = 0;
	    for(int i = 0 ; i < numbers.length; i++) {
	        result += numbers[i];
	    } 
	    return result;
	}

}
