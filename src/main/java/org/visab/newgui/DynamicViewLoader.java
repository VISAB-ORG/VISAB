package org.visab.newgui;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.dynamic.DynamicHelper;
import org.visab.dynamic.DynamicInstatiator;
import org.visab.eventbus.GeneralEventBus;
import org.visab.eventbus.IPublisher;
import org.visab.eventbus.event.VISABFileViewedEvent;
import org.visab.exception.DynamicException;
import org.visab.globalmodel.IVISABFile;
import org.visab.newgui.visualize.IVisualizeViewModel;
import org.visab.newgui.visualize.LiveStatisticsViewModelBase;
import org.visab.newgui.visualize.StatisticsViewModelBase;
import org.visab.processing.ILiveViewable;
import org.visab.processing.SessionListenerAdministration;
import org.visab.util.StringFormat;
import org.visab.workspace.Workspace;
import org.visab.workspace.config.ConfigManager;
import org.visab.workspace.config.model.ViewConfig;

import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.ViewTuple;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class DynamicViewLoader implements IPublisher<VISABFileViewedEvent> {

    private static Logger logger = LogManager.getLogger(DynamicViewLoader.class);

    /**
     * Returns a ViewTupel consisting of the View aswell as the corresponding
     * ViewModel.
     * 
     * @param game The game to load a statistics view instance for
     * @return The ViewTupel if successful, throws exception else
     */
    @SuppressWarnings("unchecked")
    public static ViewTuple<? extends FxmlView<? extends ViewModel>, ? extends ViewModel> loadStatisticsViewTuple(
            String game) {
        var className = "";

        var viewMapping = Workspace.getInstance().getConfigManager().getViewMapping(ConfigManager.CBR_SHOOTER_STRING,
                "statistics");

        if (viewMapping != null && viewMapping.getViewClass() != null)
            className = viewMapping.getViewClass();

        if (className.isBlank()) {
            // TODO: load some standard statistics view
            return null;
        } else {
            var viewClass = (Class<? extends FxmlView<? extends ViewModel>>) DynamicHelper.tryGetClass(className);

            return FluentViewLoader.fxmlView(viewClass).load();
        }
    }

    /**
     * Resolves and shows a given view.
     * 
     * @param <T>       The type of the View class
     * @param viewClass The view class
     */
    public static <T extends FxmlView<? extends ViewModel>> void showView(Class<T> viewClass, String title) {
        var viewTuple = FluentViewLoader.fxmlView(viewClass).load();
        showWindow(viewTuple.getView(), title);
    }

    public static void loadAndShowStatisticsView(String game, String fileName) {
        var viewTupel = DynamicViewLoader.loadStatisticsViewTuple(game);
        var root = viewTupel.getView();
        var vM = (StatisticsViewModelBase<?>) viewTupel.getViewModel();

        var file = Workspace.getInstance().getDatabaseManager().loadFile(fileName, game);
        if (file == null) {
            logger.error("DatabaseManager return null file for filename:" + fileName);
            return;
        }

        vM.initialize(file);

        showWindow(root, "TODO: NOT LIVE");

        var event = new VISABFileViewedEvent(fileName, game);
        publishEvent(event);
    }

    /**
     * Loads and shows a live statistics view for a given game. The game is
     * initialized with the session listener with the given sessionId. If no
     * listener with the given sessionId is found, attempts to load a non live
     * statistics view.
     * 
     * @param game      The game for which to load a live statistics view
     * @param sessionId The sessionId of the listener
     */
    public static void loadAndShowStatisticsViewLive(String game, UUID sessionId) {
        var listener = SessionListenerAdministration.getSessionListener(sessionId);
        if (listener != null) {
            var viewTupel = loadStatisticsViewTuple(game);
            var root = viewTupel.getView();

            var vM = (StatisticsViewModelBase<?>) viewTupel.getViewModel();
            if (vM.supportsLiveViewing() && listener instanceof ILiveViewable<?>) {
                var liveListener = (ILiveViewable<?>) listener;
                var liveVm = (LiveStatisticsViewModelBase<?, ?>) vM;

                liveVm.initialize(liveListener);

                showWindow(root, "TODO: LIVE");
            }
        } else {
            var fileName = Workspace.getInstance().getDatabaseManager().getSessionFileName(sessionId);
            if (fileName != "")
                loadAndShowStatisticsView(game, fileName);
        }
    }

    /**
     * Creates a window by creating a new stage and adding a scene of the parent to
     * it. Then shows it.
     * 
     * @param parent
     * @param title
     */
    private static void showWindow(Parent parent, String title) {
        // TODO: Get the style here
        var stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(parent));
        stage.show();
    }

    // TODO: Change completely
    @SuppressWarnings("unchecked")
    public void loadVisualizer(String game, IVISABFile file) {
        var mapping = Workspace.getInstance().getConfigManager().getMapping(game);
        if (mapping == null || mapping.getViewConfigurations() == null) {
            // Load default statistics view
        } else {
            for (var config : mapping.getViewConfigurations()) {
                if (config == null) {
                    logger.debug("Config was null. Skipping it.");
                    continue;
                }

                // Load view class
                var viewClass = getViewClass(config);
                if (viewClass == null) {
                    logger.error(StringFormat.niceString("Failed to load View for {0} for game {1}. Skipping it.",
                            config.getIdentifier(), game));
                    continue;
                }

                // Load viewModel class
                var viewModelClass = getViewModelClass(config);
                if (viewModelClass == null) {
                    logger.error(StringFormat.niceString("Failed to load ViewModel for {0} for game {1}. Skipping it.",
                            config.getIdentifier(), game));
                    continue;
                }

                // If we are here, both viewClass and viewModelClass are valid classes.

                // Initialize the viewmodel with the file
                var object_ = DynamicInstatiator.instantiateClass(viewModelClass);
                if (object_ == null) {
                    logger.error("Failed to create instance of " + config.getViewModelClass());
                    continue;
                }

                // Initialize viewModel with file
                var viewModel = (ViewModel) object_;
                if (viewModel instanceof IVisualizeViewModel) {
                    var asVisualize = (IVisualizeViewModel) object_;
                    asVisualize.initialize(file);
                }
                // TODO: Potentially add else case here, if we want to have views that are
                // visualized based on things other than files.

                // Create the viewStep and add the viewModel instance
                var viewStep = FluentViewLoader.fxmlView(viewClass);
                viewStep.viewModel(viewModel);
                viewStep.load();
            }
        }
    }

    // TODO: Change completely
    public void loadVisualizer(String game, UUID sessionId) {

    }

    @SuppressWarnings("unchecked")
    private Class<? extends FxmlView<? extends ViewModel>> getViewClass(ViewConfig config) {
        if (config == null) {
            logger.error("Config was null.");
            return null;
        }

        if (config.getViewClass() == null || config.getViewClass().trim().equals("")) {
            logger.error("View class of config was null or empty string.");
            return null;
        }

        var viewClass = DynamicHelper.tryGetClass(config.getViewClass());
        if (viewClass == null) {
            logger.error(StringFormat.niceString("Failed to resolve class {0} as View for view identifier {1}.",
                    config.getViewClass(), config.getIdentifier()));
            return null;
        }

        // Try to cast class_ to class of FxmlView
        Class<? extends FxmlView<? extends ViewModel>> asView = null;
        try {
            asView = (Class<? extends FxmlView<? extends ViewModel>>) viewClass;
        } catch (Exception e) {
            logger.error(StringFormat.niceString(
                    "Failed to cast {0} to Class<? extends FxmlView<? extends ViewModel>>.", viewClass.getName()));
        }

        return asView;
    }

    // TODO: Throw away
    @SuppressWarnings("unchecked")
    private Class<? extends ViewModel> getViewModelClass(ViewConfig config) {
        if (config == null) {
            logger.error("Config was null.");
            return null;
        }

        if (config.getViewModelClass() == null || config.getViewModelClass().trim().equals("")) {
            logger.error("ViewModel class of config was null or empty string.");
            return null;
        }

        var viewModelClass = DynamicHelper.tryGetClass(config.getViewModelClass());
        if (viewModelClass == null) {
            logger.error(StringFormat.niceString("Failed to resolve class {0} as ViewModel for view identifier {1}.",
                    config.getViewModelClass(), config.getIdentifier()));
            return null;
        }

        // Try to cast class_ to class of FxmlView
        Class<? extends ViewModel> asViewModel = null;
        try {
            asViewModel = (Class<? extends ViewModel>) viewModelClass;
        } catch (Exception e) {
            logger.error(StringFormat.niceString("Failed to cast {0} to Class<? extends ViewModel>.",
                    viewModelClass.getName()));
        }

        return asViewModel;
    }

    @Override
    public void publish(VISABFileViewedEvent event) {
        GeneralEventBus.getInstance().publish(event);
    }

    public static void publishEvent(VISABFileViewedEvent event) {
        new GeneralEventBus().publish(event);
    }
}
