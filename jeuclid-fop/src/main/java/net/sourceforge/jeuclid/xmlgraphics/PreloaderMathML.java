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

package net.sourceforge.jeuclid.xmlgraphics;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import net.sourceforge.jeuclid.Constants;
import net.sourceforge.jeuclid.context.LayoutContextImpl;
import net.sourceforge.jeuclid.elements.AbstractJEuclidElement;
import net.sourceforge.jeuclid.layout.JEuclidView;
import net.sourceforge.jeuclid.parser.Parser;

import org.apache.fop.util.UnclosableInputStream;
import org.apache.xmlgraphics.image.loader.ImageContext;
import org.apache.xmlgraphics.image.loader.ImageException;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.ImageSize;
import org.apache.xmlgraphics.image.loader.impl.AbstractImagePreloader;
import org.apache.xmlgraphics.image.loader.impl.ImageXMLDOM;
import org.apache.xmlgraphics.image.loader.util.ImageUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * @version $Revision$
 */
public class PreloaderMathML extends AbstractImagePreloader {
    /**
     * Default Constructor.
     */
    public PreloaderMathML() {
        // Empty on purpose
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public ImageInfo preloadImage(final String uri, final Source src,
            final ImageContext context) throws ImageException, IOException {
        Node n;
        InputStream in = null;
        try {
            in = new UnclosableInputStream(ImageUtil.needInputStream(src));
            final int length = in.available();
            in.mark(length + 1);
            n = Parser.getParser().parse(new StreamSource(in));
        } catch (final SAXException e) {
            n = null;
        } catch (final ParserConfigurationException e) {
            n = null;
        } catch (final IllegalArgumentException e) {
            n = null;
        }

        try {
            in.reset();
        } catch (final IOException ioe) {
            // Should not happen
        }
        if (n != null) {
            final ImageInfo info = new ImageInfo(uri,
                    Constants.MATHML_MIMETYPE);
            final ImageSize size = new ImageSize();

            final Image tempimage = new BufferedImage(1, 1,
                    BufferedImage.TYPE_INT_ARGB);
            final Graphics2D tempg = (Graphics2D) tempimage.getGraphics();
            final JEuclidView view = new JEuclidView(n, LayoutContextImpl
                    .getDefaultLayoutContext(), tempg);
            final int descentMpt = (int) (view.getDescentHeight() * 1000);
            final int ascentMpt = (int) (view.getAscentHeight() * 1000);

            size.setSizeInMillipoints((int) (view.getWidth() * 1000),
                    ascentMpt + descentMpt);
            size.setBaselinePositionFromBottom(descentMpt);
            // Set the resolution to that of the FOUserAgent
            size.setResolution(context.getSourceResolution());
            size.calcPixelsFromSize();
            info.setSize(size);

            // The whole image had to be loaded for this, so keep it
            final ImageXMLDOM xmlImage = new ImageXMLDOM(info, (Document) n,
                    AbstractJEuclidElement.URI);
            info.getCustomObjects().put(ImageInfo.ORIGINAL_IMAGE, xmlImage);
            return info;

        }

        return null;
    }
}
