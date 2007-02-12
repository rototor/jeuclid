/*
 * Copyright 2002 - 2006 JEuclid, http://jeuclid.sf.net
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

/* $Id: MathSqrt.java,v 1.11.2.3 2006/11/18 00:24:13 maxberger Exp $ */

package net.sourceforge.jeuclid.element;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.element.generic.AbstractMathElement;
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
    public MathSqrt(MathBase base) {
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
    public void paint(Graphics g, int posX, int posY) {
        super.paint(g, posX, posY);
        java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;

        Font font = g.getFont().deriveFont(this.getFontsizeInPoint() * 100);
        GlyphVector gv = font.createGlyphVector(g2d.getFontRenderContext(),
                new char[] { ROOT_CHAR });
        Rectangle2D gbounds = gv.getGlyphMetrics(0).getBounds2D();
        double glyphWidth = gbounds.getWidth() / 100;
        double glyphHeight = gbounds.getHeight() / 100;
        double ascent = gbounds.getY() / 100;
        double yScale, xScale;

        yScale = (getHeight(g) / glyphHeight);
        xScale = 1;

        AffineTransform transform = g2d.getTransform();
        AffineTransform prevTransform = g2d.getTransform();
        transform.scale(xScale, yScale);

        double y = posY + getDescentHeight(g);
        y = y - (ascent + glyphHeight) * yScale;
        double x = posX;
        y = (y / yScale);
        x = (x / xScale);
        final Shape oldClip = g2d.getClip();

        g2d.clipRect(posX - 1, posY + getDescentHeight(g) - getHeight(g),
                (int) (posX + glyphWidth * xScale) + getMathElementsWidth(g)
                        + 2, getHeight(g) + 2);
        g2d.setTransform(transform);
        g2d.drawString(String.valueOf(ROOT_CHAR), (float) x, (float) y);
        g2d.setTransform(prevTransform);

        int rightTopPoint = (int) ((posY + getDescentHeight(g) - getHeight(g)));
        g2d.drawLine(posX + getRootWidth() + 1, rightTopPoint, posX
                + getWidth(g), rightTopPoint);
        int pos = posX + getRootWidth() + 1;
        AbstractMathElement child;
        g2d.setClip(oldClip);

        for (int i = 0; i < getMathElementCount(); i++) {
            child = getMathElement(i);
            child.paint(g, pos, posY);
            pos += child.getWidth(g);
        }
    }

    /**
     * Returns the width of the childs
     * 
     * @return Width of childs
     */
    private int getMathElementsWidth(Graphics g) {
        int width = 0;

        for (int i = 0; i < getMathElementCount(); i++) {
            width += getMathElement(i).getWidth(g);
        }
        if (width == 0) {
            return this.getFontMetrics(g).stringWidth(" ");
        }
        return width;
    }

    /** {@inheritDoc} */
    public int calculateWidth(Graphics g) {
        return getMathElementsWidth(g) + getRootWidth() + 2;
    }

    private int getRootWidth() {
        FontRenderContext context = new FontRenderContext(
                new AffineTransform(), false, false);
        GlyphVector gv = getFont().createGlyphVector(context,
                new char[] { ROOT_CHAR });
        int result = (int) (gv.getGlyphMetrics(0).getBounds2D().getWidth());
        return result;
    }

    /** {@inheritDoc} */
    public int calculateAscentHeight(Graphics g) {
        return super.calculateAscentHeight(g) + 2;
    }

    /** {@inheritDoc} */
    public int calculateDescentHeight(Graphics g) {
        return super.calculateDescentHeight(g) + 2;
    }
}
