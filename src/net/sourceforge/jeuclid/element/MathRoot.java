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

/* $Id$ */

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
    public MathRoot(MathBase base) {
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
        if (getMathElementCount() < 2) {
            return;
        }

        java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;

        super.paint(g, posX, posY);
        AbstractMathElement e1 = getMathElement(0);
        AbstractMathElement e2 = getMathElement(1);

        int width1 = e1.getWidth(g);
        int height1 = e1.getHeight(g);

        Font font = g.getFont().deriveFont(this.getFontsizeInPoint() * 100);
        GlyphVector gv = font.createGlyphVector(g2d.getFontRenderContext(),
                new char[] { ROOT_CHAR });
        Rectangle2D gbounds = gv.getGlyphMetrics(0).getBounds2D();
        double glyphWidth = gbounds.getWidth() / 100;
        double glyphHeight = gbounds.getHeight() / 100;
        double ascent = gbounds.getY() / 100;

        double yScale, xScale;

        int width2 = (int) Math.round(e2.getWidth(g) - glyphWidth * 0.5);

        yScale = (e1.getHeight(g) + 4) / glyphHeight;
        xScale = 1;

        AffineTransform transform = g2d.getTransform();
        AffineTransform prevTransform = g2d.getTransform();
        transform.scale(xScale, yScale);

        double y = posY + getDescentHeight(g);
        y = y - (ascent + glyphHeight) * yScale;
        double x = posX + width2;
        y = (y / yScale);
        x = (x / xScale);

        g2d.setTransform(transform);
        g2d.drawString(String.valueOf(ROOT_CHAR), (float) x, (float) y);
        g2d.setTransform(prevTransform);
        final Shape oldClip = g2d.getClip();

        g2d.clipRect(posX + width2,
                posY + e1.getDescentHeight(g) - height1 - 2,
                (int) ((glyphWidth + width2) * xScale) + 1 + width1 - width2,
                height1 + 4);

        int rightTopRootPoint = posY + e1.getDescentHeight(g) - height1 - 2;

        g2d.drawLine((int) (posX + (glyphWidth + width2) * xScale) + 1,
                rightTopRootPoint, posX + getWidth(g), rightTopRootPoint);
        g2d.setClip(oldClip);

        e1.paint(g, posX + getRootWidth(g) + 1, posY);
        e2.paint(g, posX, posY + e1.getDescentHeight(g)
                - e2.getDescentHeight(g) - e1.getHeight(g) / 2);
    }

    private int getRootWidth(Graphics g) {
        int result = 0;

        FontRenderContext context = new FontRenderContext(
                new AffineTransform(), false, false);
        GlyphVector gv = getFont().createGlyphVector(context,
                new char[] { ROOT_CHAR });
        result = (int) (gv.getGlyphMetrics(0).getBounds2D().getWidth());
        result += Math.max(getMathElement(1).getWidth(g) - 0.5 * result, 0);
        return result;
    }

    /** {@inheritDoc} */
    public int calculateWidth(Graphics g) {
        if (getMathElementCount() < 2) {
            return 0;
        }
        return getMathElement(0).getWidth(g) + getRootWidth(g) + 1;
    }

    /** {@inheritDoc} */
    public int calculateAscentHeight(Graphics g) {
        if (getMathElementCount() < 2) {
            return 0;
        }

        AbstractMathElement elem = getMathElement(0);
        return Math.max(elem.getAscentHeight(g) + 2, elem.getHeight(g) / 2 + 2
                - elem.getDescentHeight(g) + elem.getHeight(g));
    }

    /** {@inheritDoc} */
    public int calculateDescentHeight(Graphics g) {
        if (getMathElementCount() < 2) {
            return 0;
        }

        return getMathElement(0).getDescentHeight(g) + 2;
    }
}
