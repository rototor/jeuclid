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
import net.sourceforge.jeuclid.elements.presentation.token.Mo;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.mathml.MathMLDocument;
import org.w3c.dom.mathml.MathMLMathElement;
import org.w3c.dom.mathml.MathMLOperatorElement;

/**
 * Various tests for the MO Element.
 * 
 * @version $Revision$
 */
public class MoTest {
    /**
     * Tests JEuclid MO Extensions
     * 
     * @throws Exception
     *             if anything goes wrong.
     */
    @Test
    public void testMOExtensions() throws Exception {
        final Document doc = MathMLParserSupport
                .parseString("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>"
                        + "<math mode='display' "
                        + "xmlns='http://www.w3.org/1998/Math/MathML' "
                        + "xmlns:jattr='http://jeuclid.sf.net/ns/ext'>"
                        + "<mo stretchy='true'>X</mo>"
                        + "<mo jattr:stretchy='horizontal'>X</mo>"
                        + "<mo jattr:stretchy='vertical'>X</mo>"
                        + "<mo jattr:stretchy='true'>X</mo>" + "</math>");
        final MathMLDocument docElement = DOMBuilder.getInstance()
                .createJeuclidDom(doc);

        final MathMLMathElement mathElement = (MathMLMathElement) docElement
                .getFirstChild();

        final Mo mo = (Mo) mathElement
                .getChildNodes().item(0);
        Assert.assertNotNull(mo);
        Assert.assertTrue(Boolean.parseBoolean(mo.getStretchy()));
        Assert.assertEquals(mo.getExtendedStretchy(), "true");               
        final Mo mo2 = (Mo) mathElement.getChildNodes().item(1);
        Assert.assertNotNull(mo2);
        Assert.assertTrue(Boolean.parseBoolean(mo2.getStretchy()));
        Assert.assertEquals(mo2.getExtendedStretchy(), "horizontal");               
        final Mo mo3 = (Mo) mathElement.getChildNodes().item(2);
        Assert.assertNotNull(mo3);
        Assert.assertTrue(Boolean.parseBoolean(mo3.getStretchy()));
        Assert.assertEquals(mo3.getExtendedStretchy(), "vertical");               
        final Mo mo4 = (Mo) mathElement.getChildNodes().item(3);
        Assert.assertNotNull(mo4);
        Assert.assertTrue(Boolean.parseBoolean(mo4.getStretchy()));
        Assert.assertEquals(mo4.getExtendedStretchy(), "true");               
    }

}
