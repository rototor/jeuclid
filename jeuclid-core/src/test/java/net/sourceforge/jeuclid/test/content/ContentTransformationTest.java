package net.sourceforge.jeuclid.test.content;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import junit.framework.Assert;
import net.sourceforge.jeuclid.DOMBuilder;
import net.sourceforge.jeuclid.MathMLParserSupport;
import net.sourceforge.jeuclid.elements.AbstractJEuclidElement;
import net.sourceforge.jeuclid.elements.support.NamespaceContextAdder;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class ContentTransformationTest {

    private void doTest(final String mathWithContent, final String... tests)
            throws Exception {

        final XPathFactory factory = XPathFactory.newInstance();
        final XPath xpath = factory.newXPath();
        xpath.setNamespaceContext(new NamespaceContextAdder("m",
                AbstractJEuclidElement.URI, null));

        final Document dorig = MathMLParserSupport.parseString("<math>"
                + mathWithContent + "</math>");
        final Document d = DOMBuilder.getInstance().createJeuclidDom(dorig,
                true, true);

        // System.out.println(MathMLSerializer.serializeDocument(d, false,
        // true));
        final int count = tests.length / 2;
        for (int i = 0; i < count; i++) {
            final String expr = tests[i * 2];
            final String text = tests[i * 2 + 1];

            final XPathExpression xpathExpr = xpath.compile("/m:math" + expr);
            final Node n = (Node) xpathExpr.evaluate(d, XPathConstants.NODE);
            Assert.assertNotNull(n);
            Assert.assertEquals(text, n.getTextContent());
        }
    }

    @Test
    public void testSimpleTransformations() throws Exception {
        this.doTest("<apply><plus/><cn>5</cn><ci>x</ci></apply>",
                "/m:mrow/m:mn[1]", "5", "/m:mrow/m:mo[1]", "+",
                "/m:mrow/m:mi[1]", "x");
        this.doTest("<apply><times/><cn>5</cn><ci>x</ci></apply>",
                "/m:mrow/m:mn[1]", "5", "/m:mrow/m:mo[1]", "\u2062",
                "/m:mrow/m:mi[1]", "x");
    }
}
