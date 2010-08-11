/*
 * Copyright 2010 - 2010 JEuclid, http://jeuclid.sf.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/* $Id$ */

package net.sourceforge.jeuclid.test.swing;

import java.awt.Dimension;

import net.sourceforge.jeuclid.swing.JMathComponent;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Node;

/**
 * Tests for {@link JMathComponent}.
 *
 * @version $Revision$
 */
public class JMathComponentTest {

    /**
     * Checks if {@link JMathComponent#getPreferredSize() works and updates if
     * the content changes.
     */
    @Test
    public void testPreferredSize() {
        final JMathComponent jmc = new JMathComponent();
        final Dimension d = jmc.getPreferredSize();
        Assert.assertEquals(d.getHeight(), 0.0, 0.01);
        Assert.assertEquals(d.getWidth(), 0.0, 0.01);
        jmc.setContent("<math><mi>x</mi></math>");
        final Dimension d2 = jmc.getPreferredSize();
        Assert.assertTrue(d2.getHeight() > 1);
        Assert.assertTrue(d2.getWidth() > 1);
        final Node mi = jmc.getDocument().getFirstChild().getFirstChild();
        mi.setTextContent("xxx");
        final Dimension d3 = jmc.getPreferredSize();
        Assert.assertEquals(d3.getHeight(), d2.getHeight(), 0.001);
        Assert.assertTrue(d3.getWidth() > d2.getWidth());
    }
}
