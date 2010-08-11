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

import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;

import net.sourceforge.jeuclid.elements.generic.DocumentElement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Builds a MathML tree from a given DOM tree.
 * 
 * @version $Revision$
 */
@ThreadSafe
public final class DOMBuilder {
    /**
     * Logger for this class
     */
    private static final Log LOGGER = LogFactory.getLog(DOMBuilder.class);

    private static final class SingletonHolder {
        private static final DOMBuilder INSTANCE = new DOMBuilder();

        private SingletonHolder() {
        }
    }

    @GuardedBy("itself")
    private final Transformer contentTransformer;

    @GuardedBy("itself")
    private final Transformer identityTransformer;

    @GuardedBy("itself")
    private final Transformer namespaceTransformer;

    /**
     * Default constructor.
     */
    protected DOMBuilder() {
        this.identityTransformer = this.createIdentityTransformer();
        this.contentTransformer = this.createTransformer(
                "/net/sourceforge/jeuclid/content/mathmlc2p.xsl",
                this.identityTransformer);
        this.namespaceTransformer = this.createTransformer(
                "/net/sourceforge/jeuclid/addMathMLNamespace.xsl",
                this.identityTransformer);
    }

    private Transformer createIdentityTransformer() {
        Transformer t;
        try {
            t = TransformerFactory.newInstance().newTransformer();
        } catch (final TransformerException e) {
            DOMBuilder.LOGGER.warn(e.getMessage());
            t = null;
            assert false;
        }
        return t;
    }

    private Transformer createTransformer(final String sourceFile,
            final Transformer fallback) {
        Transformer t;
        try {
            t = TransformerFactory.newInstance().newTemplates(
                    new StreamSource(DOMBuilder.class
                            .getResourceAsStream(sourceFile))).newTransformer();
        } catch (final TransformerException e) {
            DOMBuilder.LOGGER.warn(e.getMessage());
            t = fallback;
        }
        return t;
    }

    /**
     * @return the singleton instance of the DOMBuilder
     */
    public static DOMBuilder getInstance() {
        return DOMBuilder.SingletonHolder.INSTANCE;
    }

    /**
     * use {@link #getInstance()} instead.
     * 
     * @return see {@link #getInstance()}
     * @deprecated use {@link #getInstance()} instead.
     */
    @Deprecated
    public static DOMBuilder getDOMBuilder() {
        return DOMBuilder.getInstance();
    }

    /**
     * Constructs a builder with content math support.
     * 
     * @param node
     *            The MathML document. Can be an instance of Document, Element
     *            or DocumentFragment with Element child
     * @return the parsed Document
     * @see #createJeuclidDom(Node, boolean)
     */
    public DocumentElement createJeuclidDom(final Node node) {
        return this.createJeuclidDom(node, true);
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
     * @param supportContent
     *            if set to true, content Math will be supported. This impacts
     *            performance.
     * @return the parsed Document
     * @see MathMLParserSupport
     */
    public DocumentElement createJeuclidDom(final Node node,
            final boolean supportContent) {
        return this.createJeuclidDom(node, supportContent, false);
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
     * @param supportContent
     *            if set to true, content Math will be supported. This impacts
     *            performance.
     * @param addNamespace
     *            if set to true, the MathML namespace will be added to all
     *            elements.
     * @return the parsed Document
     * @see MathMLParserSupport
     */
    public DocumentElement createJeuclidDom(final Node node,
            final boolean supportContent, final boolean addNamespace) {
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
            throw new IllegalArgumentException("Unsupported node: " + node
                    + ". Expected either Document, Element or DocumentFragment");
        }

        if (addNamespace) {
            documentElement = this.applyTransform(documentElement,
                    this.namespaceTransformer);
        }

        final DocumentElement d;
        if (supportContent) {
            d = this.applyTransform(documentElement, this.contentTransformer);
        } else {
            d = this.applyTransform(documentElement, this.identityTransformer);
        }
        return d;
    }

    private DocumentElement applyTransform(final Node src,
            final Transformer transformer) {
        DocumentElement d;
        try {
            final DOMSource source = new DOMSource(src);
            d = new DocumentElement();
            final DOMResult result = new DOMResult(d);
            synchronized (transformer) {
                transformer.transform(source, result);
            }
        } catch (final TransformerException e) {
            d = null;
            DOMBuilder.LOGGER.warn(e.getMessage());
        } catch (final NullPointerException e) {
            d = null;
            // Happens if the stylesheet was not loaded correctly
            DOMBuilder.LOGGER.warn(e.getMessage());
        } catch (final DOMException e) {
            d = null;
            DOMBuilder.LOGGER.warn(e.getMessage());
        }
        return d;
    }

}
