package org.vaadin.addons.componentfactory;

import java.util.HashMap;
import java.util.Map;

import org.vaadin.addons.componentfactory.PivotTable.Aggregator;
import org.vaadin.addons.componentfactory.PivotTable.PivotData;
import org.vaadin.addons.componentfactory.PivotTable.PivotMode;
import org.vaadin.addons.componentfactory.PivotTable.PivotOptions;
import org.vaadin.addons.componentfactory.PivotTable.PivotTableI18n;
import org.vaadin.addons.componentfactory.PivotTable.Renderer;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

@Route(value = "i18n", layout = MainView.class)
public class I18nView extends Div {

    public I18nView() {
        PivotData pivotData = new PivotData();
        pivotData.addColumn("väri", String.class);
        pivotData.addColumn("muoto", String.class);
        pivotData.addColumn("koko", Integer.class);
        pivotData.addColumn("täyttö", Boolean.class);
        pivotData.addRow("sininen", "ympyrä", 2, true);
        pivotData.addRow("punainen", "kolmio", 3, false);
        pivotData.addRow("oranssi", "neliö", 1, true);
        pivotData.addRow("keltainen", "ympyrä", 3, false);
        pivotData.addRow("ruskea", "ympyrä", 2, true);

        PivotOptions pivotOptions = new PivotOptions();
        pivotOptions.setRows("väri");
        pivotOptions.setCols("muoto");
        pivotOptions.setCharts(true);

        pivotOptions.setDisabledRenderers(Renderer.HORIZONTAL_BAR_CHART,
                Renderer.HORIZONTAL_STACKED_BAR_CHART, Renderer.SCATTER_CHART,
                Renderer.AREA_CHART, Renderer.LINE_CHART);

        PivotTable table = new PivotTable(pivotData, pivotOptions,
                PivotMode.INTERACTIVE);

        Map<String, String> texts = new HashMap<>();
        texts.put(Renderer.TABLE, "Taulukko");
        texts.put(Renderer.TABLE_BARCHART, "Taulukko palkkikaavio");
        texts.put(Renderer.TABLE_HEATMAP, "Lämpökartta");
        texts.put(Renderer.COL_HEATMAP, "Sarake lämpökartta");
        texts.put(Renderer.ROW_HEATMAP, "Rivi lämpökartta");
        texts.put(Renderer.BAR_CHART, "Palkkikaavio");
        texts.put(Renderer.STACKED_BAR_CHART, "Pinottu palkkikaavio");
        texts.put(Renderer.TSV_EXPORT, "TSV Tuloste");
        texts.put(Aggregator.COUNT, "Määrä");
        texts.put(Aggregator.COUNT_UNIQUE_VALUES, "Laske uniikit");
        texts.put(Aggregator.LIST_UNIQUE_VALUES, "Listaa uniikit");
        texts.put(Aggregator.SUM , "Summa");
        texts.put(Aggregator.INTEGER_SUM , "Kokonaislukusumma");
        texts.put(Aggregator.MEDIAN, "Mediaani");
        texts.put(Aggregator.SAMPLE_STANDARD_DEVIATION, "Standardipoikkeama");
        texts.put(Aggregator.SAMPLE_VARIANCE, "Varianssi");
        texts.put(Aggregator.MINIMUM, "Minimi");
        texts.put(Aggregator.MAXIMUM, "Maksimi");
        texts.put(Aggregator.FIRST, "Ensimäinen");
        texts.put(Aggregator.LAST, "Viimeinen");
        texts.put(Aggregator.SUM_OVER_SUM, "Summien summa");
        texts.put(Aggregator.UPPER_BOUND, "80% yläraja");
        texts.put(Aggregator.LOWER_BOUND, "80% alaraja");
        texts.put(Aggregator.SUM_FRACTION_OF_TOTAL, "Summa yhteissummasta");
        texts.put(Aggregator.SUM_FRACTION_OF_ROWS, "Summa riveistä");
        texts.put(Aggregator.SUM_FRACTION_OF_COLS, "Summa sarakkeista");
        texts.put(Aggregator.COUNT_FRACTION_OF_TOTAL, "Määrä yhteissummasta");
        texts.put(Aggregator.COUNT_FRACTION_OF_ROWS, "Määrä riveistä");
        texts.put(Aggregator.COUNT_FRACTION_OF_COLS, "Määrä sarakkeista");

        PivotTableI18n i18n = new PivotTableI18n(texts); 
        table.setI18n(i18n);

        add(table);
    }

}