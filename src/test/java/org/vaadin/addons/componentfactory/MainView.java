package org.vaadin.addons.componentfactory;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;import com.vaadin.flow.theme.Theme;

public class MainView extends AppLayout {

    public MainView() {
        VerticalLayout menu = new VerticalLayout();
        menu.add(new RouterLink("Basic pivot", PivotView.class),
                new RouterLink("Bean list", BeanPivotView.class),
                new RouterLink("Selected renderers", MpsView.class),
                new RouterLink("Two pivots", TwoPivotsView.class),
                new RouterLink("Many columns", ManyColumnsView.class),
                new RouterLink("Localization (Fi)", I18nView.class));
        addToDrawer(menu);
    }
}
