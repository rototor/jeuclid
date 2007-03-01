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

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.element.generic.AbstractMathElement;
import net.sourceforge.jeuclid.element.generic.MathElement;
import net.sourceforge.jeuclid.element.helpers.AttributesHelper;

import org.w3c.dom.mathml.MathMLElement;
import org.w3c.dom.mathml.MathMLFractionElement;

/**
 * This math element presents a mathematical fraction.
 * 
 * @version %I%, %G%
 */
public class MathFrac extends AbstractMathElement implements
        MathMLFractionElement {
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
    public static final String ATTR_LINETHICKNESS = "linethickness";

    /** The bevelled attribute. */
    public static final String ATTR_BEVELLED = "bevelled";

    /** The numalign attribute. NOT YET SUPPORTED. */
    public static final String ATTR_NUMALIGN = "numalign";

    /** The denomalign attribute. NOT YET SUPPORTED. */
    public static final String ATTR_DENOMALIGN = "denomalign";

    /**
     * Creates a math element.
     * 
     * @param base
     *            The base for the math element tree.
     */
    public MathFrac(final MathBase base) {
        super(base);
        this.setDefaultMathAttribute(MathFrac.ATTR_LINETHICKNESS, "1px");
        this.setDefaultMathAttribute(MathFrac.ATTR_BEVELLED, "false");
        this.setDefaultMathAttribute(MathFrac.ATTR_NUMALIGN,
                AbstractMathElement.ALIGN_CENTER);
        this.setDefaultMathAttribute(MathFrac.ATTR_DENOMALIGN,
                AbstractMathElement.ALIGN_CENTER);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isChildBlock(final MathElement child) {
        return false;
    }

    /**
     * Sets the thickness of the fraction line.
     * 
     * @param linethickness
     *            Thickness
     */
    public void setLinethickness(final String linethickness) {
        this.setAttribute(MathFrac.ATTR_LINETHICKNESS, linethickness);
    }

    /**
     * @return thickness of the fraction line
     * @param g
     *            Graphics2D context to use.
     */
    public float getLinethickness(final Graphics2D g) {
        return AttributesHelper.convertSizeToPt(this.getLinethickness(),
                this, AttributesHelper.PT);
    }

    /**
     * Set value of the bevelled attribute.
     * 
     * @param bevelled
     *            Value
     */
    public void setBevelled(final String bevelled) {
        this.setAttribute(MathFrac.ATTR_BEVELLED, bevelled);
    }

    /**
     * @return Value of bevelled attribute
     */
    public String getBevelled() {
        return this.getMathAttribute(MathFrac.ATTR_BEVELLED);
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
    public void paint(final Graphics2D g, int posX, final int posY) {
        super.paint(g, posX, posY);
        final MathElement e1 = this.getMathElement(0);
        final MathElement e2 = this.getMathElement(1);

        final int middle = posY - this.getMiddleShift(g);
        final int dist = (int) AttributesHelper.convertSizeToPt("0.1em",
                this, "");

        if (Boolean.parseBoolean(this.getBevelled())) {
            final int w1 = Math.max((int) Math.round(e2.getHeight(g)
                    * MathFrac.FRAC_TILT_ANGLE), e1.getWidth(g) + dist);
            e1.paint(g, posX + w1 - e1.getWidth(g), middle
                    - e1.getDescentHeight(g));
            final float linef = this.getLinethickness(g);
            e2.paint(g, posX + w1 + dist + (int) (linef), posY
                    - this.getAscentHeight(g) + e1.getHeight(g)
                    + e2.getAscentHeight(g));
            final Stroke oldStroke = g.getStroke();
            g.setStroke(new BasicStroke(linef));
            g.drawLine(posX
                    + w1
                    + (int) (linef / 2)
                    - (int) Math.round(e2.getHeight(g)
                            * MathFrac.FRAC_TILT_ANGLE), middle
                    + e2.getHeight(g), posX
                    + w1
                    + (int) (linef / 2)
                    + (int) Math.round(e1.getHeight(g)
                            * MathFrac.FRAC_TILT_ANGLE), middle
                    - e1.getHeight(g));
            g.setStroke(oldStroke);
        } else {

            final int width = this.getWidth(g);
            posX = posX + dist;
            final float linef = this.getLinethickness(g);

            e1.paint(g, posX + (width - 2 * dist - e1.getWidth(g)) / 2,
                    middle - e1.getDescentHeight(g) - 2
                            - (int) (this.getLinethickness(g) / 2));

            final Stroke oldStroke = g.getStroke();
            g.setStroke(new BasicStroke(linef));
            g.drawLine(posX, middle, posX + width - dist * 2, middle);
            g.setStroke(oldStroke);

            e2.paint(g, posX + (width - 2 * dist - e2.getWidth(g)) / 2,
                    middle + e2.getAscentHeight(g) + 2
                            + (int) (this.getLinethickness(g) / 2));
        }
    }

    /** {@inheritDoc} */
    @Override
    public int calculateWidth(final Graphics2D g) {
        final MathElement e1 = this.getMathElement(0);
        final MathElement e2 = this.getMathElement(1);
        final int dist = (int) AttributesHelper.convertSizeToPt("0.1em",
                this, "");
        if (Boolean.parseBoolean(this.getBevelled())) {
            final int w1 = Math.max((int) Math.round(e2.getHeight(g)
                    * MathFrac.FRAC_TILT_ANGLE), e1.getWidth(g) + dist);
            final int w2 = Math.max((int) Math.round(e1.getHeight(g)
                    * MathFrac.FRAC_TILT_ANGLE), e2.getWidth(g) + dist);
            return w1 + w2 + (int) this.getLinethickness(g);
        } else {
            return Math.max(e1.getWidth(g), e2.getWidth(g)) + dist * 2;
        }
    }

    /** {@inheritDoc} */
    @Override
    public int calculateAscentHeight(final Graphics2D g) {
        if (Boolean.parseBoolean(this.getBevelled())) {
            return this.getMathElement(0).getHeight(g)
                    + this.getMiddleShift(g);
        } else {
            return this.getMathElement(0).getHeight(g) + 2
                    + (int) (this.getLinethickness(g) / 2)
                    + this.getMiddleShift(g);
        }
    }

    /** {@inheritDoc} */
    @Override
    public int calculateDescentHeight(final Graphics2D g) {
        if (Boolean.parseBoolean(this.getBevelled())) {
            return Math.max(0, this.getMathElement(1).getHeight(g)
                    - this.getMiddleShift(g));
        } else {
            return Math.max(0, this.getMathElement(1).getHeight(g) + 2
                    + (int) (this.getLinethickness(g) / 2)
                    - this.getMiddleShift(g));
        }
    }

    /** {@inheritDoc} */
    public String getTagName() {
        return MathFrac.ELEMENT;
    }

    /** {@inheritDoc} */
    public MathMLElement getDenominator() {
        return this.getMathElement(1);
    }

    /** {@inheritDoc} */
    public String getLinethickness() {
        return this.getMathAttribute(MathFrac.ATTR_LINETHICKNESS);
    }

    /** {@inheritDoc} */
    public MathMLElement getNumerator() {
        return this.getMathElement(0);
    }

    /** {@inheritDoc} */
    public void setDenominator(final MathMLElement denominator) {
        this.setMathElement(1, denominator);
    }

    /** {@inheritDoc} */
    public void setNumerator(final MathMLElement numerator) {
        this.setMathElement(0, numerator);
    }

}
