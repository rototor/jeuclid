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

package net.sourceforge.jeuclid.fop;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.PropertyList;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;

import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.dom.svg.SVGDOMImplementation;

import net.sourceforge.jeuclid.DOMBuilder;
import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.DOMBuilder;

/**
 * Defines the top-level element for MathML.
 */
public class MathMLElement extends MathMLObj {

    private Document svgDoc = null;

    private float width;

    private float height;

    private boolean converted = false;

    /** {@inheritDoc} */
    public MathMLElement(FONode parent) {
        super(parent);
    }

    /** {@inheritDoc} */
    public void processNode(String elementName, Locator locator,
            Attributes attlist, PropertyList propertyList)
            throws FOPException {
        super.processNode(elementName, locator, attlist, propertyList);
        createBasicDocument();
    }

    /**
     * Converts the MathML to SVG.
     */
    public void convertToSVG() {
        try {
            if (!converted) {
                converted = true;
                String fontname = "Helvetica";
                int fontstyle = 0;
                // int inlinefontstyle = 0;
                int displayfontsize = 12;
                int inlinefontsize = 12;

                MathBase base = new MathBase(MathBase.getDefaultParameters());
                new DOMBuilder(doc, base);

                base.setDebug(false);

                SVGCreated svgc = createSVG(base);
                svgDoc = svgc.getDocument();

                width = base.getWidth(svgc.getGraphics());
                height = base.getHeight(svgc.getGraphics());

                doc = svgDoc;
            }
        } catch (Throwable t) {
            getLogger().error("Could not convert MathML to SVG", t);
            width = 0;
            height = 0;
        }

    }

    public static class SVGCreated {
        private final Document document;

        private final Graphics2D graphics;

        public SVGCreated(final Document d, final Graphics2D g) {
            this.document = d;
            this.graphics = g;
        }

        public Document getDocument() {
            return this.document;
        }

        public Graphics2D getGraphics() {
            return this.graphics;
        }
    }

    /**
     * Create the SVG from MathML.
     * 
     * @param base
     *            the root element
     * @return the DOM document containing SVG
     */
    public static SVGCreated createSVG(MathBase base) {

        DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
        String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
        Document svgdocument = impl.createDocument(svgNS, "svg", null);

        SVGGraphics2D g = new SVGGraphics2D(svgdocument);

        g.setSVGCanvasSize(new Dimension((int) Math.ceil(base.getWidth(g)),
                (int) Math.ceil(base.getHeight(g))));

        // g.setColor(Color.white);
        // g.fillRect(0, 0, base.getWidth(), base.getHeight());
        g.setColor(Color.black);

        base.paint(g);

        // if (antialiasing)
        // element.setAttribute("text-rendering", "optimizeLegibility");
        // else
        // element.setAttribute("text-rendering", "geometricPrecision");

        // this should be done in a better way
        Element root = g.getRoot();
        svgdocument = impl.createDocument(svgNS, "svg", null);
        Node node = svgdocument.importNode(root, true);
        ((org.apache.batik.dom.svg.SVGOMDocument) svgdocument)
                .getRootElement().appendChild(node);

        return new SVGCreated(svgdocument, g);

    }

    /** @see org.apache.fop.fo.XMLObj#getDOMDocument() */
    public Document getDOMDocument() {
        convertToSVG();
        return doc;
    }

    /**
     * @see org.apache.fop.fo.FONode#getNamespaceURI()
     */
    public String getNamespaceURI() {
        if (svgDoc == null) {
            return MathMLElementMapping.NAMESPACE;
        }
        return "http://www.w3.org/2000/svg";
    }

    /**
     * @see org.apache.fop.fo.XMLObj#getDimension(Point2D)
     */
    public Point2D getDimension(Point2D view) {
        convertToSVG();
        return new Point2D.Float(width, height);
    }
}
