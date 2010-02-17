/*
 * Copyright 2002 - 2009 JEuclid, http://jeuclid.sf.net
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

import java.util.Map;

import org.apache.xmlgraphics.image.loader.Image;
import org.apache.xmlgraphics.image.loader.ImageException;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.impl.AbstractImageConverter;
import org.apache.xmlgraphics.image.loader.impl.ImageGraphics2D;
import org.apache.xmlgraphics.image.loader.impl.ImageXMLDOM;
import org.apache.xmlgraphics.java2d.Graphics2DImagePainter;

/**
 * Convert a MathML Image given as DOM to a Graphics2d Painter.
 * 
 * @version $Revision$
 */
public class ImageConverterMathML2G2D extends AbstractImageConverter {
    /**
     * Default Constructor.
     */
    public ImageConverterMathML2G2D() {
        // Empty on purpose
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    // Interface is not generic
    public Image convert(final Image src, final Map hints)
            throws ImageException {
        // TODO: Should be checked
        // this.checkSourceFlavor(src);
        final ImageXMLDOM xmlDom = (ImageXMLDOM) src;
        final Graphics2DImagePainter painter = Graphics2DImagePainterMathML
                .createGraphics2DImagePainter(xmlDom.getDocument());

        final ImageGraphics2D g2dImage = new ImageGraphics2D(src.getInfo(),
                painter);
        return g2dImage;
    }

    /** {@inheritDoc} */
    public ImageFlavor getSourceFlavor() {
        return ImageFlavor.XML_DOM;
        // TODO: Use new namespace
        // return new ImageFlavor(
        // "text/xml;DOM;namespace=http://www.w3.org/1998/Math/MathML");
    }

    /** {@inheritDoc} */
    public ImageFlavor getTargetFlavor() {
        return ImageFlavor.GRAPHICS2D;
    }

}
