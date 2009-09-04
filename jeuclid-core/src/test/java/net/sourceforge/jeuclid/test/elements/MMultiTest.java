/*
 * Copyright 2009 - 2009 JEuclid, http://jeuclid.sf.net
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

/* $Id$ */

package net.sourceforge.jeuclid.test.elements;

import net.sourceforge.jeuclid.DOMBuilder;
import net.sourceforge.jeuclid.MathMLParserSupport;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.mathml.MathMLDocument;
import org.w3c.dom.mathml.MathMLMathElement;
import org.w3c.dom.mathml.MathMLMultiScriptsElement;

/**
 * Various tests for the MMultiscript Element.
 * 
 * @version $Revision$
 */
public class MMultiTest {

    /**
     * Tests Proper identification of sub and superscripts.
     * 
     * @throws Exception
     *             if anything goes wrong.
     */
    @Test
    public void testIdentSimple() throws Exception {
        final Document docWithID = MathMLParserSupport
                .parseString("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><math mode=\"display\">"
                        + "<mmultiscripts>"
                        + "<mi>x</mi>"
                        + "<mn>1</mn>"
                        + "<mn>2</mn>"
                        + "<mprescripts/> "
                        + "<mn>3</mn>"
                        + "<mn>4</mn>" + "</mmultiscripts></math>");
        final MathMLDocument docElement = DOMBuilder.getInstance()
                .createJeuclidDom(docWithID);
        final MathMLMathElement mathElement = (MathMLMathElement) docElement
                .getFirstChild();
        final MathMLMultiScriptsElement multiElement = (MathMLMultiScriptsElement) mathElement
                .getFirstChild();

        Assert.assertEquals(multiElement.getBase().getTextContent(), "x");
        Assert.assertEquals(multiElement.getSubScript(1).getTextContent(), "1");
        Assert.assertEquals(multiElement.getSuperScript(1).getTextContent(),
                "2");
        Assert.assertEquals(multiElement.getPreSubScript(1).getTextContent(),
                "3");
        Assert.assertEquals(multiElement.getPreSuperScript(1).getTextContent(),
                "4");

    }

    /**
     * Tests Proper identification of sub and superscripts with whitespace.
     * 
     * @throws Exception
     *             if anything goes wrong.
     */
    @Test
    public void testIdentSpace() throws Exception {
        final Document docWithID = MathMLParserSupport
                .parseString("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><math mode=\"display\">"
                        + "<mmultiscripts>\n"
                        + "  <mi>x</mi>\n"
                        + "  <mn>1</mn>\n"
                        + "  <mn>2</mn>\n"
                        + "  <mprescripts/>\n"
                        + "  <mn>3</mn>\n"
                        + "  <mn>4</mn>\n" + "</mmultiscripts></math>");
        final MathMLDocument docElement = DOMBuilder.getInstance()
                .createJeuclidDom(docWithID);
        final MathMLMathElement mathElement = (MathMLMathElement) docElement
                .getFirstChild();
        final MathMLMultiScriptsElement multiElement = (MathMLMultiScriptsElement) mathElement
                .getFirstChild();

        Assert.assertEquals(multiElement.getBase().getTextContent(), "x");
        Assert.assertEquals(multiElement.getSubScript(1).getTextContent(), "1");
        Assert.assertEquals(multiElement.getSuperScript(1).getTextContent(),
                "2");
        Assert.assertEquals(multiElement.getPreSubScript(1).getTextContent(),
                "3");
        Assert.assertEquals(multiElement.getPreSuperScript(1).getTextContent(),
                "4");

    }

}
