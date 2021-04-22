package org.visab.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;


import org.visab.dataparse.AbstractParser;
import org.visab.dataparse.ShooterParser;


/**
 * 
 * This class represents a server application that gets started if VISAB operates in headless mode. 
 * By listening on a specific port it can receive data from the Unity game via TCP/IP based socket communication.
 * 
 * @author leonr
 * 
 */
public class UnityDataServer {
	
	/**
	 * The port, VISAB shall listen on for Unity Game information.
	 */
	private int port;
	/**
	 * The name of the game communicating with VISAB.
	 */
	private String game;
	/**
	 * The absolute path for the desired output directory for .visab file generation.
	 */
	private String outDir;
	
	public UnityDataServer(int port, String game, String outDir) {
		this.port = port;
		this.game = game;
		this.outDir = outDir;
		System.out.println("UnitDataServer successfully spawned for unity data transmission.");
	}

	/**
	 * This method handles the messages sent by the unity project.
	 * 
	 * @param	port	the port which is used by the unity project to send data.
	 * @throws IOException if writing to the .visab file fails.
	 */
	public void receive() throws IOException {
		
		System.out.println("Starting to receive data from unity game on port: " + this.port);
		
		String outDir = this.outDir;
		
		// Generate localized timestamp for visab file name
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM_yy_HH_mm_ss");
		String timestamp = dateFormat.format(new Date(System.currentTimeMillis()));
		
		String visabFileName = game + "_" + timestamp + ".visab";
		
		// Determine the correct parser for current game communicating
		AbstractParser parser = null;
		switch(this.game) {
			case "shooter":
				parser = new ShooterParser(outDir, visabFileName);
				parser.init();
				break;
			case "settlers":
				// TODO: Add the Settlers Parser here when time comes.
				break;
			default:
				System.err.println("Unity game provided unknown game name '" + this.game + "', cannot pick the correct parser.");
				System.exit(1);
		}
		
		
		try {
			
			// Endlessly listen on the port
			while (true) {
				
				// Reset everything
				// TODO: Check if this is really necessary or can be done more elegant
				ServerSocket serverSocket = null;
				Socket socket = null;
				
				InputStream in = null;
				// OutputStream out = null;
				
				try {
					
					// Initialize socket with port and accept communication
					serverSocket = new ServerSocket(this.port);
					socket = serverSocket.accept();
					
					// Initialize socket streams
					in = socket.getInputStream();
					// out = socket.getOutputStream();
					
					// Read JSON String
					BufferedReader inFromClient = new BufferedReader(new InputStreamReader(in));
					String jsonString = inFromClient.readLine();
					
					// Parse JSON -> .visab format
					String visabString = parser.parseJson(jsonString);
					
					parser.writeToFile(visabString);
					
					System.out.println("VISAB received JSON String: " + jsonString);
					
				} catch (IOException e) {
					System.err.println("Exception occured while reading JSON Strings from Unity game.");
					e.printStackTrace();
					parser.writeToFile(e.getMessage());
				} finally {
					try {
						// out.close();
						in.close();
						socket.close();
						serverSocket.close();
					} catch (IOException e) {
						System.err.println("Exception occured while shutting down the communication server:");
						e.printStackTrace();
					}
				}
			}
		} finally {
			parser.close();
		}
	}
}

