/*
 * Copyright 2007 - 2008 JEuclid, http://jeuclid.sf.net
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

import net.sourceforge.jeuclid.MutableLayoutContext;
import net.sourceforge.jeuclid.context.LayoutContextImpl;
import net.sourceforge.jeuclid.elements.AbstractJEuclidElement;
import net.sourceforge.jeuclid.layout.JEuclidView;
import net.sourceforge.jeuclid.xmlgraphics.PreloaderMathML;

import org.apache.fop.render.Graphics2DAdapter;
import org.apache.fop.render.Renderer;
import org.apache.fop.render.RendererContext;
import org.apache.fop.render.XMLHandler;
import org.apache.xmlgraphics.java2d.Graphics2DImagePainter;
import org.w3c.dom.Document;

/**
 * XMLHandler which draws MathML through a fop G2DAdapter.
 * 
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

        final MutableLayoutContext layoutContext = new LayoutContextImpl(
                LayoutContextImpl.getDefaultLayoutContext());

        if (g2dAdapter != null) {
            final Image tempimage = new BufferedImage(1, 1,
                    BufferedImage.TYPE_INT_ARGB);
            final Graphics2D tempg = (Graphics2D) tempimage.getGraphics();
            final JEuclidView view = new JEuclidView(document, layoutContext,
                    tempg);
            final int w = (int) Math.ceil(view.getWidth()
                    * PreloaderMathML.MPT_FACTOR);
            final float ascent = view.getAscentHeight();
            final int h = (int) Math.ceil((ascent + view.getDescentHeight())
                    * PreloaderMathML.MPT_FACTOR);
            final Graphics2DImagePainter painter = new Graphics2DImagePainter() {

                public void paint(final Graphics2D g2d, final Rectangle2D area) {
                    view.draw(g2d, 0, ascent);
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
