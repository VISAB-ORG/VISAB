package org.visab.dynamic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.ViewTuple;

public final class DynamicViewLoader {

    private static Logger logger = LogManager.getLogger(DynamicViewLoader.class);

    /**
     * Returns a ViewTupel consisting of the View aswell as the corresponding
     * 
     * 
     * ViewModel
     * 
     * @param game The game to load a statistics view instance for
     * @return The ViewTupel if successful, throws exception else
     */
    @SuppressWarnings("unchecked")
    public static ViewTuple<? extends FxmlView<? extends ViewModel>, ? extends ViewModel> loadStatisticsView(String game) {
        // TODO: get classname from somewhere
        var className = "org.visab.newgui.statistics.CBRShooterStatisticsView";

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
     * Tries to get a Class<?> object for a given class name
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
