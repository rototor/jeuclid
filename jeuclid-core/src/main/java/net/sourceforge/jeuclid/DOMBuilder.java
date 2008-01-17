/*
 * Copyright 2002 - 2007 JEuclid, http://jeuclid.sf.net
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

package net.sourceforge.jeuclid;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;

import net.sourceforge.jeuclid.elements.AbstractJEuclidElement;
import net.sourceforge.jeuclid.elements.JEuclidElementFactory;
import net.sourceforge.jeuclid.elements.generic.DocumentElement;
import net.sourceforge.jeuclid.elements.support.attributes.AttributeMap;
import net.sourceforge.jeuclid.elements.support.attributes.DOMAttributeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.mathml.MathMLElement;

/**
 * Builds a MathML tree from a given DOM tree.
 * 
 * @version $Revision$
 */
public final class DOMBuilder {
    /**
     * Logger for this class
     */
    private static final Log LOGGER = LogFactory.getLog(DOMBuilder.class);

    private static DOMBuilder domBuilder;

    private final Transformer contentTransformer;

    private final DOMImplementation domImplementation;

    private DOMBuilder() {
        Transformer t;
        DOMImplementation impl = null;
        try {
            t = TransformerFactory.newInstance().newTransformer(
                    new StreamSource(DOMBuilder.class
                            .getResourceAsStream("/content/mathmlc2p.xsl")));
        } catch (final TransformerException e) {
            DOMBuilder.LOGGER.warn(e.getMessage());
            t = null;
        }

        if (t != null) {
            try {
                impl = DOMImplementationRegistry.newInstance()
                        .getDOMImplementation("");
            } catch (final ClassCastException e) {
                DOMBuilder.LOGGER.warn(e.getMessage());
                impl = null;
            } catch (final ClassNotFoundException e) {
                DOMBuilder.LOGGER.warn(e.getMessage());
                impl = null;
            } catch (final InstantiationException e) {
                DOMBuilder.LOGGER.warn(e.getMessage());
                impl = null;
            } catch (final IllegalAccessException e) {
                DOMBuilder.LOGGER.warn(e.getMessage());
                impl = null;
            }
            if (impl == null) {
                t = null;
            }
        }

        this.contentTransformer = t;
        this.domImplementation = impl;
    }

    /**
     * @return the singleton instance of the DOMBuilder
     */
    public static synchronized DOMBuilder getDOMBuilder() {
        if (DOMBuilder.domBuilder == null) {
            DOMBuilder.domBuilder = new DOMBuilder();
        }
        return DOMBuilder.domBuilder;
    }

    /**
     * Constructs a builder.
     * <p>
     * This constructor needs a valid DOM Tree. To obtain a DOM tree, you may
     * use {@link MathMLParserSupport}.
     * 
     * @param node
     *            The MathML document. Can be an instance of Document, Element
     *            or DocumentFragment with Element child
     * @return the parsed Document
     * @see MathMLParserSupport
     */
    public DocumentElement createJeuclidDom(final Node node) {
        Node documentElement;
        if (node instanceof Document) {
            documentElement = ((Document) node).getDocumentElement();
        } else if (node instanceof Element) {
            documentElement = node;
        } else if (node instanceof DocumentFragment) {
            final Node child = node.getFirstChild();
            if (!(child instanceof Element)) {
                throw new IllegalArgumentException(
                        "Expected DocumentFragment with Element child");
            }
            documentElement = child;
        } else {
            throw new IllegalArgumentException(
                    "Unsupported node: "
                            + node
                            + ". Expected either Document, Element or DocumentFragment");
        }

        // TODO: This could be enabled / disabled with a switch?
        try {
            final DOMSource source = new DOMSource(documentElement);
            final Document d = this.domImplementation.createDocument(
                    documentElement.getNamespaceURI(), documentElement
                            .getNodeName(), null);
            final DOMResult result = new DOMResult(d.getDocumentElement());
            this.contentTransformer.transform(source, result);
            final Node realDE = d.getDocumentElement().getFirstChild();
            documentElement = realDE;
        } catch (final TransformerException e) {
            DOMBuilder.LOGGER.warn(e.getMessage());
        } catch (final NullPointerException e) {
            // Happens if the stylesheet was not loaded correctly
            DOMBuilder.LOGGER.warn(e.getMessage());
        } catch (final DOMException e) {
            DOMBuilder.LOGGER.warn(e.getMessage());
        }

        final DocumentElement rootElement = new DocumentElement();

        this.traverse(documentElement, rootElement);
        rootElement.fireChangeForSubTree();
        return rootElement;
    }

    /**
     * Creates a MathElement through traversing the DOM tree.
     * 
     * @param node
     *            Current element of the DOM tree.
     * @param parent
     *            Current element of the MathElement tree.
     * @param alignmentScope
     *            Alignment scope of elements.
     */
    private void traverse(final Node node, final Node parent) {
        if (node.getNodeType() != Node.ELEMENT_NODE) {
            return;
        }
        String tagname = node.getNodeName();
        final int posSeparator = tagname.indexOf(":");

        if (posSeparator >= 0) {
            tagname = tagname.substring(posSeparator + 1);
        }
        final AttributeMap attributes = new DOMAttributeMap(node
                .getAttributes());

        final MathMLElement element = JEuclidElementFactory.elementFromName(
                tagname, attributes);
        parent.appendChild(element);

        final NodeList childs = node.getChildNodes();

        for (int i = 0; i < childs.getLength(); i++) {
            final Node childNode = childs.item(i);
            final short childNodeType = childNode.getNodeType();
            if (childNodeType == Node.ELEMENT_NODE) {
                this.traverse(childNode, element);
            } else if (childNodeType == Node.TEXT_NODE) {
                ((AbstractJEuclidElement) element).addText(childNode
                        .getNodeValue());
            } else if (childNodeType == Node.ENTITY_REFERENCE_NODE
                    && childNode.hasChildNodes()) {
                final String entityValue = childNode.getFirstChild()
                        .getNodeValue();
                if (entityValue != null) {
                    ((AbstractJEuclidElement) element).addText(entityValue);
                }
            }
        }
    }

}
