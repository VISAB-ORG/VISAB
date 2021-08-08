package org.visab.newgui;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.ViewModel;

/**
 * Configuration for showing views from the viewmodel via the dialog helper.
 */
public class ShowViewConfiguration {

    private boolean shouldBlock;
    private String windowTitle;
    private Class<? extends FxmlView<? extends ViewModel>> viewClass;
    private int width;
    private int height;

    /**
     * @param viewClass   The class of the view to load
     * @param stageTitle  The title of the stage
     * @param shouldBlock True if view should make other open views inaccesible
     *                    until it is closed
     */
    public ShowViewConfiguration(Class<? extends FxmlView<? extends ViewModel>> viewClass, String stageTitle,
            boolean shouldBlock) {
        this.viewClass = viewClass;
        this.windowTitle = stageTitle;
        this.shouldBlock = shouldBlock;
    }

    /**
     * 
     * @param viewClass   The class of the view to load
     * @param stageTitle  The title of the stage
     * @param shouldBlock True if view should make other open views inaccesible
     *                    until it is closed
     * @param height      The height of the stage
     * @param width       The width of the stage
     */
    public ShowViewConfiguration(Class<? extends FxmlView<? extends ViewModel>> viewClass, String windowTitle,
            boolean shouldBlock, int height, int width) {
        this(viewClass, windowTitle, shouldBlock);
        this.height = height;
        this.width = width;
    }

    /**
     * The height of the stage.
     */
    public int getHeight() {
        return height;
    }

    /**
     * The width of the stage.
     */
    public int getWidth() {
        return width;
    }

    /**
     * The class of the view to load.
     */
    public Class<? extends FxmlView<? extends ViewModel>> getViewClass() {
        return viewClass;
    }

    /**
     * The title of the stage.
     */
    public String getStageTitle() {
        return windowTitle;
    }

    /**
     * True if view should make other open views inaccesible until it is closed
     */
    public boolean shouldBlock() {
        return shouldBlock;
    }

}
