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

/* $Id: BasicConverter.java 172 2007-05-05 13:30:28Z maxberger $ */

package net.sourceforge.jeuclid;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Utility class for conversion from MathML to other formats.
 * <p>
 * This class supports easy conversion from a MathML or ODF file to any
 * supported output format.
 * <p>
 * Currently supported output formats:
 * <ul>
 * <li>image/svg+xml
 * <li>All images supported by ImageIO
 * </ul>
 * 
 * @author Max Berger
 * @version $Revision: 172 $
 */
public class SVGConverter extends BasicConverter implements ConverterAPI {
    /**
     * Mime type for SVG.
     */
    public static final String TYPE_SVG = "image/svg+xml";

    /**
     * File extension for SVG.
     */
    public static final String EXTENSION_SVG = "svg";

    /**
     * Logger for this class
     */
    private static final Log LOGGER = LogFactory.getLog(SVGConverter.class);

    /** Default constructor. */
    public SVGConverter() {
        // Empty on purpose.
    }

    /**
     * Converts an existing file from MathML or ODF to the given type.
     * 
     * @param doc
     *            input document.
     * @param outFile
     *            output file.
     * @param params
     *            parameter set to use for conversion.
     * @return true if the conversion was sucessful.
     * @throws IOException
     *             if an io error occured during read or write.
     */
    public boolean convert(final Document doc, final File outFile,
            final Map<ParameterKey, String> params) throws IOException {

        try {
            SVGConverter.LOGGER.info("Converting " + doc + " to " + outFile
                    + " ...");

            final MathBase base = MathMLParserSupport
                    .createMathBaseFromDocument(doc, params);

            if (SVGConverter.TYPE_SVG.equalsIgnoreCase(params
                    .get(ParameterKey.OutFileType))) {

                // Get a DOMImplementation
                final DOMImplementation domImpl = GenericDOMImplementation
                        .getDOMImplementation();

                // Create an instance of org.w3c.dom.Document
                final Document document = domImpl.createDocument(null,
                        SVGConverter.EXTENSION_SVG, null);

                // Create an instance of the SVG Generator
                final SVGGeneratorContext svgContext = SVGGeneratorContext
                        .createDefault(document);
                svgContext.setComment("Converted from MathML using JEuclid");
                final SVGGraphics2D svgGenerator = new SVGGraphics2D(
                        svgContext, true);
                // Ask the test to render into the SVG Graphics2D
                // implementation

                svgGenerator.setSVGCanvasSize(new Dimension((int) Math
                        .ceil(base.getWidth(svgGenerator)), (int) Math
                        .ceil(base.getHeight(svgGenerator))));
                base.paint(svgGenerator);

                svgGenerator.stream(outFile.getAbsolutePath());

            } else {
                super.convert(doc, outFile, params);
            }
        } catch (final SAXException ex) {
            SVGConverter.LOGGER.fatal(
                    "Failed to process: " + ex.getMessage(), ex);
            if (outFile != null) {
                outFile.delete();
            }
            return false;
        }
        return true;
    }

    /**
     * Retrieve a list of available mime types for conversion.
     * 
     * @return a List&lt;String&gt; containing all valid mime-types.
     */
    @Override
    public List<String> getAvailableOutfileTypes() {
        final List<String> fileTypes = super.getAvailableOutfileTypes();
        fileTypes.add(SVGConverter.TYPE_SVG);
        return fileTypes;
    }

    /**
     * Returns the file suffix suitable for the given mime type.
     * <p>
     * This function is not fully implemented yet
     * 
     * @param mimeType
     *            a mimetype, as returned by
     *            {@link #getAvailableOutfileTypes()}.
     * @return the three letter suffix common for this type.
     */
    @Override
    public String getSuffixForMimeType(final String mimeType) {
        if (SVGConverter.TYPE_SVG.equalsIgnoreCase(mimeType)) {
            return SVGConverter.EXTENSION_SVG;
        } else {
            return super.getSuffixForMimeType(mimeType);
        }
    }

    /**
     * Returns the MimeType for a given suffix.
     * 
     * @param suffix
     *            the suffix, e.g. png
     * @return the mime-type
     */
    @Override
    public String getMimeTypeForSuffix(final String suffix) {
        if (SVGConverter.EXTENSION_SVG.equalsIgnoreCase(suffix)) {
            return SVGConverter.TYPE_SVG;
        } else {
            return super.getMimeTypeForSuffix(suffix);
        }
    }

}
