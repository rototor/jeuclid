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

/* $Id: MathSup.java,v 1.11.2.3 2006/11/04 04:28:29 maxberger Exp $ */

package net.sourceforge.jeuclid.element;

import java.awt.Graphics;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.element.generic.AbstractMathElement;

/**
 * This class arrange an element lower to an other element.
 * 
 */
public class MathSup extends AbstractMathElement {

    private static final double DY = 0.43 / 2;

    /**
     * The XML element from this class.
     */

    public static final String ELEMENT = "msup";

    /**
     * Value of superscriptshift property.
     */
    private int m_superscriptshift = 0;

    /**
     * Creates a math element.
     * 
     * @param base
     *            The base for the math element tree.
     */
    public MathSup(MathBase base) {
        super(base);
    }

    /**
     * Sets value of subscriptshift.
     * 
     * @param superscriptshift
     *            Value of subscriptshift property.
     */
    public void setSuperScriptShift(int superscriptshift) {
        m_superscriptshift = superscriptshift;
    }

    /**
     * Gets value of superscriptshift.
     * 
     * @return Value of superscriptshift property.
     */
    public int getSuperScriptShift() {
        return m_superscriptshift;
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
    public void paint(Graphics g, int posX, int posY) {
        super.paint(g, posX, posY);
        AbstractMathElement e1 = getMathElement(0);
        AbstractMathElement e2 = getMathElement(1);

        int middleshift = (int) (e1.getHeight(g) * DY);

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
        int posY1 = posY + e1DescentHeight + e2.getAscentHeight(g)
                - middleshift;

        if (posY2 + e2.getHeight(g) > posY1) {
            posY2 = posY1 - e2.getHeight(g);
            // if main symbol is too small, sup- and subsymblos have not to be
            // laid one on onother.
        }
        e1.paint(g, posX, posY);
        e2.paint(g, posX + e1.getWidth(g), posY2);
    }

    /** {@inheritDoc} */
    public int calculateWidth(Graphics g) {
        return getMathElement(0).getWidth(g) + getMathElement(1).getWidth(g);
    }

    /** {@inheritDoc} */
    public int calculateAscentHeight(Graphics g) {
        int e2h = Math.max(getMathElement(1).getHeight(g)
                - (int) (getMathElement(0).getHeight(g) * DY), 0);
        return getMathElement(0).getAscentHeight(g) + e2h;
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
