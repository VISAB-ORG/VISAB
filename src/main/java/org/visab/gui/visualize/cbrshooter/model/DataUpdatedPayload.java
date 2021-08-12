package org.visab.gui.visualize.cbrshooter.model;

public class DataUpdatedPayload {
    private int newFrame;
    private int oldRound;
    private int newRound;
    private int oldFrame;

    public DataUpdatedPayload(int oldFrame, int newFrame, int oldRound, int newRound) {
        this.setOldFrame(oldFrame);
        this.newFrame = newFrame;
        this.oldRound = oldRound;
        this.newRound = newRound;
    }

    public int getOldFrame() {
        return oldFrame;
    }

    public void setOldFrame(int oldFrame) {
        this.oldFrame = oldFrame;
    }

    public int getNewFrame() {
        return newFrame;
    }

    public int getNewRound() {
        return newRound;
    }

    public void setNewRound(int newRound) {
        this.newRound = newRound;
    }

    public int getOldRound() {
        return oldRound;
    }

    public void setOldRound(int oldRound) {
        this.oldRound = oldRound;
    }

    public void setNewFrame(int newFrame) {
        this.newFrame = newFrame;
    }
}
