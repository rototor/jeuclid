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
import java.util.Map;

import net.sourceforge.jeuclid.Constants;
import net.sourceforge.jeuclid.elements.AbstractJEuclidElement;

import org.apache.xmlgraphics.image.loader.Image;
import org.apache.xmlgraphics.image.loader.ImageException;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.ImageSessionContext;
import org.apache.xmlgraphics.image.loader.impl.AbstractImageLoader;
import org.apache.xmlgraphics.image.loader.impl.ImageXMLDOM;

/**
 * @version $Revision$
 */
public class ImageLoaderMathML extends AbstractImageLoader {

    private final ImageFlavor targetFlavor;

    /**
     * Main constructor.
     * 
     * @param target
     *            the target flavor
     */
    public ImageLoaderMathML(final ImageFlavor target) {
        if (!(ImageFlavor.XML_DOM.equals(target))) {
            throw new IllegalArgumentException(
                    "Unsupported target ImageFlavor: " + target);
        }
        this.targetFlavor = target;
    }

    /** {@inheritDoc} */
    public ImageFlavor getTargetFlavor() {
        return this.targetFlavor;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public Image loadImage(final ImageInfo info, final Map hints,
            final ImageSessionContext session) throws ImageException,
            IOException {
        if (!Constants.MATHML_MIMETYPE.equals(info.getMimeType())) {
            throw new IllegalArgumentException(
                    "ImageInfo must be from an MathML image");
        }
        final Image img = info.getOriginalImage();
        if (!(img instanceof ImageXMLDOM)) {
            throw new IllegalArgumentException(
                    "ImageInfo was expected to contain the MathML document as DOM");
        }
        final ImageXMLDOM mmlImage = (ImageXMLDOM) img;
        if (!AbstractJEuclidElement.URI.equals(mmlImage.getRootNamespace())) {
            throw new IllegalArgumentException(
                    "The Image is not in the MathML namespace: "
                            + mmlImage.getRootNamespace());
        }
        return mmlImage;
    }
}
