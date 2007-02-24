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

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.element.generic.AbstractMathElement;
import net.sourceforge.jeuclid.element.generic.MathElement;
import net.sourceforge.jeuclid.element.helpers.AttributesHelper;

/**
 * This class arrange an element under an other element.
 * 
 */
public class MathUnder extends AbstractMathElement {

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "munder";

    /**
     * Accentunder property.
     */
    private boolean m_accentunder = false;

    /**
     * Creates a math element.
     * 
     * @param base
     *            The base for the math element tree.
     */
    public MathUnder(final MathBase base) {
        super(base);
    }

    /**
     * Sets accentunder.
     * 
     * @param accentunder
     *            accentunder
     */
    public final void setAccentUnder(final boolean accentunder) {
        this.m_accentunder = accentunder;
    }

    /**
     * Getter for accentunder property.
     * 
     * @return accentunder
     */
    public final boolean getAccentUnder() {
        return this.m_accentunder;
    }

    /**
     * Space between base and under in pixels
     */
    private int getUnderSpace(final Graphics2D g) {
        return AttributesHelper.getPixels(MathUnderOver.UNDER_OVER_SPACE,
                this.getFontMetrics(g));
    };

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
    public final void paint(final Graphics2D g, final int posX, int posY) {
        super.paint(g, posX, posY);
        final MathElement e1 = this.getMathElement(0);
        final MathElement e2 = this.getMathElement(1);

        if ((this.getMathElement(0) instanceof MathOperator)
                && ((MathOperator) this.getMathElement(0))
                        .getMoveableLimits()) {
            final int middleshift = (int) (e1.getHeight(g) * MathSubSup.DEFAULT_SCRIPTSHIFT);
            int e1DescentHeight = e1.getDescentHeight(g);
            if (e1DescentHeight == 0) {
                e1DescentHeight = this.getFontMetrics(g).getDescent();
            }
            int e1AscentHeight = e1.getAscentHeight(g);
            if (e1AscentHeight == 0) {
                e1AscentHeight = this.getFontMetrics(g).getAscent();
            }
            final int posY1 = posY + e1DescentHeight + e2.getAscentHeight(g)
                    - middleshift - 1;
            e1.paint(g, posX, posY);
            e2.paint(g, posX + e1.getWidth(g), posY1);
        } else {
            final int width = this.getWidth(g);
            e1.paint(g, posX + (width - e1.getWidth(g)) / 2, posY);
            posY = posY + e1.getDescentHeight(g) + e2.getAscentHeight(g)
                    + this.getUnderSpace(g) - 1;
            if (this.getAccentUnder()) {
                posY = posY + this.getUnderSpace(g);
            }
            e2.paint(g, posX + (width - e2.getWidth(g)) / 2, posY);
        }
    }

    /** {@inheritDoc} */
    @Override
    public final int calculateWidth(final Graphics2D g) {
        if ((this.getMathElement(0) instanceof MathOperator)) {
            if (((MathOperator) this.getMathElement(0)).getMoveableLimits()) {
                return this.getMathElement(0).getWidth(g)
                        + this.getMathElement(1).getWidth(g);
            }
        }
        return Math.max(this.getMathElement(0).getWidth(g), this
                .getMathElement(1).getWidth(g));
    }

    /** {@inheritDoc} */
    @Override
    public final int calculateAscentHeight(final Graphics2D g) {
        return this.getMathElement(0).getAscentHeight(g);
    }

    /** {@inheritDoc} */
    @Override
    public final int calculateDescentHeight(final Graphics2D g) {
        int res;
        if ((this.getMathElement(0) instanceof MathOperator)
                && ((MathOperator) this.getMathElement(0))
                        .getMoveableLimits()) {
            res = Math.max(this.getMathElement(0).getDescentHeight(g), this
                    .getMathElement(1).getHeight(g)
                    - this.getMiddleShift(g));
        } else {
            res = this.getMathElement(0).getDescentHeight(g)
                    + this.getMathElement(1).getHeight(g)
                    + this.getUnderSpace(g);
        }
        if (this.getAccentUnder()) {
            res = res + this.getUnderSpace(g);
        }
        return res;
    }

    /** {@inheritDoc} */
    @Override
    public int getScriptlevelForChild(final MathElement child) {
        if (child.isSameNode(this.getFirstChild())) {
            return this.getAbsoluteScriptLevel();
        } else {
            // TODO: Should depend on type and accent
            return this.getAbsoluteScriptLevel() + 1;
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean isChildBlock(final MathElement child) {
        if (child.isSameNode(this.getFirstChild())) {
            return super.isChildBlock(child);
        } else {
            return false;
        }
    }

    /** {@inheritDoc} */
    public String getTagName() {
        return MathUnder.ELEMENT;
    }

}
