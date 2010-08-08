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

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import net.sourceforge.jeuclid.MutableLayoutContext;
import net.sourceforge.jeuclid.context.LayoutContextImpl;
import net.sourceforge.jeuclid.layout.JEuclidView;

import org.apache.xmlgraphics.java2d.Graphics2DImagePainter;
import org.w3c.dom.Document;

/**
 * Actually draw an JEuclidView.
 * 
 * @version $Revision$
 */
public class Graphics2DImagePainterMathML implements Graphics2DImagePainter {

    private final Dimension dimension;

    private final JEuclidView view;

    private final float ascent;

    /**
     * Default Constructor.
     * 
     * @param theView
     *            {@link JEuclidView} to paint.
     * @param dim
     *            Dimension of the view.
     * @param asc
     *            Ascent of the view.
     */
    public Graphics2DImagePainterMathML(final JEuclidView theView,
            final Dimension dim, final float asc) {
        this.view = theView;
        this.dimension = dim;
        this.ascent = asc;
    }

    /**
     * Create a new {@link Graphics2DImagePainter} for the given Document.
     * 
     * @param document
     *            A MathML DOM Document.
     * @return a {@link Graphics2DImagePainter}.
     */
    public static Graphics2DImagePainter createGraphics2DImagePainter(
            final Document document) {
        final MutableLayoutContext layoutContext = new LayoutContextImpl(
                LayoutContextImpl.getDefaultLayoutContext());

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
        return new Graphics2DImagePainterMathML(view, new Dimension(w, h),
                ascent);
    }

    /** {@inheritDoc} */
    public Dimension getImageSize() {
        return this.dimension;
    }

    /** {@inheritDoc} */
    public void paint(final Graphics2D graphics2d,
            final Rectangle2D rectangle2d) {
        this.view.draw(graphics2d, 0, this.ascent);
    }
}
