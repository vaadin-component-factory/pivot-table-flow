package org.vaadin.addons.componentfactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.vaadin.addons.componentfactory.PivotTable.PivotData;
import org.vaadin.addons.componentfactory.PivotTable.PivotMode;
import org.vaadin.addons.componentfactory.PivotTable.PivotOptions;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

@Route(value = "many", layout = MainView.class)
public class ManyColumnsView extends Div {

    Random rand = new Random();

    public ManyColumnsView() {
        PivotData pivotData = new PivotData();
        for (int i=0;i<20;i++) {
           pivotData.addColumn("col"+i, Integer.class);
        }
        for (int i=0;i<2000;i++) {
            Map<String, Object> rowData = new HashMap<>();
            for (int j=0;j<20;j++) {
                rowData.put("col"+j, rand.nextInt(10));
             }            
            pivotData.addRow(rowData);
        }
        
        PivotOptions pivotOptions = new PivotOptions();
        pivotOptions.setCols("col1");
        pivotOptions.setRows("col2");
        pivotOptions.setCharts(true);

        PivotTable table = new PivotTable(pivotData, pivotOptions,
                PivotMode.INTERACTIVE);

//        Map<String, String> texts = new HashMap<>();
//        texts.put(Renderer.TABLE, "Taulukko");
//        texts.put(Renderer.TABLE_BARCHART, "Taulukko palkkikaavio");
//        texts.put(Renderer.TABLE_HEATMAP, "Lämpökartta");
//        texts.put(Renderer.COL_HEATMAP, "Sarake lämpökartta");
//        texts.put(Renderer.ROW_HEATMAP, "Rivi lämpökartta");
//        texts.put(Renderer.BAR_CHART, "Palkkikaavio");
//        texts.put(Renderer.STACKED_BAR_CHART, "Pinottu palkkikaavio");
//        texts.put(Renderer.TSV_EXPORT, "TSV Tuloste");
//        texts.put(Aggregator.COUNT, "Määrä");
//        texts.put(Aggregator.COUNT_UNIQUE_VALUES, "Laske uniikit");
//        texts.put(Aggregator.LIST_UNIQUE_VALUES, "Listaa uniikit");
//        texts.put(Aggregator.SUM , "Summa");
//        texts.put(Aggregator.INTEGER_SUM , "Kokonaislukusumma");
//        texts.put(Aggregator.MEDIAN, "Mediaani");
//        texts.put(Aggregator.SAMPLE_STANDARD_DEVIATION, "Standardipoikkeama");
//        texts.put(Aggregator.SAMPLE_VARIANCE, "Varianssi");
//        texts.put(Aggregator.MINIMUM, "Minimi");
//        texts.put(Aggregator.MAXIMUM, "Maksimi");
//        texts.put(Aggregator.FIRST, "Ensimäinen");
//        texts.put(Aggregator.LAST, "Viimeinen");
//        texts.put(Aggregator.SUM_OVER_SUM, "Summien summa");
//        texts.put(Aggregator.UPPER_BOUND, "80% yläraja");
//        texts.put(Aggregator.LOWER_BOUND, "80% alaraja");
//        texts.put(Aggregator.SUM_FRACTION_OF_TOTAL, "Summa yhteissummasta");
//        texts.put(Aggregator.SUM_FRACTION_OF_ROWS, "Summa riveistä");
//        texts.put(Aggregator.SUM_FRACTION_OF_COLS, "Summa sarakkeista");
//        texts.put(Aggregator.COUNT_FRACTION_OF_TOTAL, "Määrä yhteissummasta");
//        texts.put(Aggregator.COUNT_FRACTION_OF_ROWS, "Määrä riveistä");
//        texts.put(Aggregator.COUNT_FRACTION_OF_COLS, "Määrä sarakkeista");
//
//        texts.put("Cancel", "Peruuta");
//        texts.put("Apply", "Käytä");
//        texts.put("Select All", "Valitse kaikki");
//        texts.put("Select None", "Poista valinnat");
//        texts.put("Filter values", "Hae arvoja");
//
//        PivotTableI18n i18n = new PivotTableI18n(texts); 
//        table.setI18n(i18n);

        add(table);
    }

}