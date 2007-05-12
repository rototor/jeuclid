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

package net.sourceforge.jeuclid.elements.presentation.general;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.elements.AbstractJEuclidElement;
import net.sourceforge.jeuclid.elements.JEuclidElement;
import net.sourceforge.jeuclid.elements.support.GraphicsSupport;
import net.sourceforge.jeuclid.elements.support.attributes.AttributesHelper;

import org.w3c.dom.mathml.MathMLElement;
import org.w3c.dom.mathml.MathMLFractionElement;

/**
 * This math element presents a mathematical fraction.
 * 
 * @author Unknown
 * @author Max Berger
 * @version $Revision$
 */
public class Mfrac extends AbstractJEuclidElement implements
        MathMLFractionElement {

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "mfrac";

    /**
     * Tilt angle for frac.
     */
    public static final float FRAC_TILT_ANGLE = 0.577f;

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

    private static final String EXTRA_SPACE_AROUND = "0.1em";

    /**
     * Creates a math element.
     * 
     * @param base
     *            The base for the math element tree.
     */
    public Mfrac(final MathBase base) {
        super(base);
        this.setDefaultMathAttribute(Mfrac.ATTR_LINETHICKNESS, "1");
        this.setDefaultMathAttribute(Mfrac.ATTR_BEVELLED, MathBase.FALSE);
        this.setDefaultMathAttribute(Mfrac.ATTR_NUMALIGN,
                AbstractJEuclidElement.ALIGN_CENTER);
        this.setDefaultMathAttribute(Mfrac.ATTR_DENOMALIGN,
                AbstractJEuclidElement.ALIGN_CENTER);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isChildBlock(final JEuclidElement child) {
        return false;
    }

    /**
     * Sets the thickness of the fraction line.
     * 
     * @param linethickness
     *            Thickness
     */
    public void setLinethickness(final String linethickness) {
        this.setAttribute(Mfrac.ATTR_LINETHICKNESS, linethickness);
    }

    /**
     * @return thickness of the fraction line
     * @param g
     *            Graphics2D context to use.
     */
    public float getLinethickness(final Graphics2D g) {
        final String sThickness = this.getLinethickness();
        float thickness;
        try {
            thickness = Float.parseFloat(sThickness);
            thickness *= GraphicsSupport.lineWidth(this);
        } catch (NumberFormatException nfe) {
            thickness = AttributesHelper.convertSizeToPt(sThickness, this,
                    AttributesHelper.PT);
        }
        return thickness;
    }

    /**
     * Set value of the bevelled attribute.
     * 
     * @param bevelled
     *            Value
     */
    public void setBevelled(final String bevelled) {
        this.setAttribute(Mfrac.ATTR_BEVELLED, bevelled);
    }

    /**
     * @return Value of bevelled attribute
     */
    public String getBevelled() {
        return this.getMathAttribute(Mfrac.ATTR_BEVELLED);
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
    public void paint(final Graphics2D g, final float posX, final float posY) {
        super.paint(g, posX, posY);
        final JEuclidElement e1 = this.getMathElement(0);
        final JEuclidElement e2 = this.getMathElement(1);

        final float middle = posY - this.getMiddleShift(g);
        final float dist = AttributesHelper.convertSizeToPt(
                Mfrac.EXTRA_SPACE_AROUND, this, "");

        if (Boolean.parseBoolean(this.getBevelled())) {
            final float w1 = Math.max(
                    e2.getHeight(g) * Mfrac.FRAC_TILT_ANGLE, e1.getWidth(g)
                            + dist);
            e1.paint(g, posX + w1 - e1.getWidth(g), middle
                    - e1.getDescentHeight(g));
            final float linef = this.getLinethickness(g);
            e2.paint(g, posX + w1 + dist + linef, posY
                    - this.getAscentHeight(g) + e1.getHeight(g)
                    + e2.getAscentHeight(g));
            final Stroke oldStroke = g.getStroke();
            g.setStroke(new BasicStroke(linef));
            g.draw(new Line2D.Float(posX + w1 + (linef / 2) - e2.getHeight(g)
                    * Mfrac.FRAC_TILT_ANGLE, middle + e2.getHeight(g), posX
                    + w1 + (linef / 2) + e1.getHeight(g)
                    * Mfrac.FRAC_TILT_ANGLE, middle - e1.getHeight(g)));
            g.setStroke(oldStroke);
        } else {

            final float width = this.getWidth(g);
            final float startX = posX + dist;
            final float linef = this.getLinethickness(g);

            e1.paint(g, startX + (width - 2 * dist - e1.getWidth(g)) / 2,
                    middle - e1.getDescentHeight(g) - 2
                            - (this.getLinethickness(g) / 2));

            final Stroke oldStroke = g.getStroke();
            g.setStroke(new BasicStroke(linef));
            g.draw(new Line2D.Float(startX, middle,
                    startX + width - dist * 2, middle));
            g.setStroke(oldStroke);

            e2.paint(g, startX + (width - 2 * dist - e2.getWidth(g)) / 2,
                    middle + e2.getAscentHeight(g) + 2
                            + (this.getLinethickness(g) / 2));
        }
    }

    /** {@inheritDoc} */
    @Override
    public float calculateWidth(final Graphics2D g) {
        final JEuclidElement e1 = this.getMathElement(0);
        final JEuclidElement e2 = this.getMathElement(1);
        final float dist = AttributesHelper.convertSizeToPt(
                Mfrac.EXTRA_SPACE_AROUND, this, "");
        if (Boolean.parseBoolean(this.getBevelled())) {
            final float w1 = Math.max(
                    e2.getHeight(g) * Mfrac.FRAC_TILT_ANGLE, e1.getWidth(g)
                            + dist);
            final float w2 = Math.max(
                    e1.getHeight(g) * Mfrac.FRAC_TILT_ANGLE, e2.getWidth(g)
                            + dist);
            return w1 + w2 + this.getLinethickness(g);
        } else {
            return Math.max(e1.getWidth(g), e2.getWidth(g)) + dist * 2;
        }
    }

    /** {@inheritDoc} */
    @Override
    public float calculateAscentHeight(final Graphics2D g) {
        if (Boolean.parseBoolean(this.getBevelled())) {
            return this.getMathElement(0).getHeight(g)
                    + this.getMiddleShift(g);
        } else {
            return this.getMathElement(0).getHeight(g) + 2
                    + (this.getLinethickness(g) / 2) + this.getMiddleShift(g);
        }
    }

    /** {@inheritDoc} */
    @Override
    public float calculateDescentHeight(final Graphics2D g) {
        if (Boolean.parseBoolean(this.getBevelled())) {
            return Math.max(0, this.getMathElement(1).getHeight(g)
                    - this.getMiddleShift(g));
        } else {
            return Math
                    .max(0, this.getMathElement(1).getHeight(g) + 2
                            + (this.getLinethickness(g) / 2)
                            - this.getMiddleShift(g));
        }
    }

    /** {@inheritDoc} */
    public String getTagName() {
        return Mfrac.ELEMENT;
    }

    /** {@inheritDoc} */
    public MathMLElement getDenominator() {
        return this.getMathElement(1);
    }

    /** {@inheritDoc} */
    public String getLinethickness() {
        return this.getMathAttribute(Mfrac.ATTR_LINETHICKNESS);
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
