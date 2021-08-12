package org.visab.gui.control;

import java.util.Optional;
import java.util.function.Function;

import javafx.scene.Node;
import javafx.scene.chart.PieChart;
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
