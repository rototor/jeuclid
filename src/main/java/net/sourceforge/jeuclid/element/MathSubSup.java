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
import net.sourceforge.jeuclid.element.generic.AbstractMathElementWithSubSuper;
import net.sourceforge.jeuclid.element.generic.MathElement;

import org.w3c.dom.mathml.MathMLElement;
import org.w3c.dom.mathml.MathMLScriptElement;

/**
 * This class arange a element lower, and a other elements upper to an
 * element.
 * 
 */
public class MathSubSup extends AbstractMathElementWithSubSuper implements
        MathMLScriptElement {

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "msubsup";

    /**
     * Creates a math element.
     * 
     * @param base
     *            The base for the math element tree.
     */
    public MathSubSup(final MathBase base) {
        super(base);
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
    public void paint(final Graphics2D g, final int posX, final int posY) {
        super.paint(g, posX, posY);
        final MathElement e1 = this.getMathElement(0);
        final MathElement e2 = this.getMathElement(1);
        final MathElement e3 = this.getMathElement(2);

        final int referenceHeight = e1.getHeight(g);

        int e1DescentHeight = e1.getDescentHeight(g);
        if (e1DescentHeight == 0) {
            e1DescentHeight = this.getFontMetrics(g).getDescent();
        }
        int e1AscentHeight = e1.getAscentHeight(g);
        if (e1AscentHeight == 0) {
            e1AscentHeight = this.getFontMetrics(g).getAscent();
        }

        final int posY1 = posY + e1DescentHeight + e2.getAscentHeight(g)
                - this.getSubMiddleShift(referenceHeight, g) - 1;

        e1.paint(g, posX, posY);
        e2.paint(g, posX + e1.getWidth(g), posY1);
        e3.paint(g, posX + e1.getWidth(g), posY
                - this.getSuperBaseLineShift(g));
    }

    /** {@inheritDoc} */
    @Override
    public int calculateWidth(final Graphics2D g) {
        return this.getMathElement(0).getWidth(g)
                + Math.max(this.getMathElement(1).getWidth(g), this
                        .getMathElement(2).getWidth(g)) + 1;
    }

    /** {@inheritDoc} */
    @Override
    public int calculateAscentHeight(final Graphics2D g) {
        return this.caclulateAscentHeightWithSuper(g);
    }

    /** {@inheritDoc} */
    @Override
    public int calculateDescentHeight(final Graphics2D g) {
        final int e2h = Math.max(this.getMathElement(1).getHeight(g)
                - this.getSubMiddleShift(this.getMathElement(0).getHeight(g),
                        g), 0);
        return this.getMathElement(0).getDescentHeight(g) + e2h;
    }

    /** {@inheritDoc} */
    @Override
    public int getScriptlevelForChild(final MathElement child) {
        if (child.isSameNode(this.getFirstChild())) {
            return this.getAbsoluteScriptLevel();
        } else {
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
        return MathSubSup.ELEMENT;
    }

    /** {@inheritDoc} */
    public MathElement getBase() {
        return this.getMathElement(0);
    }

    /** {@inheritDoc} */
    public MathElement getSubscript() {
        return this.getMathElement(1);
    }

    /** {@inheritDoc} */
    public MathElement getSuperscript() {
        return this.getMathElement(2);
    }

    /** {@inheritDoc} */
    public void setBase(final MathMLElement base) {
        this.setMathElement(0, base);
    }

    /** {@inheritDoc} */
    public void setSubscript(final MathMLElement subscript) {
        this.setMathElement(1, subscript);
    }

    /** {@inheritDoc} */
    public void setSuperscript(final MathMLElement superscript) {
        this.setMathElement(2, superscript);
    }

}
