package org.visab.dataparse;

import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;

/**
 * Abstract parser class that can be extended by any game-specific parser to handle the full functionality of 
 * transforming data received by the unity games into VISAB-conform file format.
 * 
 * @author leonr
 *
 */
public abstract class AbstractParser {
	
	/**
	 * The output directory to produce the .visab file in.
	 */
	protected String outDir;
	
	/**
	 * The file name that shall be used for the .visab output.
	 */
	protected String visabFileName;
	
	/**
	 * The FileWriter that gets used to procude the .visab file.
	 */
	protected FileWriter writer;
	
	/**
	 * The Gson object responsible for deserializing the JSON String into the respective JAVA representation.
	 */
	protected Gson gson;
	
	public AbstractParser(String outDir, String visabFileName) {
		this.outDir = outDir;
		this.visabFileName = visabFileName;
		this.gson = new Gson();
	}
	
	/**
	 * Responsible for initializing the FileWriter with the output directory.
	 * 
	 * @throws	IOException	if initializing the FileWriter fails.
	 */
	public abstract void init() throws IOException;
	
	/**
	 * Responsible for parsing the received JSON string into .visab format.
	 * 
	 * @param	jsonString	the received String from unity.
	 * @return	the .visab format String.
	 */
	public abstract String parseJson(String jsonString);
	
	/**
	 * Responsible for writing the parsed data to the correct output file.
	 * 
	 * @param	visabString	the .visab formatted String.
	 * @throws	IOException	writing to .visab file fails.
	 */
	public abstract void writeToFile(String visabString) throws IOException;
	
	/**
	 * Responsible for closing the output stream and flushing the content to the file.
	 * @throws	IOException	if closing the output stream fails.
	 */
	public abstract void close() throws IOException;
}
