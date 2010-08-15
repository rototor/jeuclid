package net.sourceforge.jeuclid.test.content;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import junit.framework.Assert;
import net.sourceforge.jeuclid.DOMBuilder;
import net.sourceforge.jeuclid.MathMLParserSupport;
import net.sourceforge.jeuclid.MathMLSerializer;
import net.sourceforge.jeuclid.elements.AbstractJEuclidElement;
import net.sourceforge.jeuclid.elements.support.NamespaceContextAdder;

import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ContentTransformationTest {

    private void testbyXpath(final String mathWithContent,
            final String... tests) throws Exception {

        final XPathFactory factory = XPathFactory.newInstance();
        final XPath xpath = factory.newXPath();
        xpath.setNamespaceContext(new NamespaceContextAdder("m",
                AbstractJEuclidElement.URI, null));

        final Document dorig = MathMLParserSupport.parseString("<math>"
                + mathWithContent + "</math>");
        final Document d = DOMBuilder.getInstance().createJeuclidDom(dorig,
                true, true);

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

    public void testByDirectComparison(final String contentMath,
            final String presentationMath) throws Exception {
        final Document dorigcontent = MathMLParserSupport.parseString("<math>"
                + contentMath + "</math>");
        final Document dcontent = DOMBuilder.getInstance().createJeuclidDom(
                dorigcontent, true, true);
        final Document dorigpresentation = MathMLParserSupport
                .parseString("<math>" + presentationMath + "</math>");
        final Document dpresentation = DOMBuilder.getInstance()
                .createJeuclidDom(dorigpresentation, true, true);

        this.removeDoubleNS(dcontent, null);
        this.removeDoubleNS(dpresentation, null);

        final String cString = MathMLSerializer.serializeDocument(dcontent,
                false, true);
        final String pString = MathMLSerializer.serializeDocument(
                dpresentation, false, true);

        Assert.assertEquals(pString, cString);
    }

    private void removeDoubleNS(@Nonnull final Node node,
            @Nullable final Map<String, Set<String>> namespaces) {
        if (node.getNodeType() == Node.TEXT_NODE) {
            return;
        }
        final Map<String, Set<String>> contextHere = new HashMap<String, Set<String>>();
        if (namespaces != null) {
            contextHere.putAll(namespaces);
        }
        final NamedNodeMap allAttrs = node.getAttributes();
        if (allAttrs != null) {
            for (int i = 0; i < allAttrs.getLength(); i++) {
                final Node attr = allAttrs.item(i);
                if (attr.getNodeName().startsWith("xmlns")) {
                    String prefix = attr.getLocalName();
                    final String namespace = attr.getNodeValue();
                    if ("xmlns".equals(prefix)) {
                        prefix = "";
                    }

                    final Set<String> currentlyKnown = contextHere
                            .get(namespace);
                    final Set<String> newSet = new LinkedHashSet<String>();
                    if (currentlyKnown != null) {
                        newSet.addAll(currentlyKnown);
                    }
                    if (!newSet.isEmpty()) {
                        ((Element) node).removeAttribute(attr.getNodeName());
                        ((Element) node).removeAttributeNS(
                                attr.getNamespaceURI(), attr.getLocalName());
                    }
                    newSet.add(prefix);
                    contextHere.put(namespace, newSet);
                }
            }
        }

        final String name = node.getNodeName();
        String prefix;
        final String[] elements = name.split(":");
        if (elements.length == 1) {
            prefix = "";
        } else {
            prefix = elements[0];
        }

        for (final Map.Entry<String, Set<String>> nsEntry : contextHere
                .entrySet()) {
            final Set<String> prefixes = nsEntry.getValue();

            if (prefixes.contains(prefix)) {
                final String first = prefixes.iterator().next();
                if (!first.equals(prefix)) {
                    final StringBuilder newName = new StringBuilder(first);
                    if (first.length()!=0) {
                        newName.append(':');
                    }
                    newName.append(node.getLocalName());
                    ((Element) node).setPrefix(first);
                }
            }
        }


        final NodeList children = node.getChildNodes();
        if (children != null) {
            for (int i = 0; i < children.getLength(); i++) {
                this.removeDoubleNS(children.item(i), contextHere);
            }
        }
    }

    @Test
    public void testSimpleTransformations() throws Exception {
        this.testbyXpath("<apply><plus/><cn>5</cn><ci>x</ci></apply>",
                "/m:mrow/m:mn[1]", "5", "/m:mrow/m:mo[1]", "+",
                "/m:mrow/m:mi[1]", "x");
        this.testbyXpath("<apply><times/><cn>5</cn><ci>x</ci></apply>",
                "/m:mrow/m:mn[1]", "5", "/m:mrow/m:mo[1]", "\u2062",
                "/m:mrow/m:mi[1]", "x");
    }

    @Test
    public void testSimpleTransformations2() throws Exception {
        this.testByDirectComparison(
                "<apply><plus/><cn>5</cn><ci>x</ci></apply>",
                "<mrow><mn>5</mn><mo>+</mo><mi>x</mi></mrow>");
        this.testByDirectComparison(
                "<apply><times/><cn>5</cn><ci>x</ci></apply>",
                "<mrow><mn>5</mn><mo>&#x2062;</mo><mi>x</mi></mrow>");
    }

    // Not yet functional.
    @Ignore
    @Test
    public void testContentParam() throws Exception {
        this.testbyXpath("<apply><times/><cn>5</cn><ci>x</ci></apply>",
                "/m:mrow/m:mn[1]", "5", "/m:mrow/m:mo[1]", "\u00d7",
                "/m:mrow/m:mi[1]", "x");
    }
}
