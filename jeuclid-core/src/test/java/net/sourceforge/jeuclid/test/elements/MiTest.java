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

package net.sourceforge.jeuclid.test.elements;

import net.sourceforge.jeuclid.DOMBuilder;
import net.sourceforge.jeuclid.MathMLParserSupport;
import net.sourceforge.jeuclid.elements.presentation.token.Mi;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.mathml.MathMLDocument;
import org.w3c.dom.mathml.MathMLMathElement;

/**
 * Various tests for the {@link Mi} Element.
 *
 * @version $Revision$
 */
public class MiTest {

    /** Default constructor. */
    public MiTest() {
        // nothing to do.
    }

    /**
     * Tests behavior of non-printing characteres to be detected as 1-letter
     * identifiers.
     *
     * @throws Exception
     *             if the test fails.
     */
    @Test
    public void testAutoItalic() throws Exception {
        final Document doc = MathMLParserSupport
                .parseString("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>"
                        + "<math mode='display' "
                        + "xmlns='http://www.w3.org/1998/Math/MathML' "
                        + "xmlns:jattr='http://jeuclid.sf.net/ns/ext'>"
                        + "<mi>i</mi>" + "<mi>ij</mi>" + "<mi>i&#x302;</mi>"
                        + "</math>");
        final MathMLDocument docElement = DOMBuilder.getInstance()
                .createJeuclidDom(doc);

        final MathMLMathElement mathElement = (MathMLMathElement) docElement
                .getFirstChild();

        final Mi mi1 = (Mi) mathElement.getChildNodes().item(0);
        Assert.assertEquals(mi1.getMathvariant(), "italic");

        final Mi mi2 = (Mi) mathElement.getChildNodes().item(1);
        Assert.assertEquals(mi2.getMathvariant(), "normal");

        final Mi mi3 = (Mi) mathElement.getChildNodes().item(2);
        Assert.assertEquals(mi3.getMathvariant(), "italic");
    }
}
