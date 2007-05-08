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

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Generic interface for all MathML converters.
 * 
 * @author Erik Putrycz
 * @author Max Berger
 * @version $Revision$
 */
public interface Converter {

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
    boolean convert(final File inFile, final File outFile,
            final String outFileType) throws IOException;

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
    boolean convert(final File inFile, final File outFile,
            final Map<ParameterKey, String> params) throws IOException;

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
    boolean convert(final Document doc, final File outFile,
            final Map<ParameterKey, String> params) throws IOException;

    /**
     * Renders a given MathML Document into a BufferedImage.
     * 
     * @param doc
     *            the document
     * @param params
     *            rendering parameters
     * @return a BufferedImage containing the rendered formula.
     * @throws SAXException
     *             if the Document could not be parsed as MathML.
     * @throws IOException
     *             if an I/O error occurred during read or write.
     */
    BufferedImage render(final Document doc,
            final Map<ParameterKey, String> params) throws SAXException,
            IOException;

    /**
     * Retrieve a list of available mime types for conversion.
     * 
     * @return a List&lt;String&gt; containing all valid mime-types.
     */
    List<String> getAvailableOutfileTypes();

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
    String getSuffixForMimeType(final String mimeType);

    /**
     * Returns the MimeType for a given suffix.
     * 
     * @param suffix
     *            the suffix, e.g. png
     * @return the mime-type
     */
    String getMimeTypeForSuffix(final String suffix);

}
