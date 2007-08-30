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

package net.sourceforge.jeuclid.elements.presentation.token;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.text.AttributedString;

import net.sourceforge.jeuclid.LayoutContext.Parameter;
import net.sourceforge.jeuclid.elements.AbstractJEuclidElement;
import net.sourceforge.jeuclid.elements.support.text.StringUtil;
import net.sourceforge.jeuclid.layout.LayoutInfo;
import net.sourceforge.jeuclid.layout.LayoutStage;
import net.sourceforge.jeuclid.layout.LayoutView;
import net.sourceforge.jeuclid.layout.TextObject;

import org.w3c.dom.mathml.MathMLPresentationToken;

/**
 * Common functionality for all tokens based on a text layout.
 * 
 * @author Max Berger
 * @version $Revision$
 */
public abstract class AbstractTokenWithTextLayout extends
        AbstractJEuclidElement implements MathMLPresentationToken {
    /**
     * Last used layout instance.
     */
    private TextLayout layout;

    private float xOffset;

    /**
     * Default constructor.
     */
    public AbstractTokenWithTextLayout() {
        super();
    }

    /**
     * Paints this element.
     * 
     * @param g
     *            The graphics context to use for painting.
     * @param posX
     *            The first left position for painting.
     * @param posY
     *            The position of the baseline.
     */
    @Override
    public void paint(final Graphics2D g, final float posX, final float posY) {
        super.paint(g, posX, posY);
        if (!this.isEmpty()) {
            this.produceTextLayout(g);
            this.layout.draw(g, posX + this.xOffset, posY);
            // g.draw(this.produceTextLayout(g).getOutline(
            // AffineTransform.getTranslateInstance(posX, posY)));
        }
    }

    /** {@inheritDoc} */
    @Override
    public void layoutStageInvariant(final LayoutView view,
            final LayoutInfo info, final LayoutStage stage) {
        if (!this.isEmpty()) {
            final Graphics2D g = view.getGraphics();
            final TextLayout t = this.produceTextLayout(g);
            final StringUtil.TextLayoutInfo tli = StringUtil
                    .getTextLayoutInfo(t);
            info.setAscentHeight(tli.getAscent(), stage);
            info.setDescentHeight(tli.getDescent(), stage);
            final float width = tli.getWidth();
            info.setHorizontalCenterOffset(width / 2.0f, stage);
            info.setWidth(width, stage);
            info.setGraphicsObject(new TextObject(t, tli.getOffset(),
                    (Color) this.getCurrentLayoutContext().getParameter(
                            Parameter.MATHCOLOR)));
        }
    }

    /**
     * Retrieve the text content as attributed string. Should be overridden
     * 
     * @return an AttributedString
     */
    protected abstract AttributedString textContentAsAttributedString();

    /**
     * Checks if this element is empty.
     * 
     * @return true if empty.
     */
    protected abstract boolean isEmpty();

    private TextLayout produceTextLayout(final Graphics2D g2d) {
        if (this.layout == null) {

            this.layout = StringUtil.createTextLayoutFromAttributedString(
                    g2d, this.textContentAsAttributedString(), this
                            .getCurrentLayoutContext());
            final Rectangle2D r2d = this.layout.getBounds();
            final float xo = (float) r2d.getX();
            if (xo < 0) {
                this.xOffset = -xo;
            } else {
                this.xOffset = 0.0f;
            }
        }
        return this.layout;
    }

    /**
     * Returns TextLayout used to paint text of this element.
     * 
     * @return TextLayout instance or null, if layout was not computed yet
     */
    public TextLayout getLayout() {
        return this.layout;
    }

    /** {@inheritDoc} */
    @Override
    public float calculateWidth(final Graphics2D g) {
        if (this.isEmpty()) {
            return 0;
        } else {
            this.produceTextLayout(g);
            return StringUtil.getWidthForTextLayout(this.layout)
                    + this.xOffset;
        }
    }

    /** {@inheritDoc} */
    @Override
    public float calculateAscentHeight(final Graphics2D g) {
        if (this.isEmpty()) {
            return g.getFontMetrics().getAscent();
        } else {
            this.produceTextLayout(g);
            // TextLayout.getAscent returns the max ascent for this font,
            // not the one for the actual content!
            final Rectangle2D textBounds = this.layout.getBounds();
            return (float) (-textBounds.getY());
        }
    }

    /** {@inheritDoc} */
    @Override
    public float calculateDescentHeight(final Graphics2D g) {
        if (this.isEmpty()) {
            return g.getFontMetrics().getDescent();
        } else {
            this.produceTextLayout(g);
            // TextLayout.getDescent returns the max descent for this font,
            // not the one for the actual content!
            final Rectangle2D textBounds = this.layout.getBounds();
            return (float) (textBounds.getY() + textBounds.getHeight());
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void changeHook() {
        super.changeHook();
        this.layout = null;
    }

}
