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

package net.sourceforge.jeuclid.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;

import net.sourceforge.jeuclid.MathBase;

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
 * @version $Revision$ $Date$
 */
public final class Converter {
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
    private static final Log LOGGER = LogFactory.getLog(Converter.class);

    private Converter() {
    }

    /**
     * Converts an existing file from MathML or ODF to the given type.
     * 
     * @param inFile
     *            input file.
     * @param outFile
     *            output file.
     * @param outFileType
     *            mimetype for the output file.
     * @return true if the conversion was sucessful.
     * @throws IOException
     *             if an io error occured during read or write.
     */
    public static boolean convert(final File inFile, final File outFile,
            final String outFileType) throws IOException {
        Document doc;
        try {
            doc = ODFSupport.parseFile(inFile);
            return Converter.convert(doc, outFile, outFileType);
        } catch (final SAXException e) {
            Converter.LOGGER.error("Failed to parse file:" + inFile, e);
            return false;
        }
    }

    /**
     * Converts an existing file from MathML or ODF to the given type.
     * 
     * @param doc
     *            input document.
     * @param outFile
     *            output file.
     * @param outFileType
     *            mimetype for the output file.
     * @return true if the conversion was sucessful.
     * @throws IOException
     *             if an io error occured during read or write.
     */
    public static boolean convert(final Document doc, final File outFile,
            final String outFileType) throws IOException {
        final Map<ParameterKey, String> params = MathBase
                .getDefaultParameters();
        params.put(ParameterKey.OutFileType, outFileType);
        return Converter.convert(doc, outFile, params);
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
    public static boolean convert(final Document doc, final File outFile,
            final Map<ParameterKey, String> params) throws IOException {

        try {
            Converter.LOGGER.info("Converting " + doc + " to " + outFile
                    + " ...");

            final MathBase base = ODFSupport.createMathBaseFromDocument(doc,
                    params);

            if (Converter.TYPE_SVG.equalsIgnoreCase(params
                    .get(ParameterKey.OutFileType))) {

                // Get a DOMImplementation
                final DOMImplementation domImpl = GenericDOMImplementation
                        .getDOMImplementation();

                // Create an instance of org.w3c.dom.Document
                final Document document = domImpl.createDocument(null,
                        Converter.EXTENSION_SVG, null);

                // Create an instance of the SVG Generator
                final SVGGeneratorContext svgContext = SVGGeneratorContext
                        .createDefault(document);
                svgContext.setComment("Converted from MathML using JEuclid");
                final SVGGraphics2D svgGenerator = new SVGGraphics2D(
                        svgContext, true);
                // Ask the test to render into the SVG Graphics2D
                // implementation

                svgGenerator
                        .setSVGCanvasSize(new Dimension(base
                                .getWidth(svgGenerator), base
                                .getHeight(svgGenerator)));
                base.paint(svgGenerator);

                svgGenerator.stream(outFile.getAbsolutePath());

            } else {

                final Iterator it = ImageIO.getImageWritersByMIMEType(params
                        .get(ParameterKey.OutFileType));
                if (it.hasNext()) {
                    final ImageWriter writer = (ImageWriter) it.next();
                    final BufferedImage tempimage = new BufferedImage(1, 1,
                            BufferedImage.TYPE_INT_ARGB);
                    final Graphics2D tempg = (Graphics2D) tempimage
                            .getGraphics();
                    final int width = base.getWidth(tempg);
                    final int height = base.getHeight(tempg);

                    final BufferedImage image = new BufferedImage(width,
                            height, BufferedImage.TYPE_INT_ARGB);
                    final Graphics2D g = (Graphics2D) image.createGraphics();

                    final Color transparency = new Color(255, 255, 255, 0);

                    g.setColor(transparency);
                    g.fillRect(0, 0, width, height);
                    g.setColor(Color.black);

                    base.paint(g);

                    final ImageOutputStream ios = new FileImageOutputStream(
                            outFile);
                    writer.setOutput(ios);
                    writer.write(image);
                    ios.close();

                } else {
                    Converter.LOGGER.fatal("Unsupported output type: "
                            + params.get(ParameterKey.OutFileType));
                    return false;
                }
            }
        } catch (final SAXException ex) {
            Converter.LOGGER.fatal("Failed to process: " + ex.getMessage(),
                    ex);
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
     * @return a List&ltString&gt; containing all valid mime-types.
     */
    public static List<String> getAvailableOutfileTypes() {
        final List<String> fileTypes = new Vector<String>(Arrays
                .asList(ImageIO.getWriterMIMETypes()));
        fileTypes.add(Converter.TYPE_SVG);
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
    public static String getSuffixForMimeType(final String mimeType) {
        if (Converter.TYPE_SVG.equalsIgnoreCase(mimeType)) {
            return Converter.EXTENSION_SVG;
        }
        // TODO: This is only partially complete! Should use ImageIO to
        // discover extensions!
        return "png";
    }

}
