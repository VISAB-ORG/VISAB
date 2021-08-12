package org.visab.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import de.saxsys.mvvmfx.Scope;
import javafx.stage.Stage;

/**
 * General scope that can be injected into viewmodels whose views are loaded by
 * the DialogHelper. Its purpose is to provide access to events of the stage
 * that the view is living in. Currenlty used only for reacting to the
 * OnStageClosing event.
 * 
 * @author leonr
 *
 */
public class GeneralScope implements Scope {

    private Stage stage;
    private List<Consumer<Stage>> stageClosingHandlers = new ArrayList<>();

    /**
     * Registers a handler for the onStageClosing event of the stage. The handler
     * will be invoked, when the OnStageClosing event occurs.
     * 
     * @param closingHandler The handler to register
     */
    public void registerOnStageClosing(Consumer<Stage> closingHandler) {
        stageClosingHandlers.add(closingHandler);
    }

    /**
     * The stage of the view.
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Sets the stage of the view. This should only be called by the DialogHelper.
     * 
     * @param stage The stage of the view
     */
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
