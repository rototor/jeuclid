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

import net.sourceforge.jeuclid.Constants;

import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.impl.AbstractImageLoaderFactory;
import org.apache.xmlgraphics.image.loader.spi.ImageLoader;

/**
 * @version $Revision$
 */
public class ImageLoaderFactoryMathML extends AbstractImageLoaderFactory {

    private static final ImageFlavor[] FLAVORS = new ImageFlavor[] { ImageFlavor.XML_DOM };

    private static final String[] MIMES = new String[] { Constants.MATHML_MIMETYPE };

    /**
     * Default Constructor.
     */
    public ImageLoaderFactoryMathML() {
        // Empty on purpose
    }

    /** {@inheritDoc} */
    public String[] getSupportedMIMETypes() {
        return ImageLoaderFactoryMathML.MIMES.clone();
    }

    /** {@inheritDoc} */
    public ImageFlavor[] getSupportedFlavors(final String mime) {
        return ImageLoaderFactoryMathML.FLAVORS.clone();
    }

    /** {@inheritDoc} */
    public ImageLoader newImageLoader(final ImageFlavor targetFlavor) {
        return new ImageLoaderMathML(targetFlavor);
    }

    /** {@inheritDoc} */
    public int getUsagePenalty(final String mime, final ImageFlavor flavor) {
        return 0;
    }

    /** {@inheritDoc} */
    public boolean isAvailable() {
        return true;
    }

}
