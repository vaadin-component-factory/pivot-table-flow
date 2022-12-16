package org.vaadin.addons.componentfactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.data.binder.BeanPropertySet;
import com.vaadin.flow.data.binder.PropertySet;
import com.vaadin.flow.internal.JsonSerializer;

import elemental.json.JsonArray;
import elemental.json.JsonObject;
import elemental.json.impl.JreJsonArray;
import elemental.json.impl.JreJsonFactory;
import elemental.json.impl.JreJsonObject;

@StyleSheet("context://c3/c3.min.css")
@StyleSheet("context://pivottable/dist/pivot.css")
@JavaScript("context://jquery/dist/jquery.min.js")
@JavaScript("context://jqueryui/jquery-ui.min.js")
@JavaScript("context://d3/build/d3.min.js")
@JavaScript("context://c3/c3.min.js")
@JavaScript("context://pivottable/dist/pivot.min.js")
@JavaScript("context://pivottable/dist/c3_renderers.min.js")
@JavaScript("context://pivottable/dist/export_renderers.min.js")
@JavaScript("./pivot_connector.js")
@CssImport("./lumo-pivot.css")
/**
 * PivotTable is component based on pivottable.js. This component performs
 * pivoting of the dataset in the browser. Thus it is suitable for small
 * datasets which do not require lazy loading from the backend.
 */
public class PivotTable extends Composite<Div> {

    private String dataJson;
    private String optionsJson;
    private PivotMode pivotMode;
    private Random rand = new Random();
    private String id;
    private PivotOptions options;

    /**
     * The mode, PivotMode.INTERACTIVE renders with Pivot UI.
     */
    public enum PivotMode {
        INTERACTIVE, NONINTERACTIVE
    }

    /**
     * Utility helpers for valid renderer strings.
     */
    public final class Renderer {
        public static final String TABLE = "Table";
        public static final String TABLE_BARCHART = "Table Barchart";
        public static final String TABLE_HEATMAP = "Heatmap";
        public static final String ROW_HEATMAP = "Row Heatmap";
        public static final String COL_HEATMAP = "Col Heatmap";
        public static final String HORIZONTAL_BAR_CHART = "Horizontal Bar Chart";
        public static final String HORIZONTAL_STACKED_BAR_CHART = "Horizontal Stacked Bar Chart";
        public static final String BAR_CHART = "Bar Chart";
        public static final String STACKED_BAR_CHART = "Stacked Bar Chart";
        public static final String LINE_CHART = "Line Chart";
        public static final String AREA_CHART = "Area Chart";
        public static final String SCATTER_CHART = "Scatter Chart";
        public static final String TSV_EXPORT = "TSV Export";
    }

    /**
     * Utility helpers for valid renderer strings.
     */
    public final class Aggregator {
        public static final String COUNT = "Count";
        public static final String COUNT_UNIQUE_VALUES = "Count Unique Values";
        public static final String LIST_UNIQUE_VALUES = "List Unique Values";
        public static final String SUM = "Sum";
        public static final String INTEGER_SUM = "Integer Sum";
        public static final String AVERAGE = "Average";
        public static final String MEDIAN = "Median";
        public static final String SAMPLE_VARIANCE = "Sample Variance";
        public static final String SAMPLE_STANDARD_DEVIATION = "Sample Standard Deviation";
        public static final String MINIMUM = "Minimum";
        public static final String MAXIMUM = "Maximum";
        public static final String FIRST = "First";
        public static final String LAST = "Last";
    }

    /**
     * Options for PivotTable
     */
    public static class PivotOptions implements Serializable {
        List<String> cols;
        List<String> rows;
        List<String> disabledRerenders;
        String renderer;
        String aggregator;
        String column;
        boolean charts;
        boolean fieldsDisabled;

        public PivotOptions() {
        }

        /**
         * Set default columns for the pivot
         * 
         * @param cols
         *            Column identifiers
         */
        public void setCols(String... cols) {
            this.cols = Arrays.asList(cols);
        }

        /**
         * Set default rows for the pivot.
         * 
         * @param rows
         *            Row identifiers
         */
        public void setRows(String... rows) {
            this.rows = Arrays.asList(rows);
        }

        /**
         * Set disabled renderers.
         *
         * @see Renderer
         * 
         * @param renderers
         *            Renderers to disable.
         */
        public void setDisabledRenderers(String... renderers) {
            this.disabledRerenders = Arrays.asList(renderers);
        }

        /**
         * Set the default renderer.
         *
         * @see Renderer
         *
         * @param renderer
         *            The renderer name.
         */
        public void setRenderer(String renderer) {
            this.renderer = renderer;
        }

        /**
         * Set the default aggregator.
         *
         * @see Aggregator
         *
         * @param aggregator
         *            The aggregator name.
         * @param column
         *            The column name. Can be null.
         */
        public void setAggregator(String aggregator, String column) {
            this.aggregator = aggregator;
            this.column = column;
        }

        /**
         * When false fields cannot be rearranged.
         * 
         * @param fieldsDisabled
         *            Boolean value.
         */
        public void setFieldsDisabled(boolean fieldsDisabled) {
            this.fieldsDisabled = fieldsDisabled;
        }

        /**
         * Enable embbeded charts.
         * 
         * @param charts
         *            true for charts enabled.
         */
        public void setCharts(boolean charts) {
            this.charts = charts;
        }

        JsonObject toJson() {
            JreJsonFactory factory = new JreJsonFactory();
            JsonObject object = new JreJsonObject(factory);
            if (cols != null) {
                object.put("cols", JsonSerializer.toJson(cols));
            }
            if (rows != null) {
                object.put("rows", JsonSerializer.toJson(rows));
            }
            return object;
        }
    }

    /**
     * Abastract base class for Pivot data models.
     */
    public static abstract class AbstractPivotData implements Serializable {

        LinkedHashMap<String, Class<?>> columns = new LinkedHashMap<>();
        List<Map<String, Object>> rows = new ArrayList<>();

        /**
         * Add column.
         * 
         * @param name
         *            Name of the column, unique.
         * @param type
         *            Data type used in the column
         */
        public void addColumn(String name, Class<?> type) {
            if (type.isAssignableFrom(Boolean.class)
                    || type.isAssignableFrom(Double.class)
                    || type.isAssignableFrom(Integer.class)
                    || type.isAssignableFrom(String.class)) {
                columns.put(name, type);
            } else {
                columns.put(name, String.class);
            }
        }

        /**
         * Add row from the map.
         * 
         * @param row
         *            Map of column key data object pairs.
         */
        public void addRow(Map<String, Object> row) {
            assert row.keySet().stream().allMatch(key -> columns.containsKey(
                    key)) : "Column key missing from configured columns.";
            rows.add(row);
        }

        String toJson() {
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
                    } else if (type.isAssignableFrom(Integer.class)) {
                        obj.put(name, (Double) Double
                                .valueOf((Integer) row.get(name)));
                    } else if (type.isAssignableFrom(String.class)) {
                        obj.put(name, (String) row.get(name).toString());
                    } else {
                        obj.put(name, (String) row.get(name).toString());
                    }
                });
                array.set(i.get(), obj);
                i.incrementAndGet();
            });
            return array.toJson();
        }
    }

    /**
     * Pivot dataa model that auto creates based on list of beans using
     * introspection.
     * <p>
     * Note: Bean properties need to be compatible with Integer, Double, Boolean
     * to be considered either number or boolean on the client side Other types
     * are converted to String using object.toString.
     */
    public static class BeanPivotData<T> extends AbstractPivotData {

        PropertySet<T> propertySet;

        public BeanPivotData(Class<T> beanType, Collection<T> data) {
            propertySet = BeanPropertySet.get(beanType);
            propertySet.getProperties()
                    .filter(property -> !property.isSubProperty())
                    .sorted((prop1, prop2) -> prop1.getName()
                            .compareTo(prop2.getName()))
                    .forEach(prop -> addColumn(prop.getName(), prop.getType()));
            data.forEach(item -> {
                HashMap<String, Object> map = new HashMap<>();
                propertySet.getProperties()
                        .filter(property -> !property.isSubProperty())
                        .sorted((prop1, prop2) -> prop1.getName()
                                .compareTo(prop2.getName()))
                        .forEach(prop -> map.put(prop.getName(),
                                prop.getGetter().apply(item)));
                addRow(map);
            });
        }
    }

    /**
     * Data model for PivotTable. Columns need to be configured first, then add
     * rows.
     */
    public static class PivotData extends AbstractPivotData {

        public PivotData() {
        }

        /**
         * Add a row of data objects.
         * 
         * @param datas
         *            Data objects in the same order as columns were added.
         */
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
    }

    public static class JsonPivotData extends AbstractPivotData {
        private String json;

        public JsonPivotData(String json) {
            this.json = json;
        }

        @Override
        public String toJson() {
            return json;
        }
    }

    /**
     * Create PivotTable using PivotMode.NONINTERACTIVE and given data and
     * options.
     * 
     * @param pivotData
     *            PivotData
     * @param pivotOptions
     *            PivotOptioms
     */
    public PivotTable(PivotData pivotData, PivotOptions pivotOptions) {
        this(pivotData, pivotOptions, PivotMode.NONINTERACTIVE);
    }

    /**
     * Create PivotTable using given data and options.
     * 
     * @param pivotData
     *            PivotData
     * @param pivotOptions
     *            PivotOptioms
     * @param mode
     *            The mode, PivotMode.INTERACTIVE renders PivotTable with
     *            interactive UI.
     */
    public PivotTable(AbstractPivotData pivotData, PivotOptions pivotOptions,
            PivotMode mode) {
        this.pivotMode = mode;
        id = randomId(10);
        setId(id);
        JsonObject optionsArray = pivotOptions.toJson();
        this.options = pivotOptions;
        this.dataJson = pivotData.toJson();
        this.optionsJson = optionsArray.toJson();
    }

    @Override
    protected void onAttach(AttachEvent event) {
        super.onAttach(event);
        if (options.charts) {
            String cols = options.cols != null
                    ? options.cols.stream().collect(Collectors.joining(","))
                    : null;
            String rows = options.rows != null
                    ? options.rows.stream().collect(Collectors.joining(","))
                    : null;
            String disabledRenderers = options.disabledRerenders != null
                    ? options.disabledRerenders.stream()
                            .collect(Collectors.joining(","))
                    : null;
            event.getUI().getPage().executeJs(
                    "window.drawChartPivotUI($0, $1, $2, $3, $4, $5, $6, $7, $8, $9);",
                    id, dataJson, cols, rows, disabledRenderers,
                    options.renderer, options.aggregator, options.column,
                    options.fieldsDisabled, pivotMode != PivotMode.INTERACTIVE);
        } else {
            event.getUI().getPage().executeJs(
                    "window.drawPivotUI($0, $1, $2, $3, $4, $5, $6, $7);", id,
                    dataJson, optionsJson, options.renderer, options.aggregator,
                    options.column, options.fieldsDisabled,
                    pivotMode != PivotMode.INTERACTIVE);
        }
    }

    private String randomId(int chars) {
        int limit = (10 * chars) - 1;
        String key = "" + rand.nextInt(limit);
        key = String.format("%" + chars + "s", key).replace(' ', '0');
        return "pivot-" + key;
    }
}
