package org.visab.gui;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.dynamic.DynamicHelper;
import org.visab.eventbus.GeneralEventBus;
import org.visab.eventbus.IPublisher;
import org.visab.eventbus.event.VISABFileVisualizedEvent;
import org.visab.globalmodel.IVISABFile;
import org.visab.gui.visualize.VisualizeScope;
import org.visab.gui.visualize.starter.view.DefaultStatisticsView;
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

/**
 * The DynamicViewLoader provides methods for loading visualizer views based on
 * a given game name. Views may be loaded from a transmission session or from a
 * file. As a basis for deciding which view should be loaded, the mappings
 * defined in the json configuration file are used.
 */
public final class DynamicViewLoader implements IPublisher<VISABFileVisualizedEvent> {

    private static final Logger logger = LogManager.getLogger(DynamicViewLoader.class);

    /**
     * Only used for publishing the FileVisualizedEvent event.
     */
    private static DynamicViewLoader instance = new DynamicViewLoader();

    /**
     * Loads the visualizer view for a given file.
     * 
     * @param game     The game of the file
     * @param fileName The fileName of the file. This has to be only the fileName,
     *                 paths are not supported.
     * @param file     The file to visualize
     */
    public static final void loadVisualizerView(String game, String fileName, IVISABFile file) {
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

        // Only publish if we have a fileName, since only then the file was already
        // created.
        if (fileName != null)
            instance.publish(new VISABFileVisualizedEvent(fileName, game));
    }

    /**
     * Loads the visualizer view for a given transmission session. If the
     * transmission session is active and the corresponding session listener
     * implements ILiveViewable the live view is loaded. Otherwise the regular, non
     * live, view is loaded.
     * 
     * @param game      The game of the session
     * @param sessionId The sessionId of the session
     */
    public static final void loadVisualizerView(String game, UUID sessionId) {
        var listener = SessionListenerAdministration.getSessionListener(sessionId);
        if (listener == null) {
            var file = Workspace.getInstance().getDatabaseManager().loadSessionFile(sessionId);
            var fileName = Workspace.getInstance().getDatabaseManager().getSessionFileName(sessionId);
            loadVisualizerView(game, fileName, file);
            return;
        } else if (!(listener instanceof ILiveViewable<?>)) {
            loadVisualizerView(game, null, listener.getCurrentFile());
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
            scope.setFile(listener.getCurrentFile());
        } else {
            logger.info(NiceString.make("Listener for game {0} did not implement ILiveViewable.", game));
        }

        // Resolve the main view
        var view = FluentViewLoader.fxmlView(viewClass).providedScopes(scope).load();

        showView(view, "Visualizer View", scope);
    }

    /**
     * Creates a stage and shows shows a given view.
     * 
     * @param viewTuple The viewTuple whos view to show
     * @param title     The title of the stage
     * @param scope     The VisualizationScope that is injected into the child
     *                  ViewModels of the view
     */
    private static final void showView(ViewTuple<? extends FxmlView<? extends ViewModel>, ViewModel> viewTuple,
            String title, VisualizeScope scope) {
        var parent = viewTuple.getView();
        var stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(parent));
        stage.setMinHeight(1000);
        stage.setMinWidth(1150);
        stage.getScene().getStylesheets().clear();
        stage.getScene().getStylesheets().add(Workspace.getInstance().getConfigManager().getCssPath());
        scope.setStage(stage);
        stage.show();
    }

    /**
     * Gets the Class<?> for a fully qualified view class name.
     * 
     * @param viewClassName The view class name to get the Class<?> of
     * @return The Class<?> corresponding to the className if successful, null else
     */
    @SuppressWarnings("unchecked")
    private static final Class<? extends FxmlView<? extends ViewModel>> getViewClass(String viewClassName) {
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
}
