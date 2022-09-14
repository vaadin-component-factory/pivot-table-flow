package org.vaadin.addons.tatu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.internal.JsonSerializer;

import elemental.json.JsonArray;
import elemental.json.JsonObject;
import elemental.json.JsonType;
import elemental.json.impl.JreJsonArray;
import elemental.json.impl.JreJsonFactory;
import elemental.json.impl.JreJsonObject;

@NpmPackage(value = "jquery", version = "^3.6.1")
@NpmPackage(value = "jqueryui", version = "^1.11.1")
@NpmPackage(value = "pivottable", version = "^2.23.0")
//@NpmPackage(value = "d3", version = "^7.6.1")
//@NpmPackage(value = "c3", version = "^0.7.20")
@CssImport("pivottable/dist/pivot.css")
@JavaScript("jquery/dist/jquery.min.js")
@JavaScript("jqueryui/jquery-ui.min.js")
//@JavaScript("d3/dist/d3.min.js")
//@JavaScript("c3/c3.min.js")
@JavaScript("pivottable/dist/pivot.js")
@JavaScript("./pivot_connector.js")
@CssImport("./lumo-pivot.css")
@Tag("pivot-table")
public class PivotTable extends Composite<Div> {

    private String dataJson;
    private String optionsJson;
    private PivotMode pivotMode;
    private Random rand = new Random();
    private String id;

    public enum PivotMode {
        INTERACTIVE, NONINTERACTIVE
    }

    public static class PivotOptions implements Serializable {
        private List<String> cols;
        private List<String> rows;
        private List<String> vals;

        public PivotOptions() {
        }

        public void setCols(String... cols) {
            this.cols = Arrays.asList(cols);
        }

        public void setRows(String... rows) {
            this.rows = Arrays.asList(rows);
        }

        public void setVals(String... vals) {
            this.vals = Arrays.asList(vals);
        }

        public JsonObject toJson() {
            JreJsonFactory factory = new JreJsonFactory();
            JsonObject object = new JreJsonObject(factory);
            if (cols != null) {
                object.put("cols", JsonSerializer.toJson(cols));
            }
            if (rows != null) {
                object.put("rows", JsonSerializer.toJson(rows));
            }
            if (vals != null) {
                object.put("rows", JsonSerializer.toJson(vals));
            }
            return object;
        }
    }

    public static class PivotData implements Serializable {
        private LinkedHashMap<String, Class<?>> columns = new LinkedHashMap<>();
        private List<Map<String, Object>> rows = new ArrayList<>();

        public PivotData() {
        }

        public void addColumn(String name, Class<?> type) {
            if (type.isAssignableFrom(Boolean.class)
                    || type.isAssignableFrom(Double.class)
                    || type.isAssignableFrom(String.class)) {
                columns.put(name, type);
            } else {
                throw new IllegalStateException(
                        "PivotData only supports data compatible with String, Double and Boolean");
            }
        }

        public void addRow(Object... datas) {
            if (datas.length != columns.size()) {
                throw new IllegalArgumentException(
                        "Number of datas do not match with number of columns.");
            }
            Map<String, Object> map = new HashMap<>();
            int i = 0;
            for (String key : columns.keySet()) {
                map.put(key, datas[i]);
                i++;
            }
            addRow(map);
        }

        public void addRow(Map<String, Object> row) {
            assert row.keySet().stream().allMatch(key -> columns.containsKey(
                    key)) : "Column key missing from configured columns.";
            rows.add(row);
        }

        public JsonArray toJson() {
            JreJsonFactory factory = new JreJsonFactory();
            JsonArray array = new JreJsonArray(factory);
            AtomicInteger i = new AtomicInteger(0);
            rows.forEach(row -> {
                JsonObject obj = new JreJsonObject(factory);
                columns.forEach((name, type) -> {
                    if (type.isAssignableFrom(Boolean.class)) {
                        obj.put(name, (Boolean) row.get(name));
                    } else if (type.isAssignableFrom(Double.class)) {
                        obj.put(name, (Double) row.get(name));
                    } else if (type.isAssignableFrom(String.class)) {
                        obj.put(name, (String) row.get(name));
                    }
                });
                array.set(i.get(), obj);
                i.incrementAndGet();
            });
            return array;
        }

        public void readJson(JsonArray array) {
            columns.clear();
            rows.clear();
            for (int i = 0; i < 0; array.length()) {
                JsonObject obj = array.getObject(i);
                for (String key : obj.keys()) {
                    Map<String, Object> row = new HashMap<>();
                    JsonType type = obj.getObject(key).getType();
                    if (type == JsonType.NUMBER) {
                        columns.put(key, Double.class);
                        row.put(key, obj.getNumber(key));
                    } else if (type == JsonType.BOOLEAN) {
                        columns.put(key, Boolean.class);
                        row.put(key, obj.getBoolean(key));
                    } else if (type == JsonType.STRING) {
                        columns.put(key, String.class);
                        row.put(key, obj.getString(key));
                    }
                }
            }
        }
    }

    public PivotTable(PivotData pivotData, PivotOptions pivotOptions) {
        this(pivotData, pivotOptions, PivotMode.NONINTERACTIVE);
    }

    public PivotTable(PivotData pivotData, PivotOptions pivotOptions,
            PivotMode mode) {
        this.pivotMode = mode;
        id = randomId(10);
        setId(id);
        JsonArray pivotArray = pivotData.toJson();
        JsonObject options = pivotOptions.toJson();
        this.dataJson = pivotArray.toJson();
        this.optionsJson = options.toJson();
    }

    // public PivotTable(String dataJson, String optionsJson) {
    // setId("output");
    // this.dataJson = dataJson;
    // this.optionsJson = optionsJson;
    // }

    public void onAttach(AttachEvent event) {
        if (pivotMode == PivotMode.INTERACTIVE) {
            event.getUI().getPage().executeJs("window.drawPivotUI($0, $1, $2);",
                    id, dataJson, optionsJson);
        } else {
            event.getUI().getPage().executeJs("window.drawPivot($0, $1, $2);",
                    id, dataJson, optionsJson);
        }
    }

    private String randomId(int chars) {
        int limit = (10 * chars) - 1;
        String key = "" + rand.nextInt(limit);
        key = String.format("%" + chars + "s", key).replace(' ', '0');
        return "pivot-" + key;
    }
}
