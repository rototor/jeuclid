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

package net.sourceforge.jeuclid.elements.presentation.general;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.elements.AbstractJEuclidElement;
import net.sourceforge.jeuclid.elements.JEuclidElement;
import net.sourceforge.jeuclid.elements.support.ElementListSupport;

/**
 * common superclass for root like elements (root, sqrt).
 * 
 * @author Max Berger
 * @version $Revision$
 */
public abstract class AbstractRoot extends AbstractJEuclidElement {

    /**
     * Char for left part of root rendering.
     */
    public static final char ROOT_CHAR = '\u221A';

    private static final int EXTRA_VERTICAL_SPACE = 4;

    private static final float INTERNAL_SCALE_FACTOR = 100.0f;

    /**
     * Default constructor.
     * 
     * @param base
     *            MathBase to use.
     */
    public AbstractRoot(final MathBase base) {
        super(base);
    }

    /**
     * retrieve the actual index for this radical.
     * 
     * @return a MathElement representing what to draw as the index
     */
    protected abstract JEuclidElement getActualIndex();

    /**
     * retrieve the content of this radical element.
     * 
     * @return A List&lt;MathElement&gt; with the contents for this element.
     */
    protected abstract List<JEuclidElement> getContent();

    /** {@inheritDoc} */
    @Override
    public float calculateAscentHeight(final Graphics2D g) {

        final List<JEuclidElement> elements = this.getContent();
        final float asHeight = ElementListSupport
                .getAscentHeight(g, elements);
        final float desHeight = ElementListSupport.getDescentHeight(g,
                elements);
        final float height = asHeight + desHeight;
        return Math.max(asHeight + 2, height / 2 + 2 - desHeight
                + this.getActualIndex().getHeight(g));
    }

    /** {@inheritDoc} */
    @Override
    public float calculateDescentHeight(final Graphics2D g) {
        return ElementListSupport.getDescentHeight(g, this.getContent()) + 2;
    }

    /** {@inheritDoc} */
    @Override
    public float calculateWidth(final Graphics2D g) {
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
    public void paint(final Graphics2D g, final float posX, final float posY) {

        super.paint(g, posX, posY);
        final List<JEuclidElement> content = this.getContent();
        final JEuclidElement e2 = this.getActualIndex();

        final float width1 = ElementListSupport.getWidth(g, content);
        final float height1 = ElementListSupport.getHeight(g, content);

        final Font font = g.getFont().deriveFont(
                this.getFontsizeInPoint()
                        * AbstractRoot.INTERNAL_SCALE_FACTOR);
        final GlyphVector gv = font.createGlyphVector(g
                .getFontRenderContext(),
                new char[] { AbstractRoot.ROOT_CHAR });
        final Rectangle2D gbounds = gv.getGlyphMetrics(0).getBounds2D();
        final float glyphWidth = (float) gbounds.getWidth()
                / AbstractRoot.INTERNAL_SCALE_FACTOR;
        final float glyphHeight = (float) gbounds.getHeight()
                / AbstractRoot.INTERNAL_SCALE_FACTOR;
        final float ascent = (float) gbounds.getY()
                / AbstractRoot.INTERNAL_SCALE_FACTOR;

        float yScale;
        float xScale;

        final float width2 = Math.max(e2.getWidth(g) - glyphWidth / 2, 0);

        yScale = (height1 + AbstractRoot.EXTRA_VERTICAL_SPACE) / glyphHeight;
        xScale = 1;

        final AffineTransform transform = g.getTransform();
        final AffineTransform prevTransform = g.getTransform();
        transform.scale(xScale, yScale);

        float y = posY + this.getDescentHeight(g);
        y = y - (ascent + glyphHeight) * yScale;
        float x = posX + width2;
        y = y / yScale;
        x = x / xScale;

        g.setTransform(transform);
        g.drawString(String.valueOf(AbstractRoot.ROOT_CHAR), x, y);
        g.setTransform(prevTransform);
        final Shape oldClip = g.getClip();

        final float contentDes = ElementListSupport.getDescentHeight(g,
                content);

        g.clip(new Rectangle2D.Float(posX + width2, posY + contentDes
                - height1 - 2, ((glyphWidth + width2) * xScale) + 1 + width1
                - width2, height1 + AbstractRoot.EXTRA_VERTICAL_SPACE));

        final float rightTopRootPoint = posY + contentDes - height1 - 2;

        g.draw(new Line2D.Float((posX + (glyphWidth + width2) * xScale) + 1,
                rightTopRootPoint, posX + this.getWidth(g) + 1,
                rightTopRootPoint));
        g.setClip(oldClip);

        ElementListSupport.paint(g, posX + this.getRootWidth(g) + 1, posY,
                content);
        e2.paint(g, posX, posY + contentDes - e2.getDescentHeight(g)
                - height1 / 2);
    }

    private float getRootWidth(final Graphics2D g) {
        float result = 0;

        final FontRenderContext context = new FontRenderContext(
                new AffineTransform(), false, false);
        final GlyphVector gv = this.getFont().createGlyphVector(context,
                new char[] { AbstractRoot.ROOT_CHAR });
        result = (float) (gv.getGlyphMetrics(0).getBounds2D().getWidth());
        result += Math.max(this.getActualIndex().getWidth(g) - result / 2.0f,
                0);
        return result;
    }

}
