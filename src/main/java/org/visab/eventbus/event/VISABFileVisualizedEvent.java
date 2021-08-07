package org.visab.eventbus.event;

import java.time.LocalDateTime;

import org.visab.eventbus.IEvent;

/**
 * The VISABFileViewedEvent that occurs when a VISAB file is visualized by using
 * the DynamicViewLoader.
 */
public class VISABFileVisualizedEvent implements IEvent {

    private LocalDateTime viewedDate = LocalDateTime.now();
    private String fileName;
    private String game;

    public VISABFileVisualizedEvent(String fileName, String game) {
        this.fileName = fileName;
        this.game = game;
    }

    public String getGame() {
        return game;
    }

    public String getFileName() {
        return fileName;
    }

    public LocalDateTime getViewedDate() {
        return viewedDate;
    }

}
