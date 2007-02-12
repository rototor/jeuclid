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
    public MathUnderOver(MathBase base) {
        super(base);
    }

    /**
     * Sets value of accentunder property.
     * 
     * @param accentunder
     *            Value of accentunder property.
     */
    public void setAccentUnder(boolean accentunder) {
        m_accentunder = accentunder;
    }

    /**
     * Gets value of accentunder property.
     * 
     * @return Value of accentunder property.
     */
    public boolean getAccentUnder() {
        return m_accentunder;
    }

    /**
     * Sets value of accent property.
     * 
     * @param accent
     *            Value of accent property.
     */
    public void setAccent(boolean accent) {
        m_accent = accent;
    }

    /**
     * Gets value of accent property.
     * 
     * @return Value of accent property.
     */
    public boolean getAccent() {
        return m_accent;
    }

    /**
     * Space between base nad under/over in pixels
     */
    private int getUnderOverSpace(Graphics2D g) {
        return AttributesHelper.getPixels(UNDER_OVER_SPACE, getFontMetrics(g));
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
    public void paint(Graphics2D g, int posX, int posY) {
        super.paint(g, posX, posY);

        AbstractMathElement e1 = getMathElement(0);
        AbstractMathElement e2 = getMathElement(1);
        AbstractMathElement e3 = getMathElement(2);

        int posY1, posY2, posX0, posX1, posX2;

        if ((getMathElement(0) instanceof MathOperator)
                && ((MathOperator) getMathElement(0)).getMoveableLimits()) {
            int middleshift = (int) (e1.getHeight(g) * MathSubSup.DY);
            int e1DescentHeight = e1.getDescentHeight(g);
            if (e1DescentHeight == 0) {
                e1DescentHeight = getFontMetrics(g).getDescent();
            }
            int e1AscentHeight = e1.getAscentHeight(g);
            if (e1AscentHeight == 0) {
                e1AscentHeight = getFontMetrics(g).getAscent();
            }
            posY1 = posY + e1DescentHeight + e2.getAscentHeight(g)
                    - middleshift - 1;
            posY2 = posY - e1AscentHeight + middleshift
                    - e2.getDescentHeight(g) + 1;
            posX0 = posX;
            posX1 = posX + e1.getWidth(g);
            posX2 = posX + e1.getWidth(g);
        } else {
            int width = getWidth(g);
            posX0 = posX + (width - e1.getWidth(g)) / 2;
            posX1 = posX + (width - e2.getWidth(g)) / 2;
            posY1 = posY + e1.getDescentHeight(g) + e2.getAscentHeight(g)
                    + getUnderOverSpace(g) - 1;
            posX2 = posX + (width - e3.getWidth(g)) / 2;
            posY2 = posY - e1.getAscentHeight(g) - e3.getDescentHeight(g)
                    - getUnderOverSpace(g) - 1;
            if (getAccent()) {
                posY1 = posY1 + getUnderOverSpace(g);
            }
            if (getAccentUnder()) {
                posY2 = posY2 - getUnderOverSpace(g);
            }
        }
        e1.paint(g, posX0, posY);
        e2.paint(g, posX1, posY1);
        e3.paint(g, posX2, posY2);
    }

    /** {@inheritDoc} */
    public int calculateWidth(Graphics2D g) {
        if ((getMathElement(0) instanceof MathOperator)
                && ((MathOperator) getMathElement(0)).getMoveableLimits()) {
            return getMathElement(0).getWidth(g)
                    + Math.max(getMathElement(1).getWidth(g), getMathElement(2)
                            .getWidth(g)) + 1;
        }
        return Math.max(getMathElement(0).getWidth(g), Math.max(getMathElement(
                1).getWidth(g), getMathElement(2).getWidth(g)));
    }

    /** {@inheritDoc} */
    public int calculateAscentHeight(Graphics2D g) {
        int res;
        if ((getMathElement(0) instanceof MathOperator)
                && ((MathOperator) getMathElement(0)).getMoveableLimits()) {
            int e2h = (int) Math.max(getMathElement(2).getHeight(g)
                    - getMathElement(0).getHeight(g) * MathSubSup.DY, 0);
            res = getMathElement(0).getAscentHeight(g) + e2h;
        } else {
            res = getMathElement(0).getAscentHeight(g)
                    + getMathElement(2).getHeight(g) + getUnderOverSpace(g);
            if (getAccentUnder()) {
                res = res + getUnderOverSpace(g);
            }
        }
        return res;
    }

    /** {@inheritDoc} */
    public int calculateDescentHeight(Graphics2D g) {
        int res;
        if ((getMathElement(0) instanceof MathOperator)
                && ((MathOperator) getMathElement(0)).getMoveableLimits()) {
            int e2h = (int) Math.max(getMathElement(1).getHeight(g)
                    - getMathElement(0).getHeight(g) * MathSubSup.DY, 0);
            res = getMathElement(0).getDescentHeight(g) + e2h;
        } else {
            res = getMathElement(0).getDescentHeight(g)
                    + getMathElement(1).getHeight(g) + getUnderOverSpace(g);
            if (getAccentUnder()) {
                res = res + getUnderOverSpace(g);
            }
        }
        return res;
    }

    /** {@inheritDoc} */
    protected int getScriptlevelForChild(AbstractMathElement child) {
        if (child.isSameNode(this.getFirstChild())) {
            return this.getAbsoluteScriptLevel();
        } else {
            // TODO: Should depend on type and accent
            return this.getAbsoluteScriptLevel() + 1;
        }
    }

    /** {@inheritDoc} */
    protected boolean isChildBlock(AbstractMathElement child) {
        if (child.isSameNode(this.getFirstChild())) {
            return super.isChildBlock(child);
        } else {
            return false;
        }
    }

    /** {@inheritDoc} */
    public String getTagName() {
        return ELEMENT;
    }
}
