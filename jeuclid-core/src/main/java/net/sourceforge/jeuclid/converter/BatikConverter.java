/*
 * Copyright 2007 - 2008 JEuclid, http://jeuclid.sf.net
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

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.sourceforge.jeuclid.LayoutContext;
import net.sourceforge.jeuclid.layout.JEuclidView;

import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * supports conversion to SVG output through Batik.
 * 
 * @version $Revision$
 */
public class BatikConverter implements ConverterPlugin {

    /**
     * Logger for this class
     */
    private static final Log LOGGER = LogFactory.getLog(BatikConverter.class);

    private final DOMImplementation domImplementation;

    BatikConverter(final DOMImplementation domImpl) {
        this.domImplementation = domImpl;
    }

    /** {@inheritDoc} */
    public Dimension convert(final Node doc, final LayoutContext context,
            final OutputStream outStream) throws IOException {
        final DocumentWithDimension svgDocDim = this.convert(doc, context);
        if (svgDocDim != null) {
            try {
                final Transformer transformer = TransformerFactory
                        .newInstance().newTransformer();
                final DOMSource source = new DOMSource(svgDocDim
                        .getDocument());
                final StreamResult result = new StreamResult(outStream);
                transformer.transform(source, result);
            } catch (final TransformerException e) {
                BatikConverter.LOGGER.warn(e);
            }
            return svgDocDim.getDimension();
        }
        return null;
    }

    /** {@inheritDoc} */
    public DocumentWithDimension convert(final Node doc,
            final LayoutContext context) {

        Document document = null;
        final String svgNS = "http://www.w3.org/2000/svg";
        document = this.domImplementation.createDocument(svgNS, "svg", null);
        if (document != null) {
            final SVGGeneratorContext svgContext = SVGGeneratorContext
                    .createDefault(document);
            svgContext.setComment("Converted from MathML using JEuclid");
            final SVGGraphics2D svgGenerator = new SVGGraphics2D(svgContext,
                    true);
            final JEuclidView view = new JEuclidView(doc, context,
                    svgGenerator);
            final int ascent = (int) Math.ceil(view.getAscentHeight());
            final int descent = (int) Math.ceil(view.getDescentHeight());
            final int height = ascent + descent;
            final int width = (int) Math.ceil(view.getWidth());
            final Dimension size = new Dimension(width, height);
            svgGenerator.setSVGCanvasSize(size);
            view.draw(svgGenerator, 0, ascent);
            document.replaceChild(svgGenerator.getRoot(), document
                    .getFirstChild());
            return new DocumentWithDimension(document, new Dimension(width,
                    height), descent);
        }
        return null;
    }
}
