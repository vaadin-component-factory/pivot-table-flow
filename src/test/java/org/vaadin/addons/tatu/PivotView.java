package org.vaadin.addons.tatu;

import org.vaadin.addons.tatu.PivotTable.PivotData;
import org.vaadin.addons.tatu.PivotTable.PivotOptions;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

@Route("")
public class PivotView extends Div {

    public PivotView() {
        PivotData pivotData = new PivotData();
        pivotData.addColumn("color", String.class);
        pivotData.addColumn("shape", String.class);
        pivotData.addColumn("size", Double.class);
        pivotData.addRow("blue", "circle", 2d);
        pivotData.addRow("red", "triangle", 3d);
        pivotData.addRow("orange", "square", 1d);
        pivotData.addRow("yellow", "circle", 3d);
        pivotData.addRow("brown", "circle", 2d);

        PivotOptions pivotOptions = new PivotOptions();
        pivotOptions.setRows("color");
        pivotOptions.setCols("shape","size");

        PivotTable table = new PivotTable(pivotData, pivotOptions, true);
        add(table);
    }
}
