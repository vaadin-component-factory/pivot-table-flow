package org.vaadin.addons.componentfactory;

import org.vaadin.addons.componentfactory.PivotTable.Aggregator;
import org.vaadin.addons.componentfactory.PivotTable.PivotData;
import org.vaadin.addons.componentfactory.PivotTable.PivotMode;
import org.vaadin.addons.componentfactory.PivotTable.PivotOptions;
import org.vaadin.addons.componentfactory.PivotTable.Renderer;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

@Route(value = "two", layout = MainView.class)
public class TwoPivotsView extends Div {

    public TwoPivotsView() {
        PivotData pivotData = new PivotData();
        pivotData.addColumn("color", String.class);
        pivotData.addColumn("shape", String.class);
        pivotData.addColumn("size", Integer.class);
        pivotData.addColumn("filled", Boolean.class);
        pivotData.addRow("blue", "circle", 2, true);
        pivotData.addRow("red", "triangle", 3, false);
        pivotData.addRow("orange", "square", 1, true);
        pivotData.addRow("yellow", "circle", 3, false);
        pivotData.addRow("brown", "circle", 2, true);

        PivotOptions pivotOptions1 = new PivotOptions();
        pivotOptions1.setRows("color");
        pivotOptions1.setCols("shape");
        pivotOptions1.setFieldsDisabled(true);
        pivotOptions1.setRenderer(Renderer.TABLE_HEATMAP);

        PivotOptions pivotOptions2 = new PivotOptions();
        pivotOptions2.setRows("size");
        pivotOptions2.setCols("shape", "color");
        pivotOptions2.setCharts(true);
        pivotOptions2.setRenderer(Renderer.BAR_CHART);
        pivotOptions2.setAggregator(Aggregator.SUM, "size");

        PivotTable table1 = new PivotTable(pivotData, pivotOptions1,
                PivotMode.INTERACTIVE);

        PivotTable table2 = new PivotTable(pivotData, pivotOptions2,
                PivotMode.NONINTERACTIVE);
        add(table1, table2);
    }

}