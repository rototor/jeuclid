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

/* $Id: MathSqrt.java,v 1.1.2.4 2007/02/10 22:57:22 maxberger Exp $ */

package net.sourceforge.jeuclid.element;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.element.generic.AbstractMathElementWithChildren;

/**
 * This class presents a mathematical square root.
 * 
 * @author Unknown
 * @author Max Berger
 */
public class MathSqrt extends AbstractMathElementWithChildren {

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "msqrt";

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
    public MathSqrt(final MathBase base) {
        super(base);
    }

    /** {@inheritDoc} */
    @Override
    public void paint(final Graphics2D g, final int posX, final int posY) {
        super.paint(g, posX, posY);

        final Font font = g.getFont().deriveFont(this.getFontsizeInPoint() * 100);
        final GlyphVector gv = font.createGlyphVector(g.getFontRenderContext(),
                new char[] { ROOT_CHAR });
        final Rectangle2D gbounds = gv.getGlyphMetrics(0).getBounds2D();
        final double glyphWidth = gbounds.getWidth() / 100;
        final double glyphHeight = gbounds.getHeight() / 100;
        final double ascent = gbounds.getY() / 100;
        double yScale, xScale;

        yScale = (this.getHeight(g) / glyphHeight);
        xScale = 1;

        final AffineTransform transform = g.getTransform();
        final AffineTransform prevTransform = g.getTransform();
        transform.scale(xScale, yScale);

        double y = posY + this.getDescentHeight(g);
        y = y - (ascent + glyphHeight) * yScale;
        double x = posX;
        y = (y / yScale);
        x = (x / xScale);
        final Shape oldClip = g.getClip();

        g.clipRect(posX - 1, posY + this.getDescentHeight(g) - this.getHeight(g),
                (int) (posX + glyphWidth * xScale)
                        + super.calculateChildrenWidth(g) + 2,
                this.getHeight(g) + 2);
        g.setTransform(transform);
        g.drawString(String.valueOf(ROOT_CHAR), (float) x, (float) y);
        g.setTransform(prevTransform);

        final int rightTopPoint = ((posY + this.getDescentHeight(g) - this.getHeight(g)));
        g.drawLine(posX + this.getRootWidth() + 1, rightTopPoint, posX
                + this.getWidth(g), rightTopPoint);
        final int pos = posX + this.getRootWidth() + 1;
        g.setClip(oldClip);

        this.paintChildren(g, pos, posY);
    }

    /** {@inheritDoc} */
    @Override
    public int calculateWidth(final Graphics2D g) {
        return super.calculateChildrenWidth(g) + this.getRootWidth() + 2;
    }

    private int getRootWidth() {
        final FontRenderContext context = new FontRenderContext(
                new AffineTransform(), false, false);
        final GlyphVector gv = this.getFont().createGlyphVector(context,
                new char[] { ROOT_CHAR });
        final int result = (int) (gv.getGlyphMetrics(0).getBounds2D().getWidth());
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public int calculateAscentHeight(final Graphics2D g) {
        return super.calculateChildrenAscentHeight(g) + 2;
    }

    /** {@inheritDoc} */
    @Override
    public int calculateDescentHeight(final Graphics2D g) {
        return super.calculateChildrenDescentHeight(g) + 2;
    }

    /** {@inheritDoc} */
    public String getTagName() {
        return ELEMENT;
    }
}
