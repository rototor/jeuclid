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

package net.sourceforge.jeuclid.element.generic;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.List;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.element.helpers.ElementListSupport;

/**
 * common superclass for root like elements (root, sqrt).
 * 
 * @author Max Berger
 */
public abstract class AbstractRootElement extends AbstractMathElement {

    /**
     * Char for left part of root rendering.
     */
    public static final char ROOT_CHAR = '\u221A';

    private static final float INTERNAL_SCALE_FACTOR = 100.0f;

    /**
     * Default constructor.
     * 
     * @param base
     *            MathBase to use.
     */
    public AbstractRootElement(final MathBase base) {
        super(base);
    }

    /**
     * TODO.
     * 
     * @return TODO
     */
    protected abstract MathElement getLeft();

    /**
     * TODO.
     * 
     * @return TODO
     */
    protected abstract List<MathElement> getContent();

    /** {@inheritDoc} */
    @Override
    public int calculateAscentHeight(final Graphics2D g) {

        final List<MathElement> elements = this.getContent();
        final int asHeight = ElementListSupport.getAscentHeight(g, elements);
        final int desHeight = ElementListSupport
                .getDescentHeight(g, elements);
        final int height = asHeight + desHeight;
        return Math.max(asHeight + 2, height / 2 + 2 - desHeight + height);
    }

    /** {@inheritDoc} */
    @Override
    public int calculateDescentHeight(final Graphics2D g) {
        return ElementListSupport.getDescentHeight(g, this.getContent()) + 2;
    }

    /** {@inheritDoc} */
    @Override
    public int calculateWidth(final Graphics2D g) {
        return ElementListSupport.getWidth(g, this.getContent())
                + this.getRootWidth(g) + 1;
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

        super.paint(g, posX, posY);
        final List<MathElement> content = this.getContent();
        final MathElement e2 = this.getLeft();

        final int width1 = ElementListSupport.getWidth(g, content);
        final int height1 = ElementListSupport.getHeight(g, content);

        final Font font = g.getFont().deriveFont(
                this.getFontsizeInPoint()
                        * AbstractRootElement.INTERNAL_SCALE_FACTOR);
        final GlyphVector gv = font.createGlyphVector(g
                .getFontRenderContext(),
                new char[] { AbstractRootElement.ROOT_CHAR });
        final Rectangle2D gbounds = gv.getGlyphMetrics(0).getBounds2D();
        final double glyphWidth = gbounds.getWidth()
                / AbstractRootElement.INTERNAL_SCALE_FACTOR;
        final double glyphHeight = gbounds.getHeight()
                / AbstractRootElement.INTERNAL_SCALE_FACTOR;
        final double ascent = gbounds.getY()
                / AbstractRootElement.INTERNAL_SCALE_FACTOR;

        double yScale;
        double xScale;

        final int width2 = Math.max((int) Math.round(e2.getWidth(g)
                - glyphWidth / 2), 0);

        yScale = (height1 + 4) / glyphHeight;
        xScale = 1;

        final AffineTransform transform = g.getTransform();
        final AffineTransform prevTransform = g.getTransform();
        transform.scale(xScale, yScale);

        double y = posY + this.getDescentHeight(g);
        y = y - (ascent + glyphHeight) * yScale;
        double x = posX + width2;
        y = y / yScale;
        x = x / xScale;

        g.setTransform(transform);
        g.drawString(String.valueOf(AbstractRootElement.ROOT_CHAR),
                (float) x, (float) y);
        g.setTransform(prevTransform);
        final Shape oldClip = g.getClip();

        final int contentDes = ElementListSupport
                .getDescentHeight(g, content);

        g.clipRect(posX + width2, posY + contentDes - height1 - 2,
                (int) ((glyphWidth + width2) * xScale) + 1 + width1 - width2,
                height1 + 4);

        final int rightTopRootPoint = posY + contentDes - height1 - 2;

        g.drawLine((int) (posX + (glyphWidth + width2) * xScale) + 1,
                rightTopRootPoint, posX + this.getWidth(g) + 1,
                rightTopRootPoint);
        g.setClip(oldClip);

        ElementListSupport.paint(g, posX + this.getRootWidth(g) + 1, posY,
                content);
        e2.paint(g, posX, posY + contentDes - e2.getDescentHeight(g)
                - height1 / 2);
    }

    private int getRootWidth(final Graphics2D g) {
        int result = 0;

        final FontRenderContext context = new FontRenderContext(
                new AffineTransform(), false, false);
        final GlyphVector gv = this.getFont().createGlyphVector(context,
                new char[] { AbstractRootElement.ROOT_CHAR });
        result = (int) (gv.getGlyphMetrics(0).getBounds2D().getWidth());
        result += Math.max(this.getLeft().getWidth(g) - 0.5 * result, 0);
        return result;
    }

}
