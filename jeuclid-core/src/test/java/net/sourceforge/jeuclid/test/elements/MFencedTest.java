/*
 * Copyright 2007 - 2010 JEuclid, http://jeuclid.sf.net
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

import junit.framework.Assert;
import net.sourceforge.jeuclid.DOMBuilder;
import net.sourceforge.jeuclid.MathMLParserSupport;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.mathml.MathMLDocument;
import org.w3c.dom.mathml.MathMLFencedElement;
import org.w3c.dom.mathml.MathMLMathElement;

/**
 * Various tests for the MFenced Element.
 *
 * @version $Revision$
 */
public class MFencedTest {

    /**
     * Tests three different values for separators.
     *
     * @throws Exception
     *             if anything goes wrong.
     */
    @Test
    public void testSeparators() throws Exception {
        Assert.assertEquals(",", this.createFenced("").getSeparators());
        Assert.assertEquals("", this.createFenced("separators=''").getSeparators());
        Assert.assertEquals(",", this.createFenced("separators=','").getSeparators());
        Assert.assertEquals("|", this.createFenced("separators='|'").getSeparators());
        Assert.assertEquals("|", this.createFenced("separators=' | '").getSeparators());
        Assert.assertEquals("|,", this.createFenced("separators=' | , '").getSeparators());
    }

    private MathMLFencedElement createFenced(final String content) throws Exception {
        final Document docWithID = MathMLParserSupport
                .parseString("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><math mode=\"display\">"
                        + "<mfenced " + content + " />" + "</math>");
        final MathMLDocument docElement = DOMBuilder.getInstance()
                .createJeuclidDom(docWithID);
        final MathMLMathElement mathElement = (MathMLMathElement) docElement
                .getFirstChild();
        final MathMLFencedElement fencedElement = (MathMLFencedElement) mathElement
                .getFirstChild();
        return fencedElement;
    }
}
