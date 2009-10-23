/*
 * Copyright 2002 - 2009 JEuclid, http://jeuclid.sf.net
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

import net.sourceforge.jeuclid.DOMBuilder;
import net.sourceforge.jeuclid.MathMLParserSupport;
import net.sourceforge.jeuclid.elements.AbstractJEuclidElement;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.mathml.MathMLDocument;

/**
 * Tests misc DOM Namespace functionality
 * 
 * @version $Revision$
 */
public class DOMNameSpaceTest {

    /**
     * Tests if MathML Namespace is kept.
     * 
     * @throws Exception
     *             if the test fails.
     */
    @Test
    public void testNameSpaceKeep() throws Exception {
        final Document doc = MathMLParserSupport
                .parseString("<math xmlns='http://www.w3.org/1998/Math/MathML'><mn>1</mn></math>");
        Assert.assertEquals(doc.getDocumentElement().getNamespaceURI(),
                AbstractJEuclidElement.URI);
        final MathMLDocument jdoc = DOMBuilder.getInstance().createJeuclidDom(
                doc);
        Assert.assertEquals(jdoc.getDocumentElement().getNamespaceURI(),
                AbstractJEuclidElement.URI);
    }

    /**
     * Tests if MathML Namespace is added.
     * 
     * @throws Exception
     *             if the test fails.
     */
    @Test
    public void testNameSpaceAdded() throws Exception {
        final Document doc = MathMLParserSupport
                .parseString("<math><mn>1</mn></math>");
        Assert.assertEquals(doc.getDocumentElement().getNamespaceURI(), null);
        final MathMLDocument jdoc = DOMBuilder.getInstance().createJeuclidDom(
                doc);
        Assert.assertEquals(jdoc.getDocumentElement().getNamespaceURI(),
                AbstractJEuclidElement.URI);
    }

    /**
     * Tests foreign namespaces
     * 
     * @throws Exception
     *             if the test fails.
     */
    @Test
    public void testForeignNameSpaces() throws Exception {
        final Document doc = MathMLParserSupport
                .parseString("<math><f:bla xmlns:f='http://www.atest.org/'>1</f:bla></math>");
        Assert.assertEquals(doc.getDocumentElement().getNamespaceURI(), null);
        final MathMLDocument jdoc = DOMBuilder.getInstance().createJeuclidDom(
                doc);
        Assert.assertEquals(jdoc.getDocumentElement().getNamespaceURI(),
                AbstractJEuclidElement.URI);
        Node n = jdoc.getDocumentElement().getChildNodes().item(0);
        Assert.assertEquals(n.getNamespaceURI(), "http://www.atest.org/");
    }

}
