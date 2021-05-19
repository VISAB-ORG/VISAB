package org.visab.processing.cbrshooter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    // Logger needs .class for each class to use for log traces
    private static Logger logger = LogManager.getLogger(ShooterParser.class);

    public ShooterParser(String outDir, String visabFileName) {
        super(outDir, visabFileName);
    }

    @Override
    public void close() throws IOException {
        try {
            this.writer.flush();
            this.writer.close();
        } catch (IOException e) {
            logger.error("CAUGHT [" + e + "] while closing the file writer - stacktrace:");
            logger.error(e.getStackTrace().toString());
        }
    }

    @Override
    public void init() throws IOException {
        logger.debug("Output file for current run: " + this.outDir.concat("\\" + visabFileName));
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
            logger.error("CAUGHT [" + e + "] while writing to .visab output file - stacktrace:");
            logger.error(e.getStackTrace().toString());
        }
    }

}
