package org.visab.newgui.controls;

import java.util.Optional;
import java.util.function.Function;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

/**
 * Allows to set a custom label via a given function. Limitation: The value
 * returned by the function has to start with the name of the label. E.g.
 * basicLabelFormat: d.getName()
 */
public class CustomLabelPieChart extends PieChart {

    public CustomLabelPieChart() {
        super();
    }

    private Function<Data, String> basicLabelFormat = d -> d.getName() + " " + d.getPieValue();

    private Function<Data, String> labelFormat;

    private Function<Data, String> toolTipFormat;

    public void setToolTipFormat(Function<Data, String> func) {
        this.getData().addListener(new ListChangeListener<Data>() {
            @Override
            public void onChanged(Change<? extends Data> c) {
                c.next();
                if (c.wasAdded() && c.getAddedSize() == 1) {
                    var data = c.getList().get(c.getFrom());
                    // TODO: Add tooltip thingy here.
                }

            }
        });
    }

    public void setLabelFormat(Function<Data, String> func) {
        labelFormat = func;
    }

    @Override
    protected void layoutChartChildren(double top, double left, double contentWidth, double contentHeight) {
        if (getLabelsVisible()) {
            getData().forEach(d -> {
                // Data with an empty or null name is not supported
                if (d.getName() != null && !d.getName().trim().equals("")) {
                    // Sadly can only get the name property for a node like this.
                    Optional<Node> opTextNode = this.lookupAll(".chart-pie-label").stream()
                            .filter(n -> n instanceof Text && ((Text) n).getText().startsWith(d.getName())).findAny();

                    if (opTextNode.isPresent()) {
                        var node = opTextNode.get();

                        if (labelFormat != null)
                            ((Text) node).setText(labelFormat.apply(d));
                        else
                            ((Text) node).setText(basicLabelFormat.apply(d));
                    }
                }
                super.layoutChartChildren(top, left, contentWidth, contentHeight);
            });
        }
    }
}
