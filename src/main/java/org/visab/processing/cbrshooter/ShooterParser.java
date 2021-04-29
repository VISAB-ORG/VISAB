package org.visab.processing.cbrshooter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.visab.processing.AbstractParser;
import org.visab.processing.cbrshooter.model.ShooterDataRepresentation;

/**
 * This class is responsible for parsing JSON data from Unity into visab-format.
 * It uses shooter-specific models to ensure proper game-specific handling.
 *
 * @author leonr
 *
 */
public class ShooterParser extends AbstractParser {

    public ShooterParser(String outDir, String visabFileName) {
	super(outDir, visabFileName);
    }

    @Override
    public void close() throws IOException {
	try {
	    this.writer.flush();
	    this.writer.close();
	} catch (IOException e) {
	    System.err.println("Exception occured while closing the output file writer.");
	    e.printStackTrace();
	}
    }

    @Override
    public void init() throws IOException {
	System.out.println("Output file for current run: " + this.outDir.concat("\\" + visabFileName));
	var directory = new File(this.outDir);

	// Create output directory if necessary
	if (!directory.exists())
	    directory.mkdirs();

	var outFile = new File(outDir.concat("/" + this.visabFileName));
	this.writer = new FileWriter(outFile);

    }

    @Override
    public String parseJson(String jsonString) {
	var shooterDataRepresentation = this.gson.fromJson(jsonString, ShooterDataRepresentation.class);

	// return the visa-format String
	return shooterDataRepresentation.toString();
    }

    @Override
    public void writeToFile(String visabString) throws IOException {
	try {
	    this.writer.write(visabString);
	    this.writer.write("\r\n");
	} catch (IOException e) {
	    System.err.println("Exception occured while writing to the .visab output file.");
	    e.printStackTrace();
	}
    }

}
