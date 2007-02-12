/*
 * Copyright 2007 - 2007 JEuclid, http://jeuclid.sf.net
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

package net.sourceforge.jeuclid.test;

import net.sourceforge.jeuclid.DOMMathBuilder;
import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.element.MathOperator;
import net.sourceforge.jeuclid.util.MathMLParserSupport;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.mathml.MathMLDocument;
import org.w3c.dom.mathml.MathMLMathElement;
import org.w3c.dom.mathml.MathMLPresentationContainer;

/**
 * Various tests for the DOM model.
 * 
 * @author Max Berger
 */
public class DOMModelTest {

    /**
     * Tests is the "id" attribute works.
     * 
     * @throws Exception
     *             if anything goes wrong.
     */
    @Test
    public void testID() throws Exception {
        final Document docWithID = MathMLParserSupport
                .parseString("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><math mode=\"display\">"
                        + "<mrow id='abc'><mn>1</mn></mrow></math>");

        final MathMLDocument docElement = new DOMMathBuilder(docWithID,
                new MathBase(MathBase.getDefaultParameters()))
                .getMathRootElement();

        final MathMLMathElement mathElement = (MathMLMathElement) docElement
                .getFirstChild();

        // TODO: enable this test
        // assertEquals(mathElement.getDisplay(), "block");
        final MathMLPresentationContainer row = (MathMLPresentationContainer) mathElement
                .getFirstChild();
        Assert.assertNotNull(row);
        Assert.assertEquals(row.getId(), "abc");
    }

    /**
     * Tests for all attributed on mathOperator attribute works.
     * 
     * @throws Exception
     *             if anything goes wrong.
     */
    @Test
    public void testMOAttrs() throws Exception {
        final Document doc = MathMLParserSupport
                .parseString("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><math mode=\"display\">"
                        + "<mo stretchy='true'>X</mo>"
                        + "<mo stretchy='false'>Y</mo>"
                        + "<mo>&#x0007d;</mo>"
                        + "<mo>&#x0201d;</mo>"
                        + "</math>");
        final MathMLDocument docElement = new DOMMathBuilder(doc,
                new MathBase(MathBase.getDefaultParameters()))
                .getMathRootElement();

        final MathMLMathElement mathElement = (MathMLMathElement) docElement
                .getFirstChild();

        // TODO: Use MathMLOperator instead
        final MathOperator mo = (MathOperator) mathElement.getChildNodes()
                .item(0);
        Assert.assertNotNull(mo);
        Assert.assertTrue(mo.getStretchy());
        final MathOperator mo2 = (MathOperator) mathElement.getChildNodes()
                .item(1);
        Assert.assertNotNull(mo2);
        Assert.assertFalse(mo2.getStretchy());
        final MathOperator mo3 = (MathOperator) mathElement.getChildNodes()
                .item(2);
        Assert.assertTrue(mo3.getStretchy());
        final MathOperator mo4 = (MathOperator) mathElement.getChildNodes()
                .item(3);
        Assert.assertFalse(mo4.getStretchy());
    }
}
