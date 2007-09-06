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

package net.sourceforge.jeuclid.xxe;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.MathMLParserSupport;
import net.sourceforge.jeuclid.ParameterKey;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.xmlmind.xmledit.imagetoolkit.ImageRenderer;
import com.xmlmind.xmledit.imagetoolkit.ImageRendererAdapter;
import com.xmlmind.xmledit.imagetoolkit.ImageToolkitUtil;

/**
 * Implements an ImageRenderer for XXE.
 * 
 * @author Max Berger
 * @version $Revision$
 */
public final class JEuclidImageRenderer extends ImageRendererAdapter {

    private static JEuclidImageRenderer renderer;

    private JEuclidImageRenderer() {
        // Empty on purpose
    }

    /** {@inheritDoc} */
    @Override
    // CHECKSTYLE:OFF
    // Has more than 7 parameters, unfortunately this is due to the upstream
    // interface.
    public Image createImage(final URL url, final double width,
            final int widthType, final double height, final int heightType,
            final boolean preserveAspectRatio, final boolean smooth,
            final double screenResolution) throws Exception {
        // CHECKSTYLE:ON
        final MathBase base = this.loadDocument(url);

        // TODO: This needs to be moved into the converter API

        final Image tempimage = new BufferedImage(1, 1,
                BufferedImage.TYPE_INT_ARGB);
        final Graphics2D tempg = (Graphics2D) tempimage.getGraphics();
        final float origWidth = base.getWidth(tempg);
        final float origHeight = base.getHeight(tempg);

        final double[] targetwh = ImageToolkitUtil.computeScaledSize(
                origWidth, origHeight, width, widthType, height, heightType,
                preserveAspectRatio);
        final double targetWidth;
        final double targetHeight;
        if (targetwh != null) {
            targetWidth = Math.max(1.0, targetwh[0]);
            targetHeight = Math.max(1.0, targetwh[1]);
        } else {
            targetWidth = origWidth;
            targetHeight = origHeight;
        }
        final int imageWidth = (int) Math.ceil(targetWidth);
        final int imageHeight = (int) Math.ceil(targetHeight);

        final BufferedImage image = new BufferedImage(imageWidth,
                imageHeight, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g = image.createGraphics();

        final Color transparency = new Color(255, 255, 255, 0);

        g.setColor(transparency);
        g.fillRect(0, 0, imageWidth, imageHeight);
        g.setColor(Color.black);
        if (targetwh != null) {
            g.scale(targetWidth / origWidth, targetHeight / origHeight);
        }
        base.paint(g);
        return image;

    }

    private MathBase loadDocument(final URL url) throws IOException,
            SAXException {
        Document doc = null;
        try {
            doc = MathMLParserSupport.parseInputStreamXML((InputStream) url
                    .getContent());
        } catch (final SAXException se) {
            try {
                doc = MathMLParserSupport
                        .parseInputStreamODF((InputStream) url.getContent());
            } catch (final SAXException s2) {
                throw se;
            }
        }
        final Map<ParameterKey, String> renderingParams = MathBase
                .getDefaultParameters();
        renderingParams.put(ParameterKey.AntiAlias, "true");
        final MathBase base = MathMLParserSupport.createMathBaseFromDocument(
                doc, renderingParams);
        return base;
    }

    /**
     * Retrieve the singleton instance of this renderer.
     * 
     * @return the ImageRenderer.
     */
    public static synchronized ImageRenderer getRenderer() {
        if (JEuclidImageRenderer.renderer == null) {
            JEuclidImageRenderer.renderer = new JEuclidImageRenderer();
        }
        return JEuclidImageRenderer.renderer;
    }

}
