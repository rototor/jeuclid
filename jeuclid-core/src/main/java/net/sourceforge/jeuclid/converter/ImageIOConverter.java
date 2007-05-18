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
    public void convert(final MathBase base, final OutputStream outStream)
            throws IOException {
        final ImageOutputStream ios = new MemoryCacheImageOutputStream(outStream);
        this.writer.setOutput(ios);
        this.writer.write(Converter.getConverter().render(base));
        ios.close();
    }

}
