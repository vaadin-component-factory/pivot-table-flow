package org.vaadin.addons.componentfactory;

import com.vaadin.testbench.TestBenchElement;

import org.junit.Assert;
import org.junit.Test;


public class AddonIT extends AbstractViewTest {

    @Test
    public void addonTextIsRendered() {
        TestBenchElement tableElement = $("table").attribute("class", "pvtUi").first();
        Assert.assertNotNull(tableElement);
    }
}
