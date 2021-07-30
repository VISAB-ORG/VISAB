package org.visab.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.visab.api.WebAPI;
import org.visab.newgui.AppMain;

import javafx.application.Application;

/**
 * Main class of the VISAB application that is responsible for parsing the
 * command line arguments and activate the respective mode that shall be
 * started.
 * 
 * @author leonr
 *
 */
public class Main {

    // Logger needs .class for each class to use for log traces
    private static Logger logger = LogManager.getLogger(Main.class);

    @Option(name = "-mode", usage = "The mode you want to execute VISAB in: { 'gui' | 'headless' }.", required = true)
    private static String mode;

    public static void main(String[] args) throws Exception {
        new Main().doMain(args);
    }

    public void doMain(String[] args) throws Exception {
        CmdLineParser parser = new CmdLineParser(this);

        try {
            parser.parseArgument(args);

            // Validate input and print information
            if (mode.equals("gui") || mode.equals("headless")) {
                logger.info("Valid execution mode " + "'" + mode + "' for VISAB detected");
            } else {
                logger.error("Invalid execution mode " + "' " + mode + "' for VISAB detected");
                parser.printUsage(System.err);
                System.exit(1);
            }

        } catch (CmdLineException e) {
            logger.error("CAUGHT [" + e + "] while parsing command line arguments - stacktrace:");
            logger.error(e.getStackTrace().toString());

            // print the list of available options
            parser.printUsage(System.err);

            System.exit(1);
        }

        logger.info("Starting VISAB API HTTP server ...");
        WebAPI.getInstance().start();

        // Start the GUI additionally if desired
        if (mode.equals("gui")) {
            logger.info("Starting VISAB as a GUI application.");
            new Thread(() -> Application.launch(AppMain.class)).start();

        }
    }

}