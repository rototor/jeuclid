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

/* $Id: ImageIOConverter.java 783 2008-06-07 14:12:27Z maxberger $ */

package euclid.converter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;

import euclid.LayoutContext;

import org.w3c.dom.Node;

/**
 * supports conversion to standard Raster formats through ImageIO.
 * 
 * @version $Revision: 783 $
 */
public class ImageIOConverter implements ConverterPlugin {

    private final ImageWriter writer;

    private final boolean removeAlpha;

    /**
     * Default constructor.
     * 
     * @param iw
     *            ImageWrite instance to use for this converter.
     */
    ImageIOConverter(final ImageWriter iw, final boolean remAlpha) {
        this.writer = iw;
        this.removeAlpha = remAlpha;
    }

    /** {@inheritDoc} */
    public Dimension convert(final Node doc, final LayoutContext context,
            final OutputStream outStream) throws IOException {
        final ImageOutputStream ios = new MemoryCacheImageOutputStream(
                outStream);
        BufferedImage image = Converter.getInstance().render(doc, context);
        if (this.removeAlpha && image.getColorModel().hasAlpha()) {
            image = this.removeAlpha(image);
        }
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

    private BufferedImage removeAlpha(final BufferedImage image) {
        final int width = image.getWidth();
        final int height = image.getHeight();
        final BufferedImage noAlpha = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        final Graphics2D g = noAlpha.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.drawImage(image, 0, 0, null);
        return noAlpha;
    }

}
