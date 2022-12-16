package org.vaadin.addons.componentfactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.vaadin.addons.componentfactory.PivotTable.Aggregator;
import org.vaadin.addons.componentfactory.PivotTable.BeanPivotData;
import org.vaadin.addons.componentfactory.PivotTable.JsonPivotData;
import org.vaadin.addons.componentfactory.PivotTable.PivotData;
import org.vaadin.addons.componentfactory.PivotTable.PivotMode;
import org.vaadin.addons.componentfactory.PivotTable.PivotOptions;
import org.vaadin.addons.componentfactory.PivotTable.Renderer;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

@Route(value = "mps", layout = MainView.class)
public class MpsView extends Div {

    public enum Fill {
        FILLED, OUTLINED;
    }

    public class Shape {
        private String color;
        private String shape;
        private Integer size;
        private Fill filled;
        private LocalDate date = LocalDate.now();

        public Shape(String shape, String color, Integer size, Fill filled) {
            this.color = color;
            this.shape = shape;
            this.size = size;
            this.filled = filled;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getShape() {
            return shape;
        }

        public void setShape(String shape) {
            this.shape = shape;
        }

        public Integer getSize() {
            return size;
        }

        public void setSize(Integer size) {
            this.size = size;
        }

        public Fill getFilled() {
            return filled;
        }

        public void setFilled(Fill filled) {
            this.filled = filled;
        }

        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }

    }

    public MpsView() {
        File file = null;
        String json = null;
        try {
            file = new File(MpsView.class.getClassLoader()
                    .getResource("mps.json").getFile());
            Path path = Path.of(file.getPath());
            json = Files.readString(path);
        } catch (IOException e) {
            System.out.println("No file");
        }

        JsonPivotData pivotData = new JsonPivotData(json);

        PivotOptions pivotOptions = new PivotOptions();
        pivotOptions.setRows("Party", "Gender");
        pivotOptions.setCols("Province");
        pivotOptions.setRenderer(Renderer.BAR_CHART);
        pivotOptions.setCharts(true);
        pivotOptions.setFieldsDisabled(true);
        pivotOptions.setAggregator(Aggregator.MINIMUM, "Age");

        pivotOptions.setDisabledRenderers(Renderer.HORIZONTAL_BAR_CHART,
                Renderer.HORIZONTAL_STACKED_BAR_CHART, Renderer.SCATTER_CHART,
                Renderer.AREA_CHART, Renderer.LINE_CHART);

        PivotTable table = new PivotTable(pivotData, pivotOptions,
                PivotMode.INTERACTIVE);

        add(table);
    }

}