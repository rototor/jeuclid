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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;

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
public class BasicConverter implements ConverterAPI {

    /**
     * Logger for this class
     */
    private static final Log LOGGER = LogFactory.getLog(BasicConverter.class);

    /**
     * Default constructor.
     */
    public BasicConverter() {
        // Empty on purpose
    }

    /** {@inheritDoc} */
    public boolean convert(final File inFile, final File outFile,
            final String outFileType) throws IOException {
        final Map<ParameterKey, String> params = MathBase
                .getDefaultParameters();
        params.put(ParameterKey.OutFileType, outFileType);
        return this.convert(inFile, outFile, params);
    }

    /** {@inheritDoc} */
    public boolean convert(final File inFile, final File outFile,
            final Map<ParameterKey, String> params) throws IOException {
        Document doc;
        try {
            doc = MathMLParserSupport.parseFile(inFile);
            return this.convert(doc, outFile, params);
        } catch (final SAXException e) {
            BasicConverter.LOGGER.error("Failed to parse file:" + inFile, e);
            return false;
        }
    }

    /** {@inheritDoc} */
    public boolean convert(final Document doc, final File outFile,
            final Map<ParameterKey, String> params) throws IOException {
        boolean result = true;
        try {
            BasicConverter.LOGGER.info("Converting " + doc + " to " + outFile
                    + " ...");

            final Iterator<ImageWriter> it = ImageIO
                    .getImageWritersByMIMEType(params
                            .get(ParameterKey.OutFileType));
            if (it.hasNext()) {
                final ImageWriter writer = (ImageWriter) it.next();

                final ImageOutputStream ios = new FileImageOutputStream(
                        outFile);
                writer.setOutput(ios);
                writer.write(this.render(doc, params));
                ios.close();
            } else {
                BasicConverter.LOGGER.fatal("Unsupported output type: "
                        + params.get(ParameterKey.OutFileType));
                result = false;
            }

        } catch (final SAXException ex) {
            BasicConverter.LOGGER.fatal("Failed to process: "
                    + ex.getMessage(), ex);
            if (outFile != null) {
                outFile.delete();
            }
            result = false;
        }
        return result;
    }

    /** {@inheritDoc} */
    public BufferedImage render(final Document doc,
            final Map<ParameterKey, String> params) throws SAXException,
            IOException {
        final MathBase base = MathMLParserSupport.createMathBaseFromDocument(
                doc, params);

        final Image tempimage = new BufferedImage(1, 1,
                BufferedImage.TYPE_INT_ARGB);
        final Graphics2D tempg = (Graphics2D) tempimage.getGraphics();
        final int width = (int) Math.ceil(base.getWidth(tempg));
        final int height = (int) Math.ceil(base.getHeight(tempg));

        final BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g = image.createGraphics();

        final Color transparency = new Color(255, 255, 255, 0);

        g.setColor(transparency);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.black);

        base.paint(g);
        return image;
    }

    /** {@inheritDoc} */
    public List<String> getAvailableOutfileTypes() {
        final List<String> fileTypes = new Vector<String>(Arrays
                .asList(ImageIO.getWriterMIMETypes()));
        return fileTypes;
    }

    /** {@inheritDoc} */
    public String getSuffixForMimeType(final String mimeType) {
        return BasicConverter.getSuffixForMimeTypeFromImageIO(mimeType);
    }

    private static String getSuffixForMimeTypeFromImageIO(
            final String mimeType) {
        final Set<String> sufs = new HashSet<String>();
        final Iterator<ImageWriter> iwit = ImageIO
                .getImageWritersByMIMEType(mimeType);

        if (iwit != null) {
            while (iwit.hasNext()) {
                final ImageWriter iw = iwit.next();
                final String[] suffixes = iw.getOriginatingProvider()
                        .getFileSuffixes();
                if (suffixes != null) {
                    Collections.addAll(sufs, suffixes);
                }
            }

        }
        if (sufs.isEmpty()) {
            return "";
        } else {
            return sufs.iterator().next();
        }
    }

    /** {@inheritDoc} */
    public String getMimeTypeForSuffix(final String suffix) {
        return BasicConverter.getMimeTypeForSuffixFromImageIO(suffix);
    }

    private static String getMimeTypeForSuffixFromImageIO(final String suffix) {
        final Set<String> mimes = new HashSet<String>();
        final Iterator<ImageWriter> iwit = ImageIO
                .getImageWritersBySuffix(suffix);
        if (iwit != null) {
            while (iwit.hasNext()) {
                final ImageWriter iw = iwit.next();
                final String[] mimeTypes = iw.getOriginatingProvider()
                        .getMIMETypes();
                if (mimeTypes != null) {
                    Collections.addAll(mimes, mimeTypes);
                }
            }
        }
        if (mimes.isEmpty()) {
            return "";
        } else {
            return mimes.iterator().next();
        }
    }

}
