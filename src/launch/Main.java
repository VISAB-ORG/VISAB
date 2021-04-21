package launch;

import application.GUIMain;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

/**
 * Main class of the VISAB application that is responsible for parsing the command line arguments 
 * and activate the respective mode that shall be started.
 * 
 * @author leonr
 *
 */
public class Main {
	
	@Option(name = "-mode", usage = "The mode you want to execute VISAB in: { 'gui' | 'headless' }", required=true)     
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
            	System.out.println("Valid execution mode "+ "'" + mode + "' for VISAB detected");
            } else {
            	System.err.println("Invalid execution mode "+ "' " + mode + "' for VISAB detected");
            	parser.printUsage(System.err);
            	System.exit(1);
            }

        } catch( CmdLineException e ) {
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
        	System.out.println("Starting VISAB as a headless server application.");
        	// TODO
        }
	}

}
