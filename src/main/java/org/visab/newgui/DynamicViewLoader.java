package org.visab.newgui;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.eventbus.GeneralEventBus;
import org.visab.eventbus.IPublisher;
import org.visab.eventbus.event.VISABFileViewedEvent;
import org.visab.exception.DynamicException;
import org.visab.globalmodel.IVISABFile;
import org.visab.newgui.visualize.LiveStatisticsViewModelBase;
import org.visab.newgui.visualize.StatisticsViewModelBase;
import org.visab.processing.ILiveViewable;
import org.visab.processing.SessionListenerAdministration;
import org.visab.util.StringFormat;
import org.visab.workspace.Workspace;
import org.visab.workspace.config.ConfigManager;

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

        if (viewMapping != null && viewMapping.getClassPath() != null)
            className = viewMapping.getClassPath();

        if (className.isBlank()) {
            // TODO: load some standard statistics view
            return null;
        } else {
            var viewClass = (Class<? extends FxmlView<? extends ViewModel>>) tryGetClass(className);

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

    /**
     * Tries to get a Class<?> object for a given class name.
     * 
     * @param className The fully classified class name
     * @return The Class<?> object if successful, null else
     */
    private static Class<?> tryGetClass(String className) {
        Class<?> _class = null;

        if (className != null && !className.isBlank()) {
            try {
                _class = Class.forName(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();

                // If there was a class name given, but it couldent be resolved throw exception
                var message = StringFormat.niceString("Failed to find class for name {0}.", className);
                logger.fatal(message);
                throw new DynamicException(message);
            }
        }

        return _class;
    }

    @Override
    public void publish(VISABFileViewedEvent event) {
        GeneralEventBus.getInstance().publish(event);
    }

    public static void publishEvent(VISABFileViewedEvent event) {
        new GeneralEventBus().publish(event);
    }
}
