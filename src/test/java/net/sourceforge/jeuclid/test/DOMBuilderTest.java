package net.sourceforge.jeuclid.test;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilderFactory;

import net.sourceforge.jeuclid.DOMBuilder;
import net.sourceforge.jeuclid.MathBase;

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

    private DocumentBuilderFactory documentBuilderFactory;

    public DOMBuilderTest() {
        documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        documentBuilderFactory.setValidating(false);
    }

    @Test
    public void testConstructor() throws Exception {
        Document doc = parse("<math><mn>1</mn></math>");
        Assert.assertEquals(new DOMBuilder(doc, new MathBase(MathBase
                .getDefaultParameters())).getMathRootElement()
                .getFirstChild().getNodeName(), "math");
        Assert.assertEquals(new DOMBuilder(doc.getDocumentElement()
                .getFirstChild(), new MathBase(MathBase
                .getDefaultParameters())).getMathRootElement()
                .getFirstChild().getNodeName(), "mn");
        DocumentFragment documentFragment = doc.createDocumentFragment();
        documentFragment.appendChild(doc.createElement("mspace"));
        Assert.assertEquals(new DOMBuilder(documentFragment, new MathBase(
                MathBase.getDefaultParameters())).getMathRootElement()
                .getFirstChild().getNodeName(), "mspace");
        try {
            new DOMBuilder(doc.getDocumentElement().getFirstChild()
                    .getFirstChild(), new MathBase(MathBase
                    .getDefaultParameters()));
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    private Document parse(String text) throws Exception {
        return documentBuilderFactory.newDocumentBuilder().parse(
                new InputSource(new StringReader(text)));
    }

}
