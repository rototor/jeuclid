/*
 * Copyright 2007 - 2008 JEuclid, http://jeuclid.sf.net
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
import net.sourceforge.jeuclid.MathMLSerializer;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.mathml.MathMLAnnotationElement;
import org.w3c.dom.mathml.MathMLDocument;
import org.w3c.dom.mathml.MathMLMathElement;
import org.w3c.dom.mathml.MathMLSemanticsElement;

/**
 * Various tests for the DOM model.
 * 
 * @version $Revision$
 */
public class ContentTest {

    /**
     * Tests is the Annotation element.
     * 
     * @throws Exception
     *             if anything goes wrong.
     */
    @Test
    public void testAnnotation() throws Exception {
        final Document docWithID = MathMLParserSupport
                .parseString("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><math mode=\"display\">"
                        + "<semantics><apply><plus/><apply><sin/><ci> x </ci></apply><cn> 5 </cn>"
                        + "</apply><annotation encoding=\"TeX\">\\sin x + 5</annotation></semantics></math>");

        final MathMLDocument docElement = DOMBuilder.getInstance()
                .createJeuclidDom(docWithID);
        final MathMLMathElement mathElement = (MathMLMathElement) docElement
                .getFirstChild();
        final MathMLSemanticsElement semElement = (MathMLSemanticsElement) mathElement
                .getFirstChild();
        final MathMLAnnotationElement anno = (MathMLAnnotationElement) semElement
                .getAnnotation(1);
        Assert.assertEquals(anno.getEncoding(), "TeX");
        Assert.assertEquals(anno.getBody(), "\\sin x + 5");

        anno.setEncoding("text/plain");
        anno.setBody("sin(x)+5");

        final Document reserial = MathMLParserSupport
                .parseString("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><math mode=\"display\">"
                        + "<semantics><apply><plus/><apply><sin/><ci> x </ci></apply><cn> 5 </cn>"
                        + "</apply><annotation encoding=\"text/plain\">sin(x)+5</annotation></semantics></math>");
        final MathMLDocument docElement2 = DOMBuilder.getInstance()
                .createJeuclidDom(reserial);
        // TODO
        Assert.assertEquals(MathMLSerializer.serializeDocument(docElement,
                false, false), MathMLSerializer.serializeDocument(
                docElement2, false, false));
    }

}
