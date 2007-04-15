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
import net.sourceforge.jeuclid.element.generic.AbstractUnderOverElement;
import net.sourceforge.jeuclid.element.generic.MathElement;
import net.sourceforge.jeuclid.element.helpers.AttributesHelper;

import org.w3c.dom.mathml.MathMLElement;

/**
 * This class arrange an element under, and an other element over an element.
 * 
 * @todo common functionality should be merged into AbstractUnderOverElement
 */
public class MathUnderOver extends AbstractUnderOverElement {

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "munderover";

    /**
     * Space between base nad under/over.
     */
    public static final String UNDER_OVER_SPACE = "0.2em";

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
     * Space between base nad under/over in pixels
     */
    private int getUnderOverSpace(final Graphics2D g) {
        return (int) AttributesHelper.convertSizeToPt(
                MathUnderOver.UNDER_OVER_SPACE, this, AttributesHelper.PT);
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
                && Boolean.parseBoolean(((MathOperator) this
                        .getMathElement(0)).getMovablelimits())) {
            final int middleshift = (int) (e1.getHeight(g) * MathSubSup.DEFAULT_SCRIPTSHIFT);
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
            if (this.getAccentAsBoolean()) {
                posY1 = posY1 + this.getUnderOverSpace(g);
            }
            if (this.getAccentunderAsBoolean()) {
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
                && Boolean.parseBoolean(((MathOperator) this
                        .getMathElement(0)).getMovablelimits())) {
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
                && Boolean.parseBoolean(((MathOperator) this
                        .getMathElement(0)).getMovablelimits())) {
            final int e2h = (int) Math.max(this.getMathElement(2)
                    .getHeight(g)
                    - this.getMathElement(0).getHeight(g)
                    * MathSubSup.DEFAULT_SCRIPTSHIFT, 0);
            res = this.getMathElement(0).getAscentHeight(g) + e2h;
        } else {
            res = this.getMathElement(0).getAscentHeight(g)
                    + this.getMathElement(2).getHeight(g)
                    + this.getUnderOverSpace(g);
            if (this.getAccentunderAsBoolean()) {
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
                && Boolean.parseBoolean(((MathOperator) this
                        .getMathElement(0)).getMovablelimits())) {
            final int e2h = (int) Math.max(this.getMathElement(1)
                    .getHeight(g)
                    - this.getMathElement(0).getHeight(g)
                    * MathSubSup.DEFAULT_SCRIPTSHIFT, 0);
            res = this.getMathElement(0).getDescentHeight(g) + e2h;
        } else {
            res = this.getMathElement(0).getDescentHeight(g)
                    + this.getMathElement(1).getHeight(g)
                    + this.getUnderOverSpace(g);
            if (this.getAccentunderAsBoolean()) {
                res = res + this.getUnderOverSpace(g);
            }
        }
        return res;
    }

    /** {@inheritDoc} */
    public String getTagName() {
        return MathUnderOver.ELEMENT;
    }

    /** {@inheritDoc} */
    public MathMLElement getOverscript() {
        return this.getMathElement(2);
    }

    /** {@inheritDoc} */
    public MathMLElement getUnderscript() {
        return this.getMathElement(1);
    }

    /** {@inheritDoc} */
    public void setOverscript(final MathMLElement overscript) {
        this.setMathElement(2, overscript);
    }

    /** {@inheritDoc} */
    public void setUnderscript(final MathMLElement underscript) {
        this.setMathElement(1, underscript);
    }

}
