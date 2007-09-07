package net.sourceforge.jeuclid.test;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilderFactory;

import net.sourceforge.jeuclid.DOMBuilder;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.xml.sax.InputSource;

/**
 * A JUnit test for DOMBuilder; in particular, the sources it can take as
 * input.
 * 
 * @author Ernest Mishkin
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
        Assert.assertEquals(DOMBuilder.getDOMBuilder().createJeuclidDom(doc)
                .getFirstChild().getNodeName(), "math");
        Assert.assertEquals(DOMBuilder.getDOMBuilder().createJeuclidDom(
                doc.getDocumentElement().getFirstChild()).getFirstChild()
                .getNodeName(), "mn");
        final DocumentFragment documentFragment = doc
                .createDocumentFragment();
        documentFragment.appendChild(doc.createElement("mspace"));
        Assert.assertEquals(DOMBuilder.getDOMBuilder().createJeuclidDom(
                documentFragment).getFirstChild().getNodeName(), "mspace");
        try {
            DOMBuilder.getDOMBuilder().createJeuclidDom(
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
