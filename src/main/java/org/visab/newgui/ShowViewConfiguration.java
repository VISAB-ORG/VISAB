package org.visab.newgui;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.ViewModel;

public class ShowViewConfiguration {

    private boolean shouldBlock;
    private String windowTitle;
    private Class<? extends FxmlView<? extends ViewModel>> viewType;
    private int width;
    private int height;

    public ShowViewConfiguration(Class<? extends FxmlView<? extends ViewModel>> viewType, String windowTitle,
            boolean shouldBlock) {
        this.viewType = viewType;
        this.windowTitle = windowTitle;
        this.shouldBlock = shouldBlock;
    }

    public ShowViewConfiguration(Class<? extends FxmlView<? extends ViewModel>> viewType, String windowTitle,
            boolean shouldBlock, int height, int width) {
        this(viewType, windowTitle, shouldBlock);
        this.height = height;
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Class<? extends FxmlView<? extends ViewModel>> getViewType() {
        return viewType;
    }

    public String getWindowTitle() {
        return windowTitle;
    }

    public boolean shouldBlock() {
        return shouldBlock;
    }

}
