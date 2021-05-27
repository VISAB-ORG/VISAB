package org.visab.newgui;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.visab.globalmodel.IVISABFile;
import org.visab.newgui.statistics.LiveStatisticsViewModelBase;
import org.visab.newgui.statistics.StatisticsViewModelBase;
import org.visab.processing.ILiveViewable;
import org.visab.processing.SessionListenerAdministration;
import org.visab.workspace.Workspace;

import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.ViewTuple;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class DynamicViewLoader {

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
        // TODO: get classname from somewhere
        var className = "org.visab.newgui.statistics.cbrshooter.CBRShooterStatisticsView";

        if (className == null) {
            // Log
            return null;
        }

        if (className.isBlank()) {
            // TODO: load some standard statistics view
            // return FluentViewLoader.fxmlView();
            return null;
        } else {
            var _class = (Class<? extends FxmlView<? extends ViewModel>>) tryGetClass(className);

            return FluentViewLoader.fxmlView(_class).load();
        }
    }

    /**
     * Loads and shows a statistics view for the given game. The statistics view is
     * initialized with the given file.
     * 
     * @param game The game to load a statistics view for
     * @param file The file to visualize
     */
    public static void loadAndShowStatisticsView(String game, IVISABFile file) {
        var viewTupel = DynamicViewLoader.loadStatisticsViewTuple(game);
        var root = viewTupel.getView();
        var vM = (StatisticsViewModelBase<?>) viewTupel.getViewModel();

        vM.initialize(file);

        showWindow(root, "TODO: NOT LIVE");
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
            var dbManager = Workspace.getInstance().getDatabaseManager();
            var fileName = dbManager.getSessionFileName(sessionId);
            if (!fileName.isBlank()) {
                var file = Workspace.getInstance().getDatabaseManager().loadFile(fileName, game);
                if (file != null)
                    loadAndShowStatisticsView(game, file);
            }
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

        try {
            _class = Class.forName(className);
        } catch (ClassNotFoundException e) {
            logger.fatal("Failed to find class for name {0}", className);
            e.printStackTrace();
        }

        return _class;
    }
}
