package org.visab.workspace;

import java.util.UUID;

/**
 * Represents the information when a file was saved.
 */
public class SavedFileInformation {

    private String fileName;
    private String game;
    private boolean savedByListener;
    private UUID sessionId;

    public String getFileName() {
        return this.fileName;
    }

    public String getGame() {
        return this.game;
    }

    public boolean isSavedByListener() {
        return this.savedByListener;
    }

    public UUID getSessionId() {
        return this.sessionId;
    }

    public SavedFileInformation(String fileName, String game, UUID sessionId) {
        this.fileName = fileName;
        this.game = game;
        this.sessionId = sessionId;

        savedByListener = true;
    }

    public SavedFileInformation(String fileName, String game) {
        this.fileName = fileName;
        this.game = game;
        savedByListener = false;
    }

}
