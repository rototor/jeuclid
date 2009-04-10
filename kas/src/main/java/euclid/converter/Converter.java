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

package euclid.converter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import euclid.LayoutContext;
import euclid.MathMLParserSupport;
import euclid.MutableLayoutContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import euclid.context.LayoutContextImpl;
import euclid.converter.ConverterPlugin.DocumentWithDimension;
import euclid.layout.JEuclidView;

/**
 * Generic converter which uses the registry to do its conversions.
 * 
 * @version $Revision$
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

    private static final String UNSUPPORTED_OUTPUT_TYPE = "Unsupported output type: ";

    private static final class SingletonHolder {
        private static final Converter INSTANCE = new Converter();

        private SingletonHolder() {
        }
    }

    /**
     * Logger for this class
     */
    private static final Log LOGGER = LogFactory.getLog(Converter.class);

    /**
     * Default constructor.
     */
    protected Converter() {
        // Empty on purpose.
    }

    /**
     * Retrieve an instance of the converter singleton class.
     * 
     * @return a Converter object.
     */
    public static Converter getInstance() {
        return Converter.SingletonHolder.INSTANCE;
    }

    /**
     * @return Converter instance
     * @deprecated use {@link #getInstance()} instead.
     */
    @Deprecated
    public static Converter getConverter() {
        return Converter.getInstance();
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
     * @return Dimension of converted image upon success, null otherwise
     * @throws IOException
     *             if an I/O error occurred during read or write.
     */
    public Dimension convert(final File inFile, final File outFile,
            final String outFileType) throws IOException {
        final MutableLayoutContext params = new LayoutContextImpl(
                LayoutContextImpl.getDefaultLayoutContext());
        return this.convert(inFile, outFile, outFileType, params);
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
     * @return Dimension of converted image upon success, null otherwise
     * @throws IOException
     *             if an I/O error occurred during read or write.
     */
    public Dimension convert(final File inFile, final File outFile,
            final String outFileType, final MutableLayoutContext params)
            throws IOException {
        Document doc;
        try {
            doc = MathMLParserSupport.parseFile(inFile);
            return this.convert(doc, outFile, outFileType, params);
        } catch (final SAXException e) {
            Converter.LOGGER.error("Failed to parse file:" + inFile, e);
            return null;
        }
    }

    /**
     * Converts an existing document from MathML to the given type and store it
     * in a file.
     * 
     * @param doc
     *            input document. See
     *            {@link net.sourceforge.jeuclid.DOMBuilder#DOMBuilder(Node, MathBase)}
     *            for the list of valid node types.
     * @param outFile
     *            output file.
     * @param outFileType
     *            mimetype for the output file.
     * @param params
     *            parameter set to use for conversion.
     * @return Dimension of converted image upon success, null otherwise
     * @throws IOException
     *             if an I/O error occurred during read or write.
     */
    public Dimension convert(final Node doc, final File outFile,
            final String outFileType, final MutableLayoutContext params)
            throws IOException {

        final OutputStream outStream = new BufferedOutputStream(
                new FileOutputStream(outFile));
        final Dimension result = this.convert(doc, outStream, outFileType,
                params);
        if (result == null) {
            if (!outFile.delete()) {
                Converter.LOGGER.debug("Could not delete " + outFile);
            }
        } else {
            // should be closed by wrapper image streams, but just in case...
            try {
                outStream.close();
            } catch (final IOException e) {
                Converter.LOGGER.debug(e);
            }
        }
        return result;
    }

    /**
     * Converts an existing document from MathML to the given XML based type and
     * store it in a DOM document.
     * 
     * @param doc
     *            input document. See
     *            {@link net.sourceforge.jeuclid.DOMBuilder#DOMBuilder(Node, MathBase)}
     *            for the list of valid node types.
     * @param outFileType
     *            mimetype for the output file.
     * @param params
     *            parameter set to use for conversion.
     * @return an instance of Document, or the appropriate subtype for this
     *         format (e.g. SVGDocument). If conversion is not supported to this
     *         type, it may return null.
     */
    public DocumentWithDimension convert(final Node doc,
            final String outFileType, final LayoutContext params) {
        final ConverterPlugin plugin = ConverterRegistry.getInstance()
                .getConverter(outFileType);
        DocumentWithDimension result = null;
        if (plugin != null) {
            result = plugin.convert(doc, params);
        }
        if (result == null) {
            Converter.LOGGER.fatal(Converter.UNSUPPORTED_OUTPUT_TYPE
                    + outFileType);
        }
        return result;
    }

    /**
     * Converts an existing file from MathML or ODF to the given type.
     * 
     * @param doc
     *            input document. See
     *            {@link net.sourceforge.jeuclid.DOMBuilder#DOMBuilder(Node, MathBase)}
     *            for the list of valid node types.
     * @param outStream
     *            output stream.
     * @param outFileType
     *            mimetype for the output file.
     * @param params
     *            parameter set to use for conversion.
     * @return Dimension of converted image upon success, null otherwise
     * @throws IOException
     *             if an I/O error occurred during read or write.
     */
    public Dimension convert(final Node doc, final OutputStream outStream,
            final String outFileType, final MutableLayoutContext params)
            throws IOException {
        final ConverterPlugin plugin = ConverterRegistry.getInstance()
                .getConverter(outFileType);
        Dimension result = null;
        if (plugin == null) {
            Converter.LOGGER.fatal(Converter.UNSUPPORTED_OUTPUT_TYPE
                    + outFileType);
        } else {
            try {
                result = plugin.convert(doc, params, outStream);
            } catch (final IOException ex) {
                Converter.LOGGER.fatal("Failed to process: " + ex.getMessage(),
                        ex);
            }
        }
        return result;
    }

    /**
     * Renders a document into an image.
     * 
     * @param node
     *            Document / Node to render
     * @param context
     *            LayoutContext to use.
     * @return the rendered image
     * @throws IOException
     *             if an I/O error occurred.
     */
    public BufferedImage render(final Node node, final LayoutContext context)
            throws IOException {
        final Image tempimage = new BufferedImage(1, 1,
                BufferedImage.TYPE_INT_ARGB);
        final Graphics2D tempg = (Graphics2D) tempimage.getGraphics();
        // meine �nderung
        final JEuclidView view = new JEuclidView((org.w3c.dom.Document) node, node, context, tempg);

        final int width = (int) Math.ceil(view.getWidth());
        final int ascent = (int) Math.ceil(view.getAscentHeight());
        final int height = (int) Math.ceil(view.getDescentHeight()) + ascent;

        final BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g = image.createGraphics();

        final Color transparency = new Color(255, 255, 255, 0);

        g.setColor(transparency);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.black);

        view.draw(g, 0, ascent);
        return image;
    }
}