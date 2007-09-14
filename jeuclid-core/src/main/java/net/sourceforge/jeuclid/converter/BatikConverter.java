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

import java.awt.Dimension;
import java.io.IOException;
import java.io.OutputStream;

import net.sourceforge.jeuclid.LayoutContext;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * supports conversion to SVG output through Batik.
 * 
 * @todo This code contains some duplications which should be cleaned up
 * @version $Revision$
 */
public class BatikConverter implements ConverterPlugin {

    BatikConverter() {
        // Empty on purpose
    }

    /** {@inheritDoc} */
    public Dimension convert(final Node doc, final LayoutContext context,
            final OutputStream outStream) throws IOException {
        // // Get a DOMImplementation
        // final DOMImplementation domImpl = GenericDOMImplementation
        // .getDOMImplementation();
        //
        // // Create an instance of org.w3c.dom.Document
        // final Document document = domImpl.createDocument(null,
        // net.sourceforge.jeuclid.converter.Converter.EXTENSION_SVG,
        // null);
        //
        // // Create an instance of the SVG Generator
        // final SVGGeneratorContext svgContext = SVGGeneratorContext
        // .createDefault(document);
        // svgContext.setComment("Converted from MathML using JEuclid");
        //
        // final SVGGraphics2D svgGenerator = new SVGGraphics2D(svgContext,
        // true);
        // // Ask the test to render into the SVG Graphics2D
        // // implementation
        //
        // final Dimension size = new Dimension((int) Math.ceil(base
        // .getWidth(svgGenerator)), (int) Math.ceil(base
        // .getHeight(svgGenerator)));
        // svgGenerator.setSVGCanvasSize(size);
        // base.paint(svgGenerator);
        //
        // svgGenerator.stream(new OutputStreamWriter(outStream));
        // return size;
        return null;
    }

    /** {@inheritDoc} */
    public Document convert(final Node doc, final LayoutContext context) {

        // final SVGGraphics2D svgGenerator =
        // this.createSVGGenerator(mathBase);
        // mathBase.paint(svgGenerator);
        //
        // /*
        // * The following line is what /should/ work. However, there appears
        // to
        // * be some disconnect in Batik between the Document and the root
        // * Element. The root Element recognizes the Document as its
        // Document,
        // * but the Document does not see the root Element as its root
        // Element
        // * ...
        // */
        // // final Document svgDocument = svgGenerator.getDOMFactory();
        // final Element svgRoot = svgGenerator.getRoot();
        //
        // /*
        // * The variable svgDocument below is the same object as the one
        // * commented out above ...
        // */
        // final Document svgDocument = svgRoot.getOwnerDocument();
        //
        // /*
        // * ... however, the root element below is /NOT/ the same as the
        // * svgRoot variable above ...
        // */
        // final Element svgRoot2 = svgDocument.getDocumentElement();
        //
        // /*
        // * ... so we need to get the Document and the root element hooked
        // up.
        // */
        // svgDocument.removeChild(svgRoot2);
        // svgDocument.appendChild(svgRoot);
        //
        // return svgDocument;
        return null;
    }

    // /**
    // * Create a Batik SVG Generator for a given MathBase instance. NOTE:
    // This
    // * method was copied verbatim from
    // *
    // *
    // net.sourceforge.jeuclid.app.foprep.Processor#createSVGGenerator(MathBase)
    // * which is a private method, then modified to start with an SVGDocument
    // * instead of generic DOM Document.
    // *
    // * @param mathBase
    // * The JEuclid MathML document to be converted.
    // * @return The Batik SVG Generator.
    // * @throws GraphicException
    // * For error obtaining a new SVGDocument instance.
    // */
    // private SVGGraphics2D createSVGGenerator(final MathBase mathBase) {
    // final SVGDocument svgDocument = this.makeSvgDocument();
    // final SVGGeneratorContext svgContext = SVGGeneratorContext
    // .createDefault(svgDocument);
    // svgContext.setComment("Converted from MathML using JEuclid");
    // final SVGGraphics2D svgGenerator = new SVGGraphics2D(svgContext, true);
    //
    // final Dimension size = new Dimension((int) Math.ceil(mathBase
    // .getWidth(svgGenerator)), (int) Math.ceil(mathBase
    // .getHeight(svgGenerator)));
    // svgGenerator.setSVGCanvasSize(size);
    // return svgGenerator;
    // }
    //
    // private SVGDocument makeSvgDocument() {
    // final DOMImplementation impl = SVGDOMImplementation
    // .getDOMImplementation();
    // // final String svgNS = SvgUtil.SVG_NAMESPACE_URI;
    // final String svgNS = null;
    // final Document dom = impl.createDocument(svgNS, "svg", null);
    // return (SVGDocument) dom;
    // }

}
