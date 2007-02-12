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

import java.awt.Graphics;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.element.generic.AbstractMathElement;
import net.sourceforge.jeuclid.element.helpers.AttributesHelper;

/**
 * This class arrange a element over an other element.
 * 
 */
public class MathOver extends AbstractMathElement {

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "mover";

    private boolean m_accent = false;

    /**
     * Creates a math element.
     * 
     * @param base
     *            The base for the math element tree.
     */
    public MathOver(MathBase base) {
        super(base);
    }

    /**
     * Set accent for mover.
     * 
     * @param accent
     *            Value of accent attribute
     */
    public void setAccent(boolean accent) {
        m_accent = accent;
    }

    /**
     * @return Accent
     */
    public boolean getAccent() {
        return m_accent;
    }

    /**
     * Space between base and under in pixels
     */
    private int getOverSpace(Graphics g) {
        return AttributesHelper.getPixels(MathUnderOver.UNDER_OVER_SPACE,
                getFontMetrics(g));
    };

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
        AbstractMathElement e1 = getMathElement(0);
        AbstractMathElement e2 = getMathElement(1);

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
            int posY2 = posY - e1AscentHeight + middleshift
                    - e2.getDescentHeight(g) + 1;

            e1.paint(g, posX, posY);
            e2.paint(g, posX + e1.getWidth(g), posY2);
        } else {
            int width = getWidth(g);
            e1.paint(g, posX + (width - e1.getWidth(g)) / 2, posY);
            posY = posY - e1.getAscentHeight(g) - e2.getDescentHeight(g)
                    - getOverSpace(g) - 1;
            if (getAccent()) {
                posY = posY - getOverSpace(g);
            }
            e2.paint(g, posX + (width - e2.getWidth(g)) / 2, posY);
        }
    }

    /** {@inheritDoc} */
    public int calculateWidth(Graphics g) {
        if ((getMathElement(0) instanceof MathOperator)) {
            if (((MathOperator) getMathElement(0)).getMoveableLimits()) {
                return getMathElement(0).getWidth(g)
                        + getMathElement(1).getWidth(g);
            }
        }
        return Math.max(getMathElement(0).getWidth(g), getMathElement(1)
                .getWidth(g));
    }

    /** {@inheritDoc} */
    public int calculateAscentHeight(Graphics g) {
        int res;
        if ((getMathElement(0) instanceof MathOperator)
                && ((MathOperator) getMathElement(0)).getMoveableLimits()) {
            res = Math.max(getMathElement(0).getAscentHeight(g),
                    getMathElement(1).getHeight(g) + getMiddleShift(g));
        } else {
            res = getMathElement(0).getAscentHeight(g)
                    + getMathElement(1).getHeight(g) + getOverSpace(g);
            if (getAccent()) {
                res = res + getOverSpace(g);
            }
        }
        return res;
    }

    /** {@inheritDoc} */
    public int calculateDescentHeight(Graphics g) {
        return getMathElement(0).getDescentHeight(g);
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

}
