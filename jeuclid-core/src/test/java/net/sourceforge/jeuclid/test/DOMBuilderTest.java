package net.sourceforge.jeuclid.test;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilderFactory;

import net.sourceforge.jeuclid.DOMBuilder;
import net.sourceforge.jeuclid.elements.presentation.token.Mspace;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

/**
 * A JUnit test for DOMBuilder; in particular, the sources it can take as
 * input.
 * 
 * @version $Revision$
 */
public class DOMBuilderTest {

    private final DocumentBuilderFactory documentBuilderFactory;

    public DOMBuilderTest() {
        this.documentBuilderFactory = DocumentBuilderFactory.newInstance();
        this.documentBuilderFactory.setNamespaceAware(true);
        this.documentBuilderFactory.setValidating(false);
    }

    @Test
    public void testConstructor() throws Exception {
        final Document doc = this.parse("<math><mn>1</mn></math>");
        final Document jdoc = DOMBuilder.getInstance()
                .createJeuclidDom(doc);
        final Node firstChild = jdoc.getFirstChild();
        Assert.assertEquals(firstChild.getNodeName(), "math");
        Assert.assertEquals(firstChild.getFirstChild().getNodeName(), "mn");
        final DocumentFragment documentFragment = doc
                .createDocumentFragment();
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

}
