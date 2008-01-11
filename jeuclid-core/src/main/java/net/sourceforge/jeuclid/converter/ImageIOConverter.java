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
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;

import net.sourceforge.jeuclid.MathBase;

/**
 * supports conversion to standard Raster formats through ImageIO.
 * 
 * @author Max Berger
 * @version $Revision$
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
    public Dimension convert(final MathBase base, final OutputStream outStream)
            throws IOException {
        final ImageOutputStream ios = new MemoryCacheImageOutputStream(
                outStream);
        this.writer.setOutput(ios);
        final BufferedImage image = Converter.getConverter().render(base);
        if (this.removeAlpha && image.getColorModel().hasAlpha()) {
            this.writer.write(this.removeAlpha(image));
        } else {
            this.writer.write(image);
        }
        ios.close();
        final Graphics2D temp = (Graphics2D) image.getGraphics();
        return new Dimension((int) Math.ceil(base.getWidth(temp)), (int) Math
                .ceil(base.getHeight(temp)));
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
        // g.drawRenderedImage(image, new AffineTransform());
        return noAlpha;
    }

}
