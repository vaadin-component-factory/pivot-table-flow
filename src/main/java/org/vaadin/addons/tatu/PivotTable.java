package org.vaadin.addons.tatu;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;

@NpmPackage(value = "jquery", version = "3.6.1")
@NpmPackage(value = "jqueryui", version = "1.11.1")
@NpmPackage(value = "pivottable", version = "2.23.0")
@CssImport("pivottable/dist/pivot.css")
@JavaScript("jquery/dist/jquery.min.js")
@JavaScript("jqueryui/jquery-ui.min.js")
@JavaScript("pivottable/dist/pivot.js")
@JavaScript("./pivot_connector.js")
public class PivotTable extends Div {

    private String dataJson;
	private String optionsJson;

	public PivotTable(String dataJson, String optionsJson) {
        setId("output");
        this.dataJson = dataJson;
        this.optionsJson = optionsJson;
    }

    public void onAttach(AttachEvent event) {
        event.getUI().getPage().executeJs("window.drawPivotUI($0, $1);", dataJson, optionsJson);        
    }
}
