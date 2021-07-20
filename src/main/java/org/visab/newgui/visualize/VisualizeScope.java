package org.visab.newgui.visualize;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import org.visab.globalmodel.IVISABFile;
import org.visab.processing.ILiveViewable;

import de.saxsys.mvvmfx.Scope;
import javafx.stage.Stage;

public class VisualizeScope implements Scope {

    private Stage stage;
    private boolean isLive;
    private IVISABFile file;
    private ILiveViewable<?> sessionListener;
    private List<Consumer<Stage>> stageClosingHandlers = new ArrayList<>();

    public void setFile(IVISABFile file) {
        this.file = file;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean isLive) {
        this.isLive = isLive;
    }

    public ILiveViewable<?> getSessionListener() {
        return sessionListener;
    }

    public void setSessionListener(ILiveViewable<?> sessionListener) {
        this.sessionListener = sessionListener;
    }

    public IVISABFile getFile() {
        return file;
    }

    public void registerOnStageClosing(Consumer<Stage> closingHandler) {
        stageClosingHandlers.add(closingHandler);
    }

    public void invokeOnStageClosed(Stage stage) {
        for (var consumer : stageClosingHandlers) {
            consumer.accept(stage);
        }
    }

}
