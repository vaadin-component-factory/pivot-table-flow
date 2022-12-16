package org.vaadin.addons.componentfactory;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.vaadin.addons.componentfactory.PivotTable.BeanPivotData;
import org.vaadin.addons.componentfactory.PivotTable.PivotMode;
import org.vaadin.addons.componentfactory.PivotTable.PivotOptions;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

@Route(value = "bean", layout = MainView.class)
public class BeanPivotView extends Div {

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

    public BeanPivotView() {
        Shape shape1 = new Shape("circle", "blue", 2, Fill.FILLED);
        Shape shape2 = new Shape("triangle", "red", 3, Fill.OUTLINED);
        Shape shape3 = new Shape("square", "orange", 1, Fill.FILLED);
        Shape shape4 = new Shape("circle", "yellow", 3, Fill.OUTLINED);
        Shape shape5 = new Shape("circle", "brown", 2, Fill.FILLED);

        List<Shape> list = Arrays.asList(shape1, shape2, shape3, shape4,
                shape5);

        BeanPivotData<Shape> pivotData = new BeanPivotData<>(Shape.class, list);

        PivotOptions pivotOptions = new PivotOptions();
        pivotOptions.setRows("color");
        pivotOptions.setCols("shape");
        pivotOptions.setCharts(true);

        PivotTable table = new PivotTable(pivotData, pivotOptions,
                PivotMode.INTERACTIVE);

        Button button = new Button("Dialog");
        button.addClickListener(event -> {
            if (getChildren().anyMatch(child -> child == table)) {
                remove(table);
                Dialog dialog = new Dialog();
                dialog.add(table);
                dialog.open();
            } else {
                add(table);
            }
        });

        add(button, table);
    }

}