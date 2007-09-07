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

import net.sourceforge.jeuclid.DOMBuilder;
import net.sourceforge.jeuclid.LayoutContext;
import net.sourceforge.jeuclid.MathMLParserSupport;
import net.sourceforge.jeuclid.MutableLayoutContext;
import net.sourceforge.jeuclid.context.LayoutContextImpl;
import net.sourceforge.jeuclid.elements.generic.DocumentElement;
import net.sourceforge.jeuclid.layout.JEuclidView;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * Generic converter which uses the registry to do its conversions.
 * 
 * @author Max Berger
 * @version $Revision$
 */
public final class Converter {

    private static Converter converter;

    /**
     * Logger for this class
     */
    private static final Log LOGGER = LogFactory.getLog(Converter.class);

    /**
     * Mime type for SVG.
     */
    public static final String TYPE_SVG = "image/svg+xml";

    /**
     * File extension for SVG.
     */
    public static final String EXTENSION_SVG = "svg";

    private Converter() {

    }

    /**
     * Retrieve an instance of the converter singleton class.
     * 
     * @return a Converter object.
     */
    public static Converter getConverter() {
        if (Converter.converter == null) {
            Converter.converter = new Converter();
        }
        return Converter.converter;
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
        final MutableLayoutContext params = LayoutContextImpl
                .getDefaultLayoutContext();
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
     * Converts an existing document from MathML to the given type and store
     * it in a file.
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
        if (result != null) {
            // should be closed by wrapper image streams, but just in case...
            try {
                outStream.close();
            } catch (final IOException e) {
                Converter.LOGGER.debug(e);
            }
        } else {
            outFile.delete();
        }
        return result;
    }

    /**
     * Converts an existing document from MathML to the given XML based type
     * and store it in a DOM document.
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
     *         format (e.g. SVGDocument). If conversion is not supported to
     *         this type, it may return null.
     * @todo This code contains duplications. Clean up!
     */
    public Document convert(final Node doc, final String outFileType,
            final MutableLayoutContext params) {
        final ConverterPlugin plugin = ConverterRegistry.getRegisty()
                .getConverter(outFileType);
        Document result = null;
        if (plugin != null) {
            final DocumentElement jDoc = DOMBuilder.getDOMBuilder()
                    .createJeuclidDom(doc);
            result = plugin.convert(jDoc, params);
        }
        if (result == null) {
            Converter.LOGGER.fatal("Unsupported output type: " + outFileType);
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
        final ConverterPlugin plugin = ConverterRegistry.getRegisty()
                .getConverter(outFileType);
        Dimension result = null;
        if (plugin != null) {
            try {
                final DocumentElement jDoc = DOMBuilder.getDOMBuilder()
                        .createJeuclidDom(doc);
                result = plugin.convert(jDoc, params, outStream);
            } catch (final IOException ex) {
                Converter.LOGGER.fatal("Failed to process: "
                        + ex.getMessage(), ex);
            }
        } else {
            Converter.LOGGER.fatal("Unsupported output type: " + outFileType);
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

        final DocumentElement jDoc = DOMBuilder.getDOMBuilder()
                .createJeuclidDom(node);
        final JEuclidView view = new JEuclidView(jDoc, context, tempg);

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
