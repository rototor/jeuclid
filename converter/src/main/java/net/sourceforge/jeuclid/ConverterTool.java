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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
 * @author Erik Putrycz
 * @version $Revision$
 */
public final class ConverterTool {
    /**
     * Logger for this class
     */
    private static final Log LOGGER = LogFactory.getLog(ConverterTool.class);

    private static Converter convInstance;

    static {
        try {
            Thread.currentThread().getContextClassLoader().loadClass(
                    "org.apache.batik.svggen.SVGGraphics2D");
            ConverterTool.LOGGER.debug("Using SVG capable converter");
            ConverterTool.convInstance = new SVGConverter();
        } catch (final ClassNotFoundException e) {
            // load the basic converter
            ConverterTool.convInstance = new BasicConverter();
            ConverterTool.LOGGER
                    .debug("Using basic converter without SVG capabilities");
        }
    }

    /**
     * Default constructor.
     */
    private ConverterTool() {
        // Empty on purpose
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
        return ConverterTool.convInstance.convert(inFile, outFile,
                outFileType);
    }

    /**
     * Renders a document into an image.
     * 
     * @param doc
     *            DOM MathML document
     * @param params
     *            parameters
     * @return a BufferedImage containing rendered version of the document.
     * @throws SAXException
     *             if the Document could not be parsed as MathML.
     * @throws IOException
     *             if an I/O error occurred during read or write.
     */
    public BufferedImage render(final Document doc,
            final Map<ParameterKey, String> params) throws SAXException,
            IOException {
        return ConverterTool.convInstance.render(doc, params);
    }

    /**
     * Converts an existing file from MathML or ODF to the given type.
     * 
     * @param inFile
     *            input file.
     * @param outFile
     *            output file.
     * @param params
     *            rendering parameters.
     * @return true if the conversion was successful.
     * @throws IOException
     *             if an I/O error occurred during read or write.
     */
    public static boolean convert(final File inFile, final File outFile,
            final Map<ParameterKey, String> params) throws IOException {
        return ConverterTool.convInstance.convert(inFile, outFile, params);
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
     * @return true if the conversion was successful.
     * @throws IOException
     *             if an I/O error occurred during read or write.
     */
    public static boolean convert(final Document doc, final File outFile,
            final Map<ParameterKey, String> params) throws IOException {
        return ConverterTool.convInstance.convert(doc, outFile, params);
    }

    /**
     * Retrieve a list of available mime types for conversion.
     * 
     * @return a List&lt;String&gt; containing all valid mime-types.
     */
    public static List<String> getAvailableOutfileTypes() {
        return ConverterTool.convInstance.getAvailableOutfileTypes();
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
        return ConverterTool.convInstance.getSuffixForMimeType(mimeType);
    }

    /**
     * Returns the MimeType for a given suffix.
     * 
     * @param suffix
     *            the suffix, e.g. png
     * @return the mime-type
     */
    public static String getMimeTypeForSuffix(final String suffix) {
        return ConverterTool.convInstance.getMimeTypeForSuffix(suffix);
    }
}
