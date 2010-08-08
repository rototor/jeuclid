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

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;

/**
 * Detects and registers the Converters from ImageIO.
 * 
 * @version $Revision$
 */
public final class ImageIODetector implements ConverterDetector {

    /**
     * Default constructor.
     */
    public ImageIODetector() {
        // Empty on purpose
    }

    /**
     * Detects and registers all converters available through ImageIO.
     * 
     * @param registry
     *            ConverterRegistry to use.
     */
    public void detectConversionPlugins(final ConverterRegistry registry) {

        final String[] mimeTypes = ImageIO.getWriterMIMETypes();

        final Set<String> noAlphaMimeTypes = new TreeSet<String>();
        noAlphaMimeTypes.add("image/jpeg");
        noAlphaMimeTypes.add("image/bmp");

        for (final String mimeType : mimeTypes) {
            final Iterator<ImageWriter> iwit = ImageIO
                    .getImageWritersByMIMEType(mimeType);
            if (iwit != null) {
                while (iwit.hasNext()) {
                    final ImageWriter iw = iwit.next();
                    final String[] suffixes = iw.getOriginatingProvider()
                            .getFileSuffixes();
                    if (suffixes != null) {
                        for (final String suffix : suffixes) {
                            registry.registerMimeTypeAndSuffix(mimeType,
                                    suffix, false);
                        }
                    }
                    registry.registerConverter(mimeType,
                            new ImageIOConverter(iw, noAlphaMimeTypes
                                    .contains(mimeType)), false);
                }

            }

        }

    }
}
