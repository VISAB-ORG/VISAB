package org.visab.processing.cbrshooter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.UUID;

import org.visab.processing.SessionListenerBase;
import org.visab.processing.cbrshooter.model.CBRShooterStatistics;
import org.visab.util.JsonSerializer;
import org.visab.util.VISABUtil;

/**
 * The CBRShooterListener class, that is responsible for listening information
 * sent by the CBRShooter game and creating files of that information.
 *
 * @author moritz
 *
 */
public class CBRShooterListener extends SessionListenerBase<CBRShooterStatistics> {

	private CBRShooterFile CBRShooterFile;
	private String fileName;
	private String outDir;

	public CBRShooterListener(UUID sessionId) {
		super("CBRShooter", sessionId);
	}

	@Override
	public void onSessionClosed() {
		var json = JsonSerializer.serializeObject(CBRShooterFile);

		var fullFilePath = new File(outDir + fileName);

		try (var fileWriter = new FileWriter(fullFilePath)) {
			fileWriter.write(json);
			System.out.println("Saved file @" + fullFilePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onSessionStarted() {
		CBRShooterFile = new CBRShooterFile();

		outDir = VISABUtil.getRunningJarRootDirPath() + "/visab_files/CBRShooter/";
		// fileName = CBRShooterFile.getCreationDate().toString() + ".txt";
		fileName = getSessionId() + ".visab2";

		var directory = new File(outDir);
		if (!directory.exists())
			directory.mkdirs();
	}

	@Override
	public void processStatistics(CBRShooterStatistics statistics) {
		CBRShooterFile.getStatistics().add(statistics);

		System.out.println(MessageFormat.format("[Game: {0}, SessionId: {1}] has {2} entries now.", getGame(),
				getSessionId(), CBRShooterFile.getStatistics().size()));

		// TODO: Do a save when list has large amount of entries
		// TODO: Since it may be hard to detect when a unity game closes, instead keep a
		// filewriter open and write on every received.
		// This should be done asynchronously
	}

}
