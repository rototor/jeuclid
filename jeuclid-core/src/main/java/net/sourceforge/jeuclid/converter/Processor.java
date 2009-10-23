/*
 * Copyright 2007 - 2007 JEuclid, http://jeuclid.sf.net
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

package net.sourceforge.jeuclid.converter;

import java.io.IOException;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;

import net.sourceforge.jeuclid.LayoutContext;
import net.sourceforge.jeuclid.context.LayoutContextImpl;
import net.sourceforge.jeuclid.converter.ConverterPlugin.DocumentWithDimension;
import net.sourceforge.jeuclid.elements.AbstractJEuclidElement;
import net.sourceforge.jeuclid.elements.generic.MathImpl;
import net.sourceforge.jeuclid.parser.Parser;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Contains the actual processing routines.
 * <p>
 * To use this class obtain an instance of the Processor singleton instance.
 * Then use the {@link #process(Source, Result)} function to process your
 * Document.
 * <p>
 * This will replace all occurrences of MathML within fo:instream tags by the
 * equivalent SVG code. It will also add a baseline-shift attribute so that the
 * formula is in line with the rest of the text.
 * 
 * @version $Revision$
 */
public final class Processor {

    private static final class SingletonHolder {
        private static final Processor INSTANCE = new Processor();

        private SingletonHolder() {
        }
    }

    /**
     * Logger for this class
     */
    private static final Log LOGGER = LogFactory.getLog(Processor.class);

    // private static final String NAMESPACE_HTML =
    // "http://www.w3.org/1999/xhtml";

    private final Transformer transformer;

    /**
     * Default constructor.
     */
    protected Processor() {
        Transformer t;
        try {
            t = TransformerFactory.newInstance().newTransformer();
        } catch (final TransformerException e) {
            t = null;
            Processor.LOGGER.warn(e.getMessage());
            assert false;
        }
        this.transformer = t;
    }

    /**
     * Retrieve the processor singleton object.
     * 
     * @return the Processor.
     */
    public static Processor getInstance() {
        return Processor.SingletonHolder.INSTANCE;
    }

    /**
     * use {@link #getInstance()} instead.
     * 
     * @return see {@link #getInstance()}
     * @throws TransformerException
     *             see {@link #getInstance()}
     * @deprecated use {@link #getInstance()} instead.
     */
    @Deprecated
    public static Processor getProcessor() throws TransformerException {
        return Processor.getInstance();
    }

    /**
     * Pre-process a .fo file.
     * 
     * @param inputSource
     *            Input File
     * @param result
     *            Output File
     * @param context
     *            LayoutContext.
     * @throws TransformerException
     *             an error occurred during the processing.
     */
    public void process(final Source inputSource, final Result result,
            final LayoutContext context) throws TransformerException {
        Processor.LOGGER.info("Processing " + inputSource.getSystemId()
                + " to " + result.getSystemId());
        try {
            final Node doc = Parser.getInstance().parse(inputSource);
            this.processSubtree(doc, context);
            final DOMSource source = new DOMSource(doc);
            this.transformer.transform(source, result);
        } catch (final IOException e) {
            throw new TransformerException("IOException", e);
        } catch (final SAXException e) {
            throw new TransformerException("SAXException", e);
        }
    }

    /**
     * Pre-process a .fo file.
     * 
     * @param inputSource
     *            Input File
     * @param result
     *            Output File
     * @throws TransformerException
     *             an error occurred during the processing.
     */
    public void process(final Source inputSource, final Result result)
            throws TransformerException {
        this.process(inputSource, result, LayoutContextImpl
                .getDefaultLayoutContext());
    }

    private void processSubtree(final Node node, final LayoutContext context) {
        if (AbstractJEuclidElement.URI.equals(node.getNamespaceURI())
                && MathImpl.ELEMENT.equals(node.getLocalName())) {

            final DocumentWithDimension svgdocdim = Converter
                    .getInstance()
                    .convert(
                            node,
                            net.sourceforge.jeuclid.converter.Converter.TYPE_SVG,
                            context);

            final float baselinePercent = -(svgdocdim.getBaseline() / (float) svgdocdim
                    .getDimension().getHeight()) * 100f;

            final Node parent = node.getParentNode();
            if ("http://www.w3.org/1999/XSL/Format".equals(parent
                    .getNamespaceURI())
                    && "instream-foreign-object".equals(parent.getLocalName())) {
                final Element pElement = (Element) parent;
                pElement
                        .setAttribute("alignment-adjust", baselinePercent + "%");
            }
            this.safeReplaceChild(parent, node, svgdocdim.getDocument()
                    .getFirstChild());
        } else {
            this.processChildren(node, context);
            // TODO: This is an IE-Fix, but does not work yet.
            // final Node parent = node.getParentNode();
            // if ((parent != null)
            // && (Processor.NAMESPACE_HTML.equals(parent
            // .getNamespaceURI()))
            // && ("html".equals(parent.getLocalName()))
            // && ("head".equals(node.getLocalName()))) {
            // ((Element) parent).setAttribute("xmlns:svg",
            // "http://www.w3.org/2000/svg");
            // final Document ownerDoc = node.getOwnerDocument();
            // final Element objectElement = ownerDoc
            // .createElement("object");
            // objectElement.setAttribute("id", "AdobeSVG");
            // objectElement.setAttribute("classid",
            // "clsid:78156a80-c6a1-4bbf-8e6a-3cd390eeb4e2");
            // node.appendChild(objectElement);
            // final ProcessingInstruction pi = ownerDoc
            // .createProcessingInstruction("import",
            // "namespace=\"svg\" implementation=\"#AdobeSVG\"");
            // node.appendChild(pi);
            // }
        }
    }

    private void safeReplaceChild(final Node parent, final Node oldChild,
            final Node newChild) {
        try {
            final DOMSource source = new DOMSource(newChild);
            final DOMResult result = new DOMResult(parent);

            this.transformer.transform(source, result);
        } catch (final TransformerException e) {
            Processor.LOGGER.warn("TranformerException: " + e.getMessage());
        }
        parent.removeChild(oldChild);
    }

    private void processChildren(final Node node, final LayoutContext context) {
        final NodeList childList = node.getChildNodes();
        if (childList != null) {
            for (int i = 0; i < childList.getLength(); i++) {
                final Node child = childList.item(i);
                this.processSubtree(child, context);
            }
        }
    }

}
