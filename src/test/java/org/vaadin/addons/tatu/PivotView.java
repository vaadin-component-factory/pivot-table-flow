package org.vaadin.addons.tatu;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

@Route("")
public class PivotView extends Div {

    public PivotView() {
        String json = "["
                + "{color: \"blue\", shape: \"circle\"},"
                + "{color: \"red\", shape: \"triangle\"}"
                + "],"
                + "{"
                + "rows: [\"color\"],"
                + "cols: [\"shape\"]"
                + "}";

        PivotTable table = new PivotTable(json);
        add(table);
    }
}
