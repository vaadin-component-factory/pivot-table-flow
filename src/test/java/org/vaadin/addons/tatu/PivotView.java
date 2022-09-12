package org.vaadin.addons.tatu;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

@Route("")
public class PivotView extends Div {

    public PivotView() {
        String dataJson = "[{\"color\": \"blue\", \"shape\": \"circle\"}, {\"color\": \"red\", \"shape\": \"triangle\"}]";
        String optionsJson = "{\"rows\": [\"color\"], \"cols\": [\"shape\"]}";
        PivotTable table = new PivotTable(dataJson, optionsJson);
        add(table);
    }
}
