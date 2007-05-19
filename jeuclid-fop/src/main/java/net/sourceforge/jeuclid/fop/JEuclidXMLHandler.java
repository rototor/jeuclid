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

/* 
 * Parts of the contents are heavily inspired by work done for Barcode4J by
 * Jeremias Maerki, available at http://barcode4j.sf.net/
 */
package net.sourceforge.jeuclid.fop;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.MathMLParserSupport;
import net.sourceforge.jeuclid.elements.AbstractJEuclidElement;
import org.apache.fop.render.Graphics2DAdapter;
import org.apache.fop.render.Graphics2DImagePainter;
import org.apache.fop.render.Renderer;
import org.apache.fop.render.RendererContext;
import org.apache.fop.render.XMLHandler;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * XMLHandler which draws MathML through a fop G2DAdapter.
 * 
 * @author Max Berger
 * @version $Revision$
 */
public class JEuclidXMLHandler implements XMLHandler {

    /** Creates a new instance of JEuclidXMLHandler. */
    public JEuclidXMLHandler() {
    }

    /** {@inheritDoc} */
    public void handleXML(final RendererContext rendererContext,
            final Document document, final String ns) throws Exception {
        final Graphics2DAdapter g2dAdapter = rendererContext.getRenderer()
                .getGraphics2DAdapter();
        if (g2dAdapter != null) {
            try {
                final MathBase base = MathMLParserSupport
                        .createMathBaseFromDocument(document, MathBase
                                .getDefaultParameters());
                final Image tempimage = new BufferedImage(1, 1,
                        BufferedImage.TYPE_INT_ARGB);
                final Graphics2D tempg = (Graphics2D) tempimage.getGraphics();
                final int w = (int) Math.ceil(base.getWidth(tempg) * 1000);
                final int h = (int) Math.ceil(base.getHeight(tempg) * 1000);
                final Graphics2DImagePainter painter = new Graphics2DImagePainter() {

                    public void paint(final Graphics2D g2d,
                            final Rectangle2D area) {
                        base.paint(g2d);
                    }

                    public Dimension getImageSize() {
                        return new Dimension(w, h);
                    }

                };
                g2dAdapter.paintImage(painter, rendererContext,
                        ((Integer) rendererContext.getProperty("xpos"))
                                .intValue(), ((Integer) rendererContext
                                .getProperty("ypos")).intValue(),
                        ((Integer) rendererContext.getProperty("width"))
                                .intValue(), ((Integer) rendererContext
                                .getProperty("height")).intValue());
            } catch (SAXException x) {

            } catch (IOException x) {

            }
        }
    }

    /** {@inheritDoc} */
    public boolean supportsRenderer(final Renderer renderer) {
        return renderer.getGraphics2DAdapter() != null;
    }

    /** {@inheritDoc} */
    public String getNamespace() {
        return AbstractJEuclidElement.URI;
    }

}
