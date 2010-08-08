/*
 * Copyright 2007 - 2009 JEuclid, http://jeuclid.sf.net
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

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;

import net.sourceforge.jeuclid.LayoutContext;

import org.w3c.dom.Node;

/**
 * supports conversion to standard Raster formats through ImageIO.
 * 
 * @version $Revision$
 */
public class ImageIOConverter implements ConverterPlugin {

    private final ImageWriter writer;

    private final int colorModel;

    /**
     * Default constructor.
     * 
     * @param iw
     *            ImageWrite instance to use for this converter.
     * @param noAlpha
     *            if true, this image format does not support alpha values.
     */
    ImageIOConverter(final ImageWriter iw, final boolean noAlpha) {
        this.writer = iw;
        if (noAlpha) {
            this.colorModel = BufferedImage.TYPE_INT_RGB;
        } else {
            this.colorModel = BufferedImage.TYPE_INT_ARGB;
        }
    }

    /** {@inheritDoc} */
    public Dimension convert(final Node doc, final LayoutContext context,
            final OutputStream outStream) throws IOException {
        final ImageOutputStream ios = new MemoryCacheImageOutputStream(
                outStream);
        final BufferedImage image = Converter.getInstance().render(doc,
                context, this.colorModel);
        synchronized (this.writer) {
            this.writer.setOutput(ios);
            this.writer.write(image);
        }
        ios.close();
        return new Dimension(image.getWidth(), image.getHeight());
    }

    /** {@inheritDoc} */
    public DocumentWithDimension convert(final Node doc,
            final LayoutContext context) {
        return null;
    }

}
