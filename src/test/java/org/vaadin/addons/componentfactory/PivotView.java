package org.vaadin.addons.componentfactory;

import org.vaadin.addons.componentfactory.PivotTable.PivotData;
import org.vaadin.addons.componentfactory.PivotTable.PivotMode;
import org.vaadin.addons.componentfactory.PivotTable.PivotOptions;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

@Route("")
public class PivotView extends Div {

    public PivotView() {
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

        PivotOptions pivotOptions = new PivotOptions();
        pivotOptions.setRows("color");
        pivotOptions.setCols("shape");
        pivotOptions.setCharts(true);

        PivotTable table = new PivotTable(pivotData, pivotOptions,
                PivotMode.INTERACTIVE);

        Button button = new Button("to Dialog");
        button.addClickListener(event -> {
            if (getChildren().anyMatch(child -> child == table)) {
                remove(table);
                button.setText("to Normal");
                Dialog dialog = new Dialog();
                dialog.add(table);
                dialog.open();
            } else {
                button.setText("to Dialog");
                add(table);
            }
        });
        
        add(button, table);
    }

}