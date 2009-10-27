/*
 * Copyright 2002 - 2008 JEuclid, http://jeuclid.sf.net
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

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilderFactory;

import net.sourceforge.jeuclid.DOMBuilder;
import net.sourceforge.jeuclid.elements.AbstractJEuclidElement;
import net.sourceforge.jeuclid.elements.presentation.token.Mspace;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.mathml.MathMLPresentationToken;
import org.xml.sax.InputSource;

/**
 * A JUnit test for DOMBuilder; in particular, the sources it can take as input.
 * 
 * @version $Revision$
 */
public class DOMBuilderTest {

    private final DocumentBuilderFactory documentBuilderFactory;

    /**
     * Initialize test.
     */
    public DOMBuilderTest() {
        this.documentBuilderFactory = DocumentBuilderFactory.newInstance();
        this.documentBuilderFactory.setNamespaceAware(true);
        this.documentBuilderFactory.setValidating(false);
    }

    /**
     * Tests if DOM can be constructed manually.
     * 
     * @throws Exception
     *             if the test fails.
     */
    @Test
    public void testConstructor() throws Exception {
        final Document doc = this.parse("<math><mn>1</mn></math>");
        final Document jdoc = DOMBuilder.getInstance().createJeuclidDom(doc);
        final Node firstChild = jdoc.getFirstChild();
        Assert.assertEquals(firstChild.getNodeName(), "math");
        Assert.assertEquals(firstChild.getFirstChild().getNodeName(), "mn");
        final DocumentFragment documentFragment = doc.createDocumentFragment();
        documentFragment.appendChild(doc.createElement(Mspace.ELEMENT));
        Assert.assertEquals(DOMBuilder.getInstance().createJeuclidDom(
                documentFragment).getFirstChild().getNodeName(), "mspace");
        try {
            DOMBuilder.getInstance().createJeuclidDom(
                    doc.getDocumentElement().getFirstChild().getFirstChild());
            Assert.fail("Expected IllegalArgumentException");
        } catch (final IllegalArgumentException e) {
        }
    }

    private Document parse(final String text) throws Exception {
        return this.documentBuilderFactory.newDocumentBuilder().parse(
                new InputSource(new StringReader(text)));
    }

    /**
     * Tests for namespace issues with manual element construction in default
     * namespace.
     * 
     * @throws Exception
     *             if the test fails.
     */
    @Test
    public void testConstructionNSDefault() throws Exception {
        final Document doc = this.parse("<math></math>");
        final Document jdoc = DOMBuilder.getInstance().createJeuclidDom(doc);
        final Node mathNode = jdoc.getFirstChild();
        Assert.assertEquals(mathNode.getNodeName(), "math");
        Assert.assertEquals(mathNode.getNamespaceURI(),
                AbstractJEuclidElement.URI);
        final MathMLPresentationToken mn = (MathMLPresentationToken) mathNode
                .getOwnerDocument().createElement("mn");
        mn.setTextContent("1");
        mathNode.appendChild(mn);
        Assert.assertSame(mn.getOwnerDocument(), mathNode.getOwnerDocument());
        Assert.assertSame(mn.getOwnerMathElement(), mathNode);
        Assert.assertEquals(mn.getNamespaceURI(), AbstractJEuclidElement.URI);
    }

    /**
     * Tests for namespace issues with manual element construction in mml
     * namespace.
     * 
     * @throws Exception
     *             if the test fails.
     */
    @Test
    public void testConstructionNSMml() throws Exception {
        final Document doc = this.parse("<mml:math "
                + "xmlns:mml='http://www.w3.org/1998/Math/MathML' "
                + "xmlns='http://bla'></mml:math>");
        final Document jdoc = DOMBuilder.getInstance().createJeuclidDom(doc);
        final Node mathNode = jdoc.getFirstChild();
        Assert.assertEquals(mathNode.getNodeName(), "mml:math");
        Assert.assertEquals(mathNode.getLocalName(), "math");
        Assert.assertEquals(mathNode.getNamespaceURI(),
                AbstractJEuclidElement.URI);
        final MathMLPresentationToken mn = (MathMLPresentationToken) mathNode
                .getOwnerDocument().createElement("mn");
        mn.setTextContent("1");
        mathNode.appendChild(mn);
        Assert.assertSame(mn.getOwnerDocument(), mathNode.getOwnerDocument());
        Assert.assertSame(mn.getOwnerMathElement(), mathNode);
        Assert.assertEquals(mn.getNamespaceURI(), AbstractJEuclidElement.URI);
    }

}
