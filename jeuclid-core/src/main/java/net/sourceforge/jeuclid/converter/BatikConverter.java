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
import java.io.OutputStreamWriter;

import net.sourceforge.jeuclid.MathBase;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

/**
 * supports conversion to SVG output through Batik.
 * 
 * @author Max Berger
 * @version $Revision$
 */
public class BatikConverter implements ConverterPlugin {

    BatikConverter() {
        // Empty on purpose
    }

    /** {@inheritDoc} */
    public Dimension convert(final MathBase base, final OutputStream outStream)
            throws IOException {
        // Get a DOMImplementation
        final DOMImplementation domImpl = GenericDOMImplementation
                .getDOMImplementation();

        // Create an instance of org.w3c.dom.Document
        final Document document = domImpl.createDocument(null,
                net.sourceforge.jeuclid.converter.Converter.EXTENSION_SVG, null);

        // Create an instance of the SVG Generator
        final SVGGeneratorContext svgContext = SVGGeneratorContext
                .createDefault(document);
        svgContext.setComment("Converted from MathML using JEuclid");
        final SVGGraphics2D svgGenerator = new SVGGraphics2D(svgContext, true);
        // Ask the test to render into the SVG Graphics2D
        // implementation

        final Dimension size = new Dimension(
                (int) Math.ceil(base.getWidth(svgGenerator)), 
                (int) Math.ceil(base.getHeight(svgGenerator)));
        svgGenerator.setSVGCanvasSize(size);
        base.paint(svgGenerator);

        svgGenerator.stream(new OutputStreamWriter(outStream));
        return size;
    }

}
