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

/* $Id: MathFrac.java,v 1.1.2.3 2007/02/10 22:57:21 maxberger Exp $ */

package net.sourceforge.jeuclid.element;

import java.awt.Graphics2D;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.element.generic.AbstractMathElement;
import net.sourceforge.jeuclid.element.helpers.AttributesHelper;

/**
 * This math element presents a mathematical fraction.
 * 
 * @version %I%, %G%
 */
public class MathFrac extends AbstractMathElement {
    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "mfrac";

    /**
     * Tilt angle for frac.
     */
    public static final double FRAC_TILT_ANGLE = 0.577;

    /**
     * Attribute name of the linethickness property.
     */
    public static final String ATTRIBUTE_LINETHICKNESS = "linethickness";

    private String m_linethickness = "medium";

    private boolean m_bevelled = false;

    /**
     * Creates a math element.
     * 
     * @param base
     *            The base for the math element tree.
     */
    public MathFrac(MathBase base) {
        super(base);
    }

    /** {@inheritDoc} */
    protected boolean isChildBlock(AbstractMathElement child) {
        return false;
    }

    /**
     * Sets the thickness of the fraction line.
     * 
     * @param linethickness
     *            Thickness
     */
    public void setLinethickness(String linethickness) {
        m_linethickness = linethickness;
    }

    /**
     * @return thickness of the fraction line
     * @param g
     *            Graphics2D context to use.
     */
    public int getLinethickness(Graphics2D g) {
        try {
            m_linethickness = String.valueOf(Integer.valueOf(m_linethickness)
                    .shortValue()
                    * AttributesHelper.getPixels("medium", getFontMetrics(g)))
                    + "px";
        } catch (Exception e) {
        }
        return AttributesHelper.getPixels(m_linethickness, getFontMetrics(g));
    }

    /**
     * Set value of the bevelled attribute.
     * 
     * @param bevelled
     *            Value
     */
    public void setBevelled(boolean bevelled) {
        m_bevelled = bevelled;
    }

    /**
     * @return Value of bevelled attribute
     */
    public boolean getBevelled() {
        return m_bevelled;
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
    public void paint(Graphics2D g, int posX, int posY) {
        super.paint(g, posX, posY);
        AbstractMathElement e1 = getMathElement(0);
        AbstractMathElement e2 = getMathElement(1);

        int middle = posY - getMiddleShift(g);
        int dist = AttributesHelper.getPixels("0.1em", getFontMetrics(g));

        if (getBevelled()) {
            int w1 = Math.max((int) Math.round(e2.getHeight(g)
                    * FRAC_TILT_ANGLE), e1.getWidth(g) + dist);
            e1.paint(g, posX + w1 - e1.getWidth(g), middle
                    - e1.getDescentHeight(g));
            int linef = getLinethickness(g);
            e2.paint(g, posX + w1 + dist + linef, posY - getAscentHeight(g)
                    + e1.getHeight(g) + e2.getAscentHeight(g));
            for (int i = 0; i < linef; i++) {
                g.drawLine(posX + w1 + i
                        - (int) Math.round(e2.getHeight(g) * FRAC_TILT_ANGLE),
                        middle + e2.getHeight(g), posX
                                + w1
                                + i
                                + (int) Math.round(e1.getHeight(g)
                                        * FRAC_TILT_ANGLE), middle
                                - e1.getHeight(g));
            }
        } else {

            int width = getWidth(g);
            posX = posX + dist;

            e1.paint(g, posX + (width - 2 * dist - e1.getWidth(g)) / 2, middle
                    - e1.getDescentHeight(g) - 2 - getLinethickness(g) / 2);
            int linef = getLinethickness(g);

            for (int i = 0; i < linef; i++) {
                g.drawLine(posX, middle + i - linef / 2, posX + width - dist
                        * 2, middle + i - linef / 2);
            }
            e2.paint(g, posX + (width - 2 * dist - e2.getWidth(g)) / 2, middle
                    + e2.getAscentHeight(g) + 2 + getLinethickness(g) / 2);
        }
    }

    /** {@inheritDoc} */
    public int calculateWidth(Graphics2D g) {
        AbstractMathElement e1 = getMathElement(0);
        AbstractMathElement e2 = getMathElement(1);
        int dist = AttributesHelper.getPixels("0.1em", getFontMetrics(g));
        if (getBevelled()) {
            int w1 = Math.max((int) Math.round(e2.getHeight(g)
                    * FRAC_TILT_ANGLE), e1.getWidth(g) + dist);
            int w2 = Math.max((int) Math.round(e1.getHeight(g)
                    * FRAC_TILT_ANGLE), e2.getWidth(g) + dist);
            return w1 + w2 + getLinethickness(g);
        } else {
            return Math.max(e1.getWidth(g), e2.getWidth(g)) + dist * 2;
        }
    }

    /** {@inheritDoc} */
    public int calculateAscentHeight(Graphics2D g) {
        if (getBevelled()) {
            return getMathElement(0).getHeight(g) + getMiddleShift(g);
        } else {
            return getMathElement(0).getHeight(g) + 2 + getLinethickness(g) / 2
                    + getMiddleShift(g);
        }
    }

    /** {@inheritDoc} */
    public int calculateDescentHeight(Graphics2D g) {
        if (getBevelled()) {
            return Math.max(0, getMathElement(1).getHeight(g)
                    - getMiddleShift(g));
        } else {
            return Math.max(0, getMathElement(1).getHeight(g) + 2
                    + getLinethickness(g) / 2 - getMiddleShift(g));
        }
    }

    /** {@inheritDoc} */
    public String getTagName() {
        return ELEMENT;
    }

}
