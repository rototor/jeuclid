/*
 * Copyright 2002 - 2007 JEuclid, http://jeuclid.sf.net
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

package net.sourceforge.jeuclid.element;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.element.generic.AbstractMathElement;
import net.sourceforge.jeuclid.element.generic.MathElement;

/**
 * This class presents a mathematical root.
 * 
 * @author Unknown
 * @author Max Berger
 */
public class MathRoot extends AbstractMathElement {

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "mroot";

    /**
     * Char for left part of root rendering.
     */
    public static final char ROOT_CHAR = '\u221A';

    /**
     * Creates a math element.
     * 
     * @param base
     *            The base for the math element tree.
     */
    public MathRoot(final MathBase base) {
        super(base);
    }

    /**
     * Paints this element.
     * 
     * @param g
     *            The graphics context to use for painting
     * @param posX
     *            The first left position for painting
     * @param posY
     *            The position of the baseline
     */
    @Override
    public void paint(final Graphics2D g, final int posX, final int posY) {
        if (this.getMathElementCount() < 2) {
            return;
        }

        super.paint(g, posX, posY);
        final MathElement e1 = this.getMathElement(0);
        final MathElement e2 = this.getMathElement(1);

        final int width1 = e1.getWidth(g);
        final int height1 = e1.getHeight(g);

        final Font font = g.getFont().deriveFont(
                this.getFontsizeInPoint() * 100);
        final GlyphVector gv = font.createGlyphVector(g
                .getFontRenderContext(), new char[] { MathRoot.ROOT_CHAR });
        final Rectangle2D gbounds = gv.getGlyphMetrics(0).getBounds2D();
        final double glyphWidth = gbounds.getWidth() / 100;
        final double glyphHeight = gbounds.getHeight() / 100;
        final double ascent = gbounds.getY() / 100;

        double yScale, xScale;

        final int width2 = (int) Math
                .round(e2.getWidth(g) - glyphWidth * 0.5);

        yScale = (e1.getHeight(g) + 4) / glyphHeight;
        xScale = 1;

        final AffineTransform transform = g.getTransform();
        final AffineTransform prevTransform = g.getTransform();
        transform.scale(xScale, yScale);

        double y = posY + this.getDescentHeight(g);
        y = y - (ascent + glyphHeight) * yScale;
        double x = posX + width2;
        y = (y / yScale);
        x = (x / xScale);

        g.setTransform(transform);
        g
                .drawString(String.valueOf(MathRoot.ROOT_CHAR), (float) x,
                        (float) y);
        g.setTransform(prevTransform);
        final Shape oldClip = g.getClip();

        g.clipRect(posX + width2,
                posY + e1.getDescentHeight(g) - height1 - 2,
                (int) ((glyphWidth + width2) * xScale) + 1 + width1 - width2,
                height1 + 4);

        final int rightTopRootPoint = posY + e1.getDescentHeight(g) - height1
                - 2;

        g
                .drawLine((int) (posX + (glyphWidth + width2) * xScale) + 1,
                        rightTopRootPoint, posX + this.getWidth(g),
                        rightTopRootPoint);
        g.setClip(oldClip);

        e1.paint(g, posX + this.getRootWidth(g) + 1, posY);
        e2.paint(g, posX, posY + e1.getDescentHeight(g)
                - e2.getDescentHeight(g) - e1.getHeight(g) / 2);
    }

    private int getRootWidth(final Graphics2D g) {
        int result = 0;

        final FontRenderContext context = new FontRenderContext(
                new AffineTransform(), false, false);
        final GlyphVector gv = this.getFont().createGlyphVector(context,
                new char[] { MathRoot.ROOT_CHAR });
        result = (int) (gv.getGlyphMetrics(0).getBounds2D().getWidth());
        result += Math.max(this.getMathElement(1).getWidth(g) - 0.5 * result,
                0);
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public int calculateWidth(final Graphics2D g) {
        if (this.getMathElementCount() < 2) {
            return 0;
        }
        return this.getMathElement(0).getWidth(g) + this.getRootWidth(g) + 1;
    }

    /** {@inheritDoc} */
    @Override
    public int calculateAscentHeight(final Graphics2D g) {
        if (this.getMathElementCount() < 2) {
            return 0;
        }

        final MathElement elem = this.getMathElement(0);
        return Math.max(elem.getAscentHeight(g) + 2, elem.getHeight(g) / 2
                + 2 - elem.getDescentHeight(g) + elem.getHeight(g));
    }

    /** {@inheritDoc} */
    @Override
    public int calculateDescentHeight(final Graphics2D g) {
        if (this.getMathElementCount() < 2) {
            return 0;
        }

        return this.getMathElement(0).getDescentHeight(g) + 2;
    }

    /** {@inheritDoc} */
    public String getTagName() {
        return MathRoot.ELEMENT;
    }
}
