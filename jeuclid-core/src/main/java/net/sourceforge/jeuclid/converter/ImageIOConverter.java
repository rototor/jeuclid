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

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;

import net.sourceforge.jeuclid.LayoutContext;
import net.sourceforge.jeuclid.elements.generic.DocumentElement;

import org.w3c.dom.Document;

/**
 * supports conversion to standard Raster formats through ImageIO.
 * 
 * @author Max Berger
 * @version $Revision$
 */
public class ImageIOConverter implements ConverterPlugin {

    private final ImageWriter writer;

    /**
     * Default constructor.
     * 
     * @param iw
     *            ImageWrite instance to use for this converter.
     */
    ImageIOConverter(final ImageWriter iw) {
        this.writer = iw;
    }

    /** {@inheritDoc} */
    public Dimension convert(final DocumentElement doc,
            final LayoutContext context, final OutputStream outStream)
            throws IOException {
        final ImageOutputStream ios = new MemoryCacheImageOutputStream(
                outStream);
        this.writer.setOutput(ios);
        final BufferedImage image = Converter.getConverter().render(doc,
                context);
        this.writer.write(image);
        ios.close();
        return new Dimension(image.getWidth(), image.getHeight());
    }

    /** {@inheritDoc} */
    public Document convert(final DocumentElement doc,
            final LayoutContext context) {
        return null;
    }

}
