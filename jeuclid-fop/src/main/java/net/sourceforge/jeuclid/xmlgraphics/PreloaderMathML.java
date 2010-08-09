/*
 * Copyright 2002 - 2008 JEuclid, http://jeuclid.sf.net
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

package net.sourceforge.jeuclid.xmlgraphics;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;

import net.sourceforge.jeuclid.Constants;
import net.sourceforge.jeuclid.context.LayoutContextImpl;
import net.sourceforge.jeuclid.elements.AbstractJEuclidElement;
import net.sourceforge.jeuclid.elements.generic.MathImpl;
import net.sourceforge.jeuclid.layout.JEuclidView;
import net.sourceforge.jeuclid.parser.Parser;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.util.UnclosableInputStream;
import org.apache.xmlgraphics.image.loader.ImageContext;
import org.apache.xmlgraphics.image.loader.ImageException;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.ImageSize;
import org.apache.xmlgraphics.image.loader.impl.AbstractImagePreloader;
import org.apache.xmlgraphics.image.loader.impl.ImageXMLDOM;
import org.apache.xmlgraphics.image.loader.util.ImageUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * @version $Revision$
 */
public class PreloaderMathML extends AbstractImagePreloader {
    /**
     * Convert from point to millipoint.
     */
    public static final float MPT_FACTOR = 1000.0f;

    /**
     * Logger for this class
     */
    private static final Log LOGGER = LogFactory
            .getLog(PreloaderMathML.class);

    /**
     * Default Constructor.
     */
    public PreloaderMathML() {
        // Empty on purpose
    }

    /** {@inheritDoc} */
    public ImageInfo preloadImage(final String uri, final Source src,
            final ImageContext context) throws ImageException, IOException {
        final Document n = this.parseSource(src);
        if (n != null) {
            return this.createImageInfo(uri, context, n);
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    private ImageInfo createImageInfo(final String uri,
            final ImageContext context, final Document n) {
        final ImageInfo info = new ImageInfo(uri, Constants.MATHML_MIMETYPE);
        final ImageSize size = new ImageSize();
        final JEuclidView view = new JEuclidView(n, LayoutContextImpl
                .getDefaultLayoutContext(), null);
        final int descentMpt = (int) (view.getDescentHeight() * PreloaderMathML.MPT_FACTOR);
        final int ascentMpt = (int) (view.getAscentHeight() * PreloaderMathML.MPT_FACTOR);

        size.setSizeInMillipoints(
                (int) (view.getWidth() * PreloaderMathML.MPT_FACTOR),
                ascentMpt + descentMpt);
        size.setBaselinePositionFromBottom(descentMpt);
        // Set the resolution to that of the FOUserAgent
        size.setResolution(context.getSourceResolution());
        size.calcPixelsFromSize();
        info.setSize(size);

        // The whole image had to be loaded for this, so keep it
        final ImageXMLDOM xmlImage = new ImageXMLDOM(info, n,
                AbstractJEuclidElement.URI);
        info.getCustomObjects().put(ImageInfo.ORIGINAL_IMAGE, xmlImage);
        return info;
    }

    private Document parseSource(final Source src) {
        Document n = null;
        InputStream in = null;
        try {
            if (src instanceof DOMSource) {
                final DOMSource domSrc = (DOMSource) src;
                n = (Document) domSrc.getNode();
            } else {
                in = new UnclosableInputStream(ImageUtil.needInputStream(src));
                final int length = in.available();
                in.mark(length + 1);
                n = Parser.getInstance().parseStreamSource(
                        new StreamSource(in));
            }
            final Element rootNode = n.getDocumentElement();
            if (!(AbstractJEuclidElement.URI.equals(rootNode
                    .getNamespaceURI()) || MathImpl.ELEMENT.equals(rootNode
                    .getNodeName()))) {
                n = null;
            }
        } catch (final IOException e) {
            n = null;
        } catch (final SAXException e) {
            n = null;
        } catch (final IllegalArgumentException e) {
            n = null;
        } catch (final NullPointerException e) {
            // Due to a bug in xmlgraphics-commons 1.3.1 which sometimes
            // creates wrapper around null streams if files do not exist.
            n = null;
        }
        try {
            if (in != null) {
                in.reset();
            }
        } catch (final IOException ioe) {
            PreloaderMathML.LOGGER.warn("Should never happen: "
                    + ioe.getMessage());
        } catch (final NullPointerException e) {
            // Due to a bug in xmlgraphics-commons 1.3.1 which sometimes
            // creates wrapper around null streams if files do not exist.
            n = null;
        }
        return n;
    }
}
