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

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.element.generic.AbstractMathElementWithChildren;

import org.w3c.dom.mathml.MathMLEncloseElement;

/**
 * @author Dmitry Mironovich
 * @since 21.02.2005, 15:01:33
 */
public class MathEnclose extends AbstractMathElementWithChildren implements
        MathMLEncloseElement {

    // TODO: This class needs to be cleaned up.

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "menclose";

    /**
     * Char for rendering left part of the lonogdivision.
     */
    public static final char LONGDIV_CHAR = ')';

    /** The notation attribute. */
    public static final String ATTR_NOTATION = "notation";

    // longdiv | actuarial | radical | box | roundedbox | circle | left |
    // right
    // | top | bottom |
    // updiagonalstrike | downdiagonalstrike

    private Integer isLongdiv;

    private Integer isRadical;

    private Integer isUpdiagonalstrike;

    private Integer isDowndiagonalstrike;

    private final SortedSet<Integer> notations = new TreeSet<Integer>();

    /**
     * Creates a math element.
     * 
     * @param base
     *            The base for the math element tree.
     */
    public MathEnclose(final MathBase base) {
        super(base);
    }

    /**
     * @return notation of menclose element
     */
    public String getNotation() {
        return this.getMathAttribute(MathEnclose.ATTR_NOTATION);
    }

    /**
     * Sets notation for menclose element.
     * 
     * @param notation
     *            Notation
     */
    public void setNotation(final String notation) {
        this.setAttribute(MathEnclose.ATTR_NOTATION, notation);
        this.parseNotation();
    }

    private void parseNotation() {
        final String notation = this.getNotation();
        this.isLongdiv = new Integer(notation.indexOf("longdiv"));
        if (this.isLongdiv.intValue() > -1) {
            this.notations.add(this.isLongdiv);
        }
        this.isUpdiagonalstrike = new Integer(notation
                .indexOf("updiagonalstrike"));
        if (this.isUpdiagonalstrike.intValue() > -1) {
            this.notations.add(this.isUpdiagonalstrike);
        }
        this.isDowndiagonalstrike = new Integer(notation
                .indexOf("downdiagonalstrike"));
        if (this.isDowndiagonalstrike.intValue() > -1) {
            this.notations.add(this.isDowndiagonalstrike);
        }
        this.isRadical = new Integer(notation.indexOf("radical"));
        if (this.isRadical.intValue() > -1) {
            this.notations.add(this.isRadical);
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void changeHook() {
        super.changeHook();
        this.parseNotation();
    }

    /** {@inheritDoc} */
    @Override
    public void paint(final Graphics2D g, final int posX, final int posY) {
        super.paint(g, posX, posY);
        Integer currentNotation = null;
        final int width = this.getWidth(g);
        final int ascHeight = this.getAscentHeight(g);
        final int descHeight = this.getDescentHeight(g);
        final NotationDesc nd = new NotationDesc(posX, posY, width,
                ascHeight, descHeight);
        for (final Object element2 : this.notations) {
            currentNotation = (Integer) element2;
            this.drawNotation(currentNotation, g, nd);
        }
        super.paint(g, nd.mposX, nd.mposY);
    }

    private int getRootWidth(final char root) {
        final java.awt.font.FontRenderContext context = new FontRenderContext(
                new java.awt.geom.AffineTransform(), false, false);
        final GlyphVector gv = this.getFont().createGlyphVector(context,
                new char[] { root });
        final int result = (int) gv.getGlyphMetrics(0).getBounds2D()
                .getWidth();
        return result + 2;
    }

    private class NotationDesc {
        private int mposX;

        private int mposY;

        private int mwidth;

        private int mascHeight;

        private int mdescHeight;

        /**
         * @param posX
         * @param posY
         * @param width
         * @param ascHeight
         * @param descHeight
         */
        public NotationDesc(final int posX, final int posY, final int width,
                final int ascHeight, final int descHeight) {
            super();
            this.mposX = posX;
            this.mposY = posY;
            this.mwidth = width;
            this.mascHeight = ascHeight;
            this.mdescHeight = descHeight;
        }
    }

    /**
     * Render notation
     * 
     * @param notation
     *            Notation
     * @param g
     *            Graphics2D for rendering
     * @param nd
     *            dimensions of the natation
     */
    private void drawNotation(final Integer notation, final Graphics2D g2d,
            final NotationDesc nd) {
        if (notation == this.isLongdiv) {
            final java.awt.Font font = this.getFont();
            final GlyphVector gv = font.createGlyphVector(g2d
                    .getFontRenderContext(),
                    new char[] { MathEnclose.LONGDIV_CHAR });
            final Rectangle2D gbounds = gv.getGlyphMetrics(0).getBounds2D();
            final double glyphWidth = gbounds.getWidth();
            final double glyphHeight = gbounds.getHeight();
            double yScale;
            double xScale;
            yScale = (nd.mascHeight + nd.mdescHeight) / glyphHeight;
            xScale = 1;
            final java.awt.geom.AffineTransform transform = g2d
                    .getTransform();
            final java.awt.geom.AffineTransform prevTransform = g2d
                    .getTransform();
            transform.scale(xScale, yScale);
            double y = nd.mposY + nd.mdescHeight;
            y = y - (gbounds.getY() + gbounds.getHeight()) * yScale;
            double x = nd.mposX;
            y = y / yScale;
            x = x / xScale;
            final Shape oldClip = g2d.getClip();
            g2d.clipRect(nd.mposX - 1,
                    (int) (nd.mposY + nd.mdescHeight - glyphHeight * yScale),
                    (int) (glyphWidth * xScale + 2), (int) (glyphHeight
                            * yScale + 2));
            g2d.setTransform(transform);
            g2d.drawGlyphVector(gv, (float) x, (float) y);
            g2d.setTransform(prevTransform);
            g2d.setClip(oldClip);
            final int rightTopPoint = (int) ((nd.mposY + nd.mdescHeight - glyphHeight
                    * yScale));
            g2d.drawLine(nd.mposX + 1, rightTopPoint, nd.mposX + 1
                    + nd.mwidth, rightTopPoint);
            nd.mposX = nd.mposX + this.getRootWidth(MathEnclose.LONGDIV_CHAR)
                    + 1;
            nd.mwidth = nd.mwidth
                    - this.getRootWidth(MathEnclose.LONGDIV_CHAR) - 2;
            nd.mascHeight = nd.mascHeight - 2;
            nd.mdescHeight = nd.mdescHeight - 2;
        } else if (notation == this.isUpdiagonalstrike) {
            g2d.drawLine(nd.mposX, nd.mposY + nd.mdescHeight, nd.mposX
                    + nd.mwidth, nd.mposY - nd.mascHeight);
        } else if (notation == this.isDowndiagonalstrike) {
            g2d.drawLine(nd.mposX, nd.mposY - nd.mascHeight, nd.mposX
                    + nd.mwidth, nd.mposY + nd.mdescHeight);
        }
    }

    /** {@inheritDoc} */
    @Override
    public int calculateWidth(final Graphics2D g) {
        int width = super.calculateChildrenWidth(g);
        Integer notation = null;
        for (final Object element2 : this.notations) {
            notation = (Integer) element2;
            if (notation == this.isLongdiv) {
                width = width + this.getRootWidth(MathEnclose.LONGDIV_CHAR)
                        + 2;
            }
        }
        return width;
    }

    /** {@inheritDoc} */
    @Override
    public int calculateAscentHeight(final Graphics2D g) {
        int ah = this.calculateChildrenAscentHeight(g);
        Integer notation = null;
        for (final Object element2 : this.notations) {
            notation = (Integer) element2;
            if (notation == this.isLongdiv) {
                ah = ah + 2;
            }
        }
        return ah;
    }

    /** {@inheritDoc} */
    @Override
    public int calculateDescentHeight(final Graphics2D g) {
        int dh = super.calculateChildrenDescentHeight(g);
        Integer notation = null;
        for (final Object element2 : this.notations) {
            notation = (Integer) element2;
            if (notation == this.isLongdiv) {
                dh = dh + 2;
            }
        }
        return dh;
    }

    /** {@inheritDoc} */
    public String getTagName() {
        return MathEnclose.ELEMENT;
    }

}
