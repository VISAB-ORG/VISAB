package org.visab.eventbus.event;

import java.time.LocalDateTime;

import org.visab.eventbus.IEvent;

public class VISABFileViewedEvent implements IEvent {

    private LocalDateTime viewedDate = LocalDateTime.now();

    private String fileName;

    private String game;

    public VISABFileViewedEvent(String fileName, String game) {
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
