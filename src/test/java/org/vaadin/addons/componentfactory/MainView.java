package org.vaadin.addons.componentfactory;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.Theme;

@Theme("mytheme")
public class MainView extends AppLayout implements AppShellConfigurator {

    public MainView() {
        VerticalLayout menu = new VerticalLayout();
        menu.add(new RouterLink("Basic pivot", PivotView.class),
                new RouterLink("Bean list", BeanPivotView.class),
                new RouterLink("Selected renderers", MpsView.class),
                new RouterLink("Two pivots", TwoPivotsView.class));
        addToDrawer(menu);
    }
}
