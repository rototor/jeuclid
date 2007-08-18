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

package net.sourceforge.jeuclid.layout;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.text.AttributedString;
import java.text.CharacterIterator;

import net.sourceforge.jeuclid.LayoutContext;
import net.sourceforge.jeuclid.elements.support.text.StringUtil;

/**
 * @author Max Berger
 * @version $Revision$
 */
public class TextLayout extends AbstractBorderlessNode {

    private static final float UNSET = -2.0f;

    private static final float MORE_THAN_UNSET = -1.0f;

    /**
     * Last used layout instance.
     */
    private java.awt.font.TextLayout layout;

    private float xOffset;

    private float ascentHeight;

    private float descentHeight;

    private float width;

    private final AttributedString attributedString;

    private final Graphics2D graphics2d;

    /**
     * Default Constructor.
     * 
     * @param attrString
     *            String to display.
     * @param context
     *            rendering context to use.
     * @param g2d
     *            Graphics context to use.
     */
    public TextLayout(final AttributedString attrString,
            final LayoutContext context, final Graphics2D g2d) {
        super(context);
        this.attributedString = attrString;
        this.graphics2d = g2d;
        this.resetCache();
    }

    private void produceTextLayout() {
        if (this.attributedString.getIterator().current() == CharacterIterator.DONE) {
            this.layout = null;
            this.ascentHeight = this.graphics2d.getFontMetrics().getAscent();
            this.descentHeight = this.graphics2d.getFontMetrics()
                    .getDescent();
            this.width = 0.0f;
        } else {
            this.layout = StringUtil.createTextLayoutFromAttributedString(
                    this.graphics2d, this.attributedString, this
                            .getLayoutContext());
            final Rectangle2D textBounds = this.layout.getBounds();
            final float xo = (float) textBounds.getX();
            if (xo < 0) {
                this.xOffset = -xo;
            } else {
                this.xOffset = 0.0f;
            }
            this.ascentHeight = (float) (-textBounds.getY());
            this.descentHeight = (float) (textBounds.getY() + textBounds
                    .getHeight());
            this.width = StringUtil.getWidthForTextLayout(this.layout)
                    + this.xOffset;
        }
    }

    /** {@inheritDoc} */
    public float getAscentHeight() {
        if (this.width < TextLayout.MORE_THAN_UNSET) {
            this.produceTextLayout();
        }
        return this.ascentHeight;
    }

    /** {@inheritDoc} */
    public float getDescentHeight() {
        if (this.width < TextLayout.MORE_THAN_UNSET) {
            this.produceTextLayout();
        }
        return this.descentHeight;
    }

    /** {@inheritDoc} */
    public float getWidth() {
        if (this.width < TextLayout.MORE_THAN_UNSET) {
            this.produceTextLayout();
        }
        return this.width;
    }

    /** {@inheritDoc} */
    public void resetCache() {
        this.width = TextLayout.UNSET;
    }

    /** {@inheritDoc} */
    @Override
    public void actuallyPaint(final float x, final float y,
            final Graphics2D g2d) {
        if (this.width < TextLayout.MORE_THAN_UNSET) {
            this.produceTextLayout();
        }
        if (this.layout != null) {
            this.layout.draw(g2d, x + this.xOffset, y);
        }
    }
}
