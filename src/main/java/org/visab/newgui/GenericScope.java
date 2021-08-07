package org.visab.newgui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import de.saxsys.mvvmfx.Scope;
import javafx.stage.Stage;

/**
 * Generic scope that can be used for injection to enable any view in the GUI to
 * have onStageClose events with Consumers as their handlers.
 * 
 * @author leonr
 *
 */
public class GenericScope implements Scope {

    private Stage stage;
    private List<Consumer<Stage>> stageClosingHandlers = new ArrayList<>();

    public void registerOnStageClosing(Consumer<Stage> closingHandler) {
        stageClosingHandlers.add(closingHandler);
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        stage.setOnCloseRequest(e -> invokeOnStageClosed(stage));
    }

    private void invokeOnStageClosed(Stage stage) {
        for (var consumer : stageClosingHandlers) {
            consumer.accept(stage);
        }
    }

}
