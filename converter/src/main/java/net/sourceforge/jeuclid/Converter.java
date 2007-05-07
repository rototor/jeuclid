package net.sourceforge.jeuclid;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

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
     * @return true if the conversion was sucessful.
     * @throws IOException
     *             if an io error occured during read or write.
     */
    public abstract boolean convert(final File inFile, final File outFile,
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
     * @return true if the conversion was sucessful.
     * @throws IOException
     *             if an io error occured during read or write.
     */
    public abstract boolean convert(final File inFile, final File outFile,
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
     * @return true if the conversion was sucessful.
     * @throws IOException
     *             if an io error occured during read or write.
     */
    public abstract boolean convert(final Document doc, final File outFile,
            final Map<ParameterKey, String> params) throws IOException;

    /**
     * @param base
     * @return
     * @throws IOException
     * @throws SAXException
     */
    public abstract BufferedImage render(final Document doc,
            final Map<ParameterKey, String> params) throws SAXException,
            IOException;

    /**
     * Retrieve a list of available mime types for conversion.
     * 
     * @return a List&lt;String&gt; containing all valid mime-types.
     */
    public abstract List<String> getAvailableOutfileTypes();

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
    public abstract String getSuffixForMimeType(final String mimeType);

    /**
     * Returns the MimeType for a given suffix.
     * 
     * @param suffix
     *            the suffix, e.g. png
     * @return the mime-type
     */
    public abstract String getMimeTypeForSuffix(final String suffix);

}