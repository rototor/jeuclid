package net.sourceforge.jeuclid.test.swing;

import java.awt.Dimension;

import net.sourceforge.jeuclid.swing.JMathComponent;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.w3c.dom.Node;

public class JMathComponentTest {

    @Test
    public void testPreferredSize() {
        JMathComponent jmc = new JMathComponent();
        Dimension d = jmc.getPreferredSize();
        Assert.assertEquals(d.getHeight(), 0.0);
        Assert.assertEquals(d.getWidth(), 0.0);
        jmc.setContent("<math><mi>x</mi></math>");
        Dimension d2 = jmc.getPreferredSize();
        Assert.assertTrue(d2.getHeight() > 1);
        Assert.assertTrue(d2.getWidth() > 1);
        Node mi = jmc.getDocument().getFirstChild().getFirstChild();
        mi.setTextContent("xxx");
        Dimension d3 = jmc.getPreferredSize();
        Assert.assertEquals(d3.getHeight(), d2.getHeight());
        Assert.assertTrue(d3.getWidth() > d2.getWidth());
    }
}
