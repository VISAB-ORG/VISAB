package org.visab.main;

import java.io.IOException;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.visab.communication.UnityDataServer;
import org.visab.gui.GUIMain;

/**
 * Main class of the VISAB application that is responsible for parsing the command line arguments 
 * and activate the respective mode that shall be started.
 * 
 * @author leonr
 *
 */
public class Main {
	
	@Option(name = "-mode", usage = "The mode you want to execute VISAB in: { 'gui' | 'headless' }.", required=true)     
    private static String mode;
	
	@Option(name = "-port", usage = "The port, VISAB shall listen on for Unity Game information.", required=false)     
    private static int port;
	
	@Option(name = "-game", usage = "The name of the game communicating with VISAB.", required=false)     
    private static String game;
	
	@Option(name = "-out", usage = "The absolute path for the desired output directory for .visab file generation.", required=false)     
    private static String out;


	public static void main(String[] args) throws Exception {
		new Main().doMain(args);
	}
	
	public void doMain(String[] args) throws Exception {
		CmdLineParser parser = new CmdLineParser(this);
       
        try {
            parser.parseArgument(args);

            // Validate input and print information
            if (mode.equals("gui") || mode.equals("headless")) {
            	System.out.println("Valid execution mode "+ "'" + mode + "' for VISAB detected");
            } else {
            	System.err.println("Invalid execution mode "+ "' " + mode + "' for VISAB detected");
            	parser.printUsage(System.err);
            	System.exit(1);
            }

        } catch(CmdLineException e ) {
        	System.err.println("Exception occured while parsing command line arguments...");
            System.err.println(e.getMessage());
            // print the list of available options
            parser.printUsage(System.err);

            System.exit(1);
        }
		
        if (mode.equals("gui")) {
        	System.out.println("Starting VISAB as a GUI application.");
        	// Start the VISAB GUI as a new thread
        	new Thread() {
                @Override
                public void run() {
                    javafx.application.Application.launch(GUIMain.class);
                }
            }.start();
        } else {
        	System.out.println("Starting VISAB as a headless server application ...");
        	if (port == 0) {
        		System.err.println("No port provided, VISAB cannot start in headless server mode.");
        		System.exit(1);
        	} else if (game == null) {
        		System.err.println("No game name provided, VISAB cannot start in headless server mode.");
        		System.exit(1);
        	} else if (out == null) {
        		System.err.println("No output directory provided, VISAB cannot start in headless server mode.");
        		System.exit(1);
        	} else {
        		System.out.println("... on port: " + port + " for game: " + game + " ...");
        		UnityDataServer unityDataServer = new UnityDataServer(port, game, out);
        		
        		new Thread(new Runnable() {

        			@Override
        			public void run() {
        				while (true) {
        					try {
								unityDataServer.receive();
							} catch (IOException e) {
								System.err.println("Error occured while listening for Unity game data.");
								e.printStackTrace();
							}
        				}
        			}
      			}).start();
        		
        	}
        }
	}

}
