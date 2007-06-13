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

import java.awt.Image;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.MathMLParserSupport;
import net.sourceforge.jeuclid.ParameterKey;
import net.sourceforge.jeuclid.converter.Converter;

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
    public Image createImage(final URL url, final double width,
            final int widthType, final double height, final int heightType,
            final boolean preserveAspectRatio, final boolean smooth,
            final double screenResolution) throws Exception {

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
        final Image mml = Converter.getConverter().render(base);
        final Image scaledImage = ImageToolkitUtil.scaleImage(mml, width,
                widthType, height, heightType, preserveAspectRatio, smooth);
        return scaledImage;
    }

    /**
     * Retrieve the singleton instance of this renderer.
     * 
     * @return the ImageRenderer.
     */
    public static ImageRenderer getRenderer() {
        if (JEuclidImageRenderer.renderer == null) {
            JEuclidImageRenderer.renderer = new JEuclidImageRenderer();
        }
        return JEuclidImageRenderer.renderer;
    }

}
