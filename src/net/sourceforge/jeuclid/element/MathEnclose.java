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

/* $Id: MathEnclose.java,v 1.7.2.3 2006/11/18 00:24:13 maxberger Exp $ */

package net.sourceforge.jeuclid.element;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.element.generic.AbstractMathElementWithChildren;

/**
 * @author Dmitry Mironovich
 * @since 21.02.2005, 15:01:33
 */
public class MathEnclose extends AbstractMathElementWithChildren {

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "menclose";

    /**
     * Char for rendering left part of the lonogdivision.
     */
    public static final char LONGDIV_CHAR = ')';

    private String m_notation;

    // longdiv | actuarial | radical | box | roundedbox | circle | left | right
    // | top | bottom |
    // updiagonalstrike | downdiagonalstrike

    private Integer isLongdiv;

    private Integer isRadical;

    private Integer isUpdiagonalstrike;

    private Integer isDowndiagonalstrike;

    private SortedSet notations = new TreeSet();

    /**
     * Creates a math element.
     * 
     * @param base
     *            The base for the math element tree.
     */
    public MathEnclose(MathBase base) {
        super(base);
    }

    /**
     * @return notation of menclose element
     */
    public String getNotation() {
        return m_notation;
    }

    /**
     * Sets notation for menclose element.
     * 
     * @param notation
     *            Notation
     */
    public void setNotation(String notation) {
        isLongdiv = new Integer(notation.indexOf("longdiv"));
        if (isLongdiv.intValue() > -1) {
            notations.add(isLongdiv);
        }
        isUpdiagonalstrike = new Integer(notation.indexOf("updiagonalstrike"));
        if (isUpdiagonalstrike.intValue() > -1) {
            notations.add(isUpdiagonalstrike);
        }
        isDowndiagonalstrike = new Integer(notation
                .indexOf("downdiagonalstrike"));
        if (isDowndiagonalstrike.intValue() > -1) {
            notations.add(isDowndiagonalstrike);
        }
        isRadical = new Integer(notation.indexOf("radical"));
        if (isRadical.intValue() > -1) {
            notations.add(isRadical);
        }
        m_notation = notation;
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
        Integer currentNotation = null;
        int width = getWidth(g);
        int ascHeight = getAscentHeight(g);
        int descHeight = getDescentHeight(g);
        NotationDesc nd = new NotationDesc(posX, posY, width, ascHeight,
                descHeight);
        for (Iterator i = notations.iterator(); i.hasNext();) {
            currentNotation = (Integer) i.next();
            drawNotation(currentNotation, g, nd);
        }
        super.paint(g, nd.m_posX, nd.m_posY);
    }

    private int getRootWidth(char root) {
        java.awt.font.FontRenderContext context = new FontRenderContext(
                new java.awt.geom.AffineTransform(), false, false);
        GlyphVector gv = (getFont().createGlyphVector(context,
                new char[] { root }));
        int result = (int) gv.getGlyphMetrics(0).getBounds2D().getWidth();
        return result + 2;
    }

    private class NotationDesc {
        private int m_posX;

        private int m_posY;

        private int m_width;

        private int m_ascHeight;

        private int m_descHeight;

        /**
         * @param posX
         * @param posY
         * @param width
         * @param ascHeight
         * @param descHeight
         */
        public NotationDesc(int posX, int posY, int width, int ascHeight,
                int descHeight) {
            super();
            m_posX = posX;
            m_posY = posY;
            m_width = width;
            m_ascHeight = ascHeight;
            m_descHeight = descHeight;
        }
    }

    /**
     * Render notation
     * 
     * @param notation
     *            Notation
     * @param g
     *            Graphics for rendering
     * @param nd
     *            dimensions of the natation
     */
    private void drawNotation(Integer notation, Graphics g, NotationDesc nd) {
        if (notation == isLongdiv) {
            java.awt.Font font = getFont();
            Graphics2D g2d = (java.awt.Graphics2D) g;
            GlyphVector gv = font.createGlyphVector(g2d.getFontRenderContext(),
                    new char[] { LONGDIV_CHAR });
            Rectangle2D gbounds = gv.getGlyphMetrics(0).getBounds2D();
            double glyphWidth = gbounds.getWidth();
            double glyphHeight = gbounds.getHeight();
            double yScale, xScale;
            yScale = ((nd.m_ascHeight + nd.m_descHeight) / glyphHeight);
            xScale = 1;
            java.awt.geom.AffineTransform transform = g2d.getTransform();
            java.awt.geom.AffineTransform prevTransform = g2d.getTransform();
            transform.scale(xScale, yScale);
            double y = nd.m_posY + nd.m_descHeight;
            y = y - (gbounds.getY() + gbounds.getHeight()) * yScale;
            double x = nd.m_posX;
            y = (y / yScale);
            x = (x / xScale);
            final Shape oldClip = g2d.getClip();
            g2d.clipRect((int) nd.m_posX - 1, (int) (nd.m_posY
                    + nd.m_descHeight - glyphHeight * yScale),
                    (int) (glyphWidth * xScale + 2), (int) (glyphHeight
                            * yScale + 2));
            g2d.setTransform(transform);
            g2d.drawGlyphVector(gv, (float) x, (float) y);
            g2d.setTransform(prevTransform);
            g2d.setClip(oldClip);
            int rightTopPoint = (int) ((nd.m_posY + nd.m_descHeight - glyphHeight
                    * yScale));
            g.drawLine(nd.m_posX + 1, rightTopPoint,
                    nd.m_posX + 1 + nd.m_width, rightTopPoint);
            nd.m_posX = nd.m_posX + getRootWidth(LONGDIV_CHAR) + 1;
            nd.m_width = nd.m_width - getRootWidth(LONGDIV_CHAR) - 2;
            nd.m_ascHeight = nd.m_ascHeight - 2;
            nd.m_descHeight = nd.m_descHeight - 2;
        } else if (notation == isUpdiagonalstrike) {
            g.drawLine(nd.m_posX, nd.m_posY + nd.m_descHeight, nd.m_posX
                    + nd.m_width, nd.m_posY - nd.m_ascHeight);
        } else if (notation == isDowndiagonalstrike) {
            g.drawLine(nd.m_posX, nd.m_posY - nd.m_ascHeight, nd.m_posX
                    + nd.m_width, nd.m_posY + nd.m_descHeight);
        }
    }

    /** {@inheritDoc} */
    public int calculateWidth(Graphics g) {
        int width = super.calculateWidth(g);
        Integer notation = null;
        for (Iterator i = notations.iterator(); i.hasNext();) {
            notation = (Integer) i.next();
            if (notation == isLongdiv) {
                width = width + getRootWidth(LONGDIV_CHAR) + 2;
            }
        }
        return width;
    }

    /** {@inheritDoc} */
    public int calculateAscentHeight(Graphics g) {
        int ah = super.calculateAscentHeight(g);
        Integer notation = null;
        for (Iterator i = notations.iterator(); i.hasNext();) {
            notation = (Integer) i.next();
            if (notation == isLongdiv) {
                ah = ah + 2;
            }
        }
        return ah;
    }

    /** {@inheritDoc} */
    public int calculateDescentHeight(Graphics g) {
        int dh = super.calculateDescentHeight(g);
        Integer notation = null;
        for (Iterator i = notations.iterator(); i.hasNext();) {
            notation = (Integer) i.next();
            if (notation == isLongdiv) {
                dh = dh + 2;
            }
        }
        return dh;
    }
}
