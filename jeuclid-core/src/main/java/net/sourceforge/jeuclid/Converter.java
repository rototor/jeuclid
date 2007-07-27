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
import java.util.Vector;

import net.sourceforge.jeuclid.converter.ConverterRegistry;

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
 * <li>All images supported by ImageIO
 * <li>SVG if Batik is in the classpath.
 * <li>EMF, GIF, PDF, PS, SVG, SWF if FreeHEP is in the classpath.
 * </ul>
 * Conversion is done through the Converter class in the new interface. This
 * class will be deprecated some time after the 3.0 release. However, the new
 * API is not to be considered stable yet, so please use these functions for
 * the time being.
 * <p>
 * Replaced by {@link net.sourceforge.jeuclid.converter.Converter} and
 * {@link net.sourceforge.jeuclid.converter.ConverterRegistry}
 * 
 * @author Max Berger
 * @version $Revision$
 * @deprecated
 */
@Deprecated
public final class Converter {

    /**
     * Logger for this class
     */
    // unused
    // private static final Log LOGGER = LogFactory.getLog(Converter.class);
    private Converter() {
        // Empty on purpose.
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
     * @return true if the conversion was successful.
     * @throws IOException
     *             if an I/O error occurred during read or write.
     */
    public static boolean convert(final File inFile, final File outFile,
            final String outFileType) throws IOException {
        return net.sourceforge.jeuclid.converter.Converter.getConverter()
                .convert(inFile, outFile, outFileType) != null;
    }

    /**
     * Renders a document into an image.
     * 
     * @param doc
     *            DOM MathML document
     * @param params
     *            parameters
     * @return the rendered image
     * @throws SAXException
     *             if an error occurs reading the DOM document
     * @throws IOException
     *             if an I/O error occurred.
     */
    public static BufferedImage render(final Document doc,
            final MutableLayoutContext params) throws SAXException,
            IOException {
        return net.sourceforge.jeuclid.converter.Converter.getConverter()
                .render(
                        MathMLParserSupport.createMathBaseFromDocument(doc,
                                params));
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
     * @param params
     *            rendering parameters.
     * @return true if the conversion was successful.
     * @throws IOException
     *             if an I/O error occurred during read or write.
     */
    public static boolean convert(final File inFile, final File outFile,
            final String outFileType, final MutableLayoutContext params)
            throws IOException {
        return net.sourceforge.jeuclid.converter.Converter.getConverter()
                .convert(inFile, outFile, outFileType, params) != null;
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
     * @param params
     *            parameter set to use for conversion.
     * @return true if the conversion was successful.
     * @throws IOException
     *             if an I/O error occurred during read or write.
     */
    public static boolean convert(final Document doc, final File outFile,
            final String outFileType, final MutableLayoutContext params)
            throws IOException {
        return net.sourceforge.jeuclid.converter.Converter.getConverter()
                .convert(doc, outFile, outFileType, params) != null;
    }

    /**
     * Retrieve a list of available mime types for conversion.
     * 
     * @return a List&lt;String&gt; containing all valid mime-types.
     */
    public static List<String> getAvailableOutfileTypes() {
        return new Vector<String>(ConverterRegistry.getRegisty()
                .getAvailableOutfileTypes());
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
        return ConverterRegistry.getRegisty().getSuffixForMimeType(mimeType);
    }

    /**
     * Returns the MimeType for a given suffix.
     * 
     * @param suffix
     *            the suffix, e.g. png
     * @return the mime-type
     */
    public static String getMimeTypeForSuffix(final String suffix) {
        return ConverterRegistry.getRegisty().getMimeTypeForSuffix(suffix);
    }
}
