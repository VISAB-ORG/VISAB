package org.visab.newgui;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.dynamic.DynamicHelper;
import org.visab.eventbus.GeneralEventBus;
import org.visab.eventbus.IPublisher;
import org.visab.eventbus.event.VISABFileVisualizedEvent;
import org.visab.globalmodel.IVISABFile;
import org.visab.newgui.visualize.VisualizeScope;
import org.visab.newgui.visualize.starter.view.DefaultStatisticsView;
import org.visab.processing.ILiveViewable;
import org.visab.processing.SessionListenerAdministration;
import org.visab.util.NiceString;
import org.visab.workspace.Workspace;

import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.ViewTuple;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class DynamicViewLoader implements IPublisher<VISABFileVisualizedEvent> {

    private static Logger logger = LogManager.getLogger(DynamicViewLoader.class);

    public static void loadVisualizer(String game, IVISABFile file) {
        if (file == null) {
            logger.fatal("Given file was null!");
            return;
        }

        String viewClassName;
        var mapping = Workspace.getInstance().getConfigManager().getMapping(game);
        if (mapping == null || mapping.getVisualizer() == null || mapping.getVisualizer().isBlank()) {
            // Load default statistics view
            viewClassName = DefaultStatisticsView.class.getName();
        } else {
            viewClassName = mapping.getVisualizer();
        }

        // Load main view class
        var viewClass = getViewClass(viewClassName);
        if (viewClass == null) {
            logger.error(NiceString.make("Failed to load Visualizer View for for game {0}.", game));
        }

        // Create new scope instance that will be injected in all the view types for
        // visualization
        var scope = new VisualizeScope();
        scope.setFile(file);
        scope.setLive(false);

        // Resolve the main view
        var view = FluentViewLoader.fxmlView(viewClass).providedScopes(scope).load();

        showView(view, "Visualizer View", scope);
    }

    public static void loadVisualizer(String game, UUID sessionId) {
        var listener = SessionListenerAdministration.getSessionListener(sessionId);
        if (listener == null) {
            var file = Workspace.getInstance().getDatabaseManager().loadSessionFile(sessionId);
            loadVisualizer(game, file);
            return;
        } else if (!(listener instanceof ILiveViewable<?>)) {
            loadVisualizer(game, listener.getCurrentFile());
            return;
        }

        var mapping = Workspace.getInstance().getConfigManager().getMapping(game);
        var viewClassName = mapping.getVisualizer();

        // Load main view class
        var viewClass = getViewClass(viewClassName);
        if (viewClass == null) {
            logger.error(NiceString.make("Failed to load Visualizer View for for game {0}. Skipping it.", game));
        }

        // Create new scope instance that will be injected in all the view types for
        // visualization
        var scope = new VisualizeScope();
        if (listener instanceof ILiveViewable<?>) {
            var asLiveViewable = (ILiveViewable<?>) listener;
            scope.setSessionListener(asLiveViewable);
            scope.setLive(true);
        } else {
            logger.info(NiceString.make("Listener for game {0} did not implement ILiveViewable.", game));
        }

        // Resolve the main view
        var view = FluentViewLoader.fxmlView(viewClass).providedScopes(scope).load();

        showView(view, "Visualizer View", scope);
    }

    /**
     * TODO: Add parameters for blocking or not blocking etc.
     * 
     * @param viewTuple
     * @param title
     */
    private static void showView(ViewTuple<? extends FxmlView<? extends ViewModel>, ViewModel> viewTuple, String title,
            VisualizeScope scope) {
        var parent = viewTuple.getView();
        var stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(parent));
        stage.setMinHeight(1000);
        stage.setMinWidth(1150);
        scope.setStage(stage);
        stage.setOnCloseRequest(e -> scope.invokeOnStageClosed(stage));
        stage.show();
    }

    @SuppressWarnings("unchecked")
    private static Class<? extends FxmlView<? extends ViewModel>> getViewClass(String viewClassName) {
        if (viewClassName == null) {
            logger.error("ViewClass was null.");
            return null;
        }

        if (viewClassName.isBlank()) {
            logger.error("ViewClass was empty string.");
            return null;
        }

        var class_ = DynamicHelper.tryGetClass(viewClassName);
        if (class_ == null) {
            logger.error(NiceString.make("Failed to resolve class {0}.", viewClassName));
            return null;
        }

        // Try to cast class_ to class of FxmlView
        Class<? extends FxmlView<? extends ViewModel>> asView = null;
        try {
            asView = (Class<? extends FxmlView<? extends ViewModel>>) class_;
        } catch (Exception e) {
            logger.error(NiceString.make("Failed to cast {0} to Class<? extends FxmlView<? extends ViewModel>>.",
                    viewClassName));
        }

        return asView;
    }

    @Override
    public void publish(VISABFileVisualizedEvent event) {
        GeneralEventBus.getInstance().publish(event);
    }

    public static void publishEvent(VISABFileVisualizedEvent event) {
        new GeneralEventBus().publish(event);
    }
}
