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
 * This class arrange an element under, and an other element over an element.
 * 
 */
public class MathUnderOver extends AbstractMathElement {

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "munderover";

    /**
     * Space between base nad under/over.
     */
    public static final String UNDER_OVER_SPACE = "0.2em";

    /**
     * Value of accentunder property.
     */
    private boolean m_accentunder = false;

    /**
     * Value of accent property.
     */
    private boolean m_accent = false;

    /**
     * Creates a math element.
     * 
     * @param base
     *            The base for the math element tree.
     */
    public MathUnderOver(final MathBase base) {
        super(base);
    }

    /**
     * Sets value of accentunder property.
     * 
     * @param accentunder
     *            Value of accentunder property.
     */
    public void setAccentUnder(final boolean accentunder) {
        this.m_accentunder = accentunder;
    }

    /**
     * Gets value of accentunder property.
     * 
     * @return Value of accentunder property.
     */
    public boolean getAccentUnder() {
        return this.m_accentunder;
    }

    /**
     * Sets value of accent property.
     * 
     * @param accent
     *            Value of accent property.
     */
    public void setAccent(final boolean accent) {
        this.m_accent = accent;
    }

    /**
     * Gets value of accent property.
     * 
     * @return Value of accent property.
     */
    public boolean getAccent() {
        return this.m_accent;
    }

    /**
     * Space between base nad under/over in pixels
     */
    private int getUnderOverSpace(final Graphics2D g) {
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
    public void paint(final Graphics2D g, final int posX, final int posY) {
        super.paint(g, posX, posY);

        final MathElement e1 = this.getMathElement(0);
        final MathElement e2 = this.getMathElement(1);
        final MathElement e3 = this.getMathElement(2);

        int posY1, posY2, posX0, posX1, posX2;

        if ((this.getMathElement(0) instanceof MathOperator)
                && ((MathOperator) this.getMathElement(0))
                        .getMoveableLimits()) {
            final int middleshift = (int) (e1.getHeight(g) * MathSubSup.DY);
            int e1DescentHeight = e1.getDescentHeight(g);
            if (e1DescentHeight == 0) {
                e1DescentHeight = this.getFontMetrics(g).getDescent();
            }
            int e1AscentHeight = e1.getAscentHeight(g);
            if (e1AscentHeight == 0) {
                e1AscentHeight = this.getFontMetrics(g).getAscent();
            }
            posY1 = posY + e1DescentHeight + e2.getAscentHeight(g)
                    - middleshift - 1;
            posY2 = posY - e1AscentHeight + middleshift
                    - e2.getDescentHeight(g) + 1;
            posX0 = posX;
            posX1 = posX + e1.getWidth(g);
            posX2 = posX + e1.getWidth(g);
        } else {
            final int width = this.getWidth(g);
            posX0 = posX + (width - e1.getWidth(g)) / 2;
            posX1 = posX + (width - e2.getWidth(g)) / 2;
            posY1 = posY + e1.getDescentHeight(g) + e2.getAscentHeight(g)
                    + this.getUnderOverSpace(g) - 1;
            posX2 = posX + (width - e3.getWidth(g)) / 2;
            posY2 = posY - e1.getAscentHeight(g) - e3.getDescentHeight(g)
                    - this.getUnderOverSpace(g) - 1;
            if (this.getAccent()) {
                posY1 = posY1 + this.getUnderOverSpace(g);
            }
            if (this.getAccentUnder()) {
                posY2 = posY2 - this.getUnderOverSpace(g);
            }
        }
        e1.paint(g, posX0, posY);
        e2.paint(g, posX1, posY1);
        e3.paint(g, posX2, posY2);
    }

    /** {@inheritDoc} */
    @Override
    public int calculateWidth(final Graphics2D g) {
        if ((this.getMathElement(0) instanceof MathOperator)
                && ((MathOperator) this.getMathElement(0))
                        .getMoveableLimits()) {
            return this.getMathElement(0).getWidth(g)
                    + Math.max(this.getMathElement(1).getWidth(g), this
                            .getMathElement(2).getWidth(g)) + 1;
        }
        return Math.max(this.getMathElement(0).getWidth(g), Math.max(this
                .getMathElement(1).getWidth(g), this.getMathElement(2)
                .getWidth(g)));
    }

    /** {@inheritDoc} */
    @Override
    public int calculateAscentHeight(final Graphics2D g) {
        int res;
        if ((this.getMathElement(0) instanceof MathOperator)
                && ((MathOperator) this.getMathElement(0))
                        .getMoveableLimits()) {
            final int e2h = (int) Math.max(this.getMathElement(2)
                    .getHeight(g)
                    - this.getMathElement(0).getHeight(g) * MathSubSup.DY, 0);
            res = this.getMathElement(0).getAscentHeight(g) + e2h;
        } else {
            res = this.getMathElement(0).getAscentHeight(g)
                    + this.getMathElement(2).getHeight(g)
                    + this.getUnderOverSpace(g);
            if (this.getAccentUnder()) {
                res = res + this.getUnderOverSpace(g);
            }
        }
        return res;
    }

    /** {@inheritDoc} */
    @Override
    public int calculateDescentHeight(final Graphics2D g) {
        int res;
        if ((this.getMathElement(0) instanceof MathOperator)
                && ((MathOperator) this.getMathElement(0))
                        .getMoveableLimits()) {
            final int e2h = (int) Math.max(this.getMathElement(1)
                    .getHeight(g)
                    - this.getMathElement(0).getHeight(g) * MathSubSup.DY, 0);
            res = this.getMathElement(0).getDescentHeight(g) + e2h;
        } else {
            res = this.getMathElement(0).getDescentHeight(g)
                    + this.getMathElement(1).getHeight(g)
                    + this.getUnderOverSpace(g);
            if (this.getAccentUnder()) {
                res = res + this.getUnderOverSpace(g);
            }
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
        return MathUnderOver.ELEMENT;
    }
}
