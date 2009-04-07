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

/* $Id: DOMBuilder.java 827 2008-08-28 12:30:05Z maxberger $ */

package euclid;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import euclid.elements.generic.DocumentElement;

/**
 * Builds a MathML tree from a given DOM tree.
 * 
 * @version $Revision: 827 $
 */
public final class DOMBuilder {

    private static final class SingletonHolder {
        private static final DOMBuilder INSTANCE = new DOMBuilder();

        private SingletonHolder() {
        }
    }

    private final Transformer contentTransformer;

    private final Transformer identityTransformer;

    /**
     * Default constructor.
     */
    protected DOMBuilder() {
        this.identityTransformer = this.createIdentityTransformer();
        this.contentTransformer = this
                .createContentTransformer(this.identityTransformer);
    }

    private Transformer createIdentityTransformer() {
        Transformer t;
        try {
            t = TransformerFactory.newInstance().newTransformer();
        } catch (final TransformerException e) {
            e.printStackTrace();
            t = null;
            assert false;
        }
        return t;
    }

    private Transformer createContentTransformer(final Transformer fallback) {
        Transformer t;
        try {
            t = TransformerFactory.newInstance().newTemplates(
                    new StreamSource(DOMBuilder.class
                            .getResourceAsStream("/mathmlc2p.xsl")))
                    .newTransformer();
        } catch (final TransformerException e) {
        	e.printStackTrace();
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
     * @deprecated
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
        Node documentElement;
        if (node instanceof Document) {
//            System.out.println("DombuilderCreateJeuclidDomFormDocument");
        	documentElement = ((Document) node).getDocumentElement();
        } else if (node instanceof Element) {
//        	System.out.println("createJeuclidDomFormElement");
        	documentElement = node;
        } else if (node instanceof DocumentFragment) {
//        	System.out.println("createJeuclidDomFormFragment");
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
        DocumentElement d = null;
        try {
            final DOMSource source = new DOMSource(documentElement);
            d = new DocumentElement();
            final DOMResult result = new DOMResult(d);
            final Transformer t;
            if (supportContent) {
                t = this.contentTransformer;
            } else {
                t = this.identityTransformer;
            }
            synchronized (t) {
                t.transform(source, result);
            }
        } catch (final TransformerException e) {
            d = null;
            e.printStackTrace();
        } catch (final NullPointerException e) {
            d = null;
            // Happens if the stylesheet was not loaded correctly
            e.printStackTrace();
        } catch (final DOMException e) {
            d = null;
            e.printStackTrace();
        }
        return d;
    }

}
