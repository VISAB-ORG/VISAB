package org.visab.dynamic;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.ViewTuple;

public class DynamicViewLoader {

    /**
     * Singelton instance
     */
    public static final DynamicViewLoader instance = new DynamicViewLoader();

    public <T extends FxmlView<TViewModel>, TViewModel extends ViewModel> ViewTuple<T, TViewModel> loadStatisticsView() {
        return null;
    }

}
