/*
 * Copyright 2007 - 2007 JEuclid, http://jeuclid.sf.net
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

package net.sourceforge.jeuclid.element.generic;

import java.awt.Graphics2D;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.element.helpers.AttributesHelper;

import org.w3c.dom.mathml.MathMLScriptElement;

/**
 * Generic support for alle elements that have a subscript or a superscript
 * attribute.
 * 
 * @author Max Berger
 */
public abstract class AbstractMathElementWithSubSuper extends
        AbstractMathElement implements MathMLScriptElement {

    /** attribute for subscriptshift. */
    public static final String ATTR_SUBSCRIPTSHIFT = "subscriptshift";

    /** attribute for superscriptshift. */
    public static final String ATTR_SUPERSCRIPTSHIFT = "superscriptshift";

    /**
     * Magic constant describing the default middleshift for sub and sup
     * elements.
     * 
     * @todo Check this number. Where does it come from?
     */
    public static final float DEFAULT_SCRIPTSHIFT = 0.215f;

    private static final String NONE = "0";

    /**
     * Default constructor.
     * 
     * @param base
     *            MathBase to link to.
     */
    public AbstractMathElementWithSubSuper(final MathBase base) {
        super(base);
        this.setDefaultMathAttribute(
                AbstractMathElementWithSubSuper.ATTR_SUBSCRIPTSHIFT,
                AbstractMathElementWithSubSuper.NONE);
        this.setDefaultMathAttribute(
                AbstractMathElementWithSubSuper.ATTR_SUPERSCRIPTSHIFT,
                AbstractMathElementWithSubSuper.NONE);
    }

    /**
     * @return attribute subscriptshift.
     */
    public String getSubscriptshift() {
        return this
                .getMathAttribute(AbstractMathElementWithSubSuper.ATTR_SUBSCRIPTSHIFT);
    }

    /**
     * @param subscriptshift
     *            new value for subscriptshift
     */
    public void setSubscriptshift(final String subscriptshift) {
        this.setAttribute(
                AbstractMathElementWithSubSuper.ATTR_SUBSCRIPTSHIFT,
                subscriptshift);
    }

    /**
     * @return attribtue superscriptshift
     */
    public String getSuperscriptshift() {
        return this
                .getMathAttribute(AbstractMathElementWithSubSuper.ATTR_SUPERSCRIPTSHIFT);
    }

    /**
     * @param superscriptshift
     *            new value for superscriptshift
     */
    public void setSuperscriptshift(final String superscriptshift) {
        this.setAttribute(
                AbstractMathElementWithSubSuper.ATTR_SUPERSCRIPTSHIFT,
                superscriptshift);
    }

    /**
     * Retrieve the actual middleshift for sub elements based on reference
     * height.
     * 
     * @param reference
     *            the height of the reference element
     * @param g
     *            Graphics context to use.
     * @return subscriptshift
     */
    protected float getSubMiddleShift(final float reference,
            final Graphics2D g) {
        return reference
                * AbstractMathElementWithSubSuper.DEFAULT_SCRIPTSHIFT
                + AttributesHelper.convertSizeToPt(this.getSubscriptshift(),
                        this, AttributesHelper.PT);
    }

    /**
     * Retrieve the actual middleshift for sup elements based on reference
     * height.
     * 
     * @param reference
     *            height of the reference element
     * @param g
     *            Graphics context to use.
     * @return actual supscriptshift
     */
    protected float getSupMiddleShift(final float reference,
            final Graphics2D g) {
        return reference
                * (1.0f - AbstractMathElementWithSubSuper.DEFAULT_SCRIPTSHIFT)
                + AttributesHelper
                        .convertSizeToPt(this.getSuperscriptshift(), this,
                                AttributesHelper.PT);
    }

    /** {@inheritDoc} */
    public abstract MathElement getBase();

    /** {@inheritDoc} */
    public abstract MathElement getSuperscript();

    /** {@inheritDoc} */
    public abstract MathElement getSubscript();

    /**
     * Retrieve the amount of pts by which the baseline of the super element
     * is shifted.
     * 
     * @param g
     *            Graphics context to use
     * @return baseline shift for super elements
     */
    protected float getSuperBaseLineShift(final Graphics2D g) {
        final MathElement supElement = this.getSuperscript();
        final MathElement baseElement = this.getBase();
        final float middleshift = this.getSupMiddleShift(Math.max(baseElement
                .getHeight(g), (baseElement.getFontsizeInPoint() / 2)), g);
        final float baseDescentHeight = baseElement.getDescentHeight(g);
        final float superDescentHeight = supElement.getDescentHeight(g);

        return -baseDescentHeight + middleshift + superDescentHeight;
    }

    /**
     * Retrieve the amount of pts by which the baseline of the sub element is
     * shifted.
     * 
     * @param g
     *            Graphics context to use
     * @return baseline shift for super elements
     */
    protected float getSubBaseLineShift(final Graphics2D g) {
        final MathElement subElement = this.getSubscript();
        final MathElement baseElement = this.getBase();
        final float middleshift = this.getSubMiddleShift(Math.max(baseElement
                .getHeight(g), baseElement.getFontsizeInPoint()), g);
        final float baseDescentHeight = baseElement.getDescentHeight(g);
        final float subDescentHeight = subElement.getDescentHeight(g);

        return baseDescentHeight + middleshift + subDescentHeight;
    }

    /**
     * Calculate the ascent height, taking a super element into account.
     * 
     * @param g
     *            Graphics context to use
     * @return ascent height
     */
    protected float caclulateAscentHeightWithSuper(final Graphics2D g) {
        final MathElement supElement = this.getSuperscript();
        final MathElement baseElement = this.getBase();
        return Math.max(baseElement.getAscentHeight(g), this
                .getSuperBaseLineShift(g)
                + supElement.getAscentHeight(g));

    }

    /**
     * Calculate the descent height, taking a sub element into account.
     * 
     * @param g
     *            Graphics context to use
     * @return descent height
     */
    protected float caclulateDescentHeightWithSub(final Graphics2D g) {
        final MathElement subElement = this.getSubscript();
        final MathElement baseElement = this.getBase();
        return Math.max(baseElement.getDescentHeight(g), this
                .getSubBaseLineShift(g)
                + subElement.getDescentHeight(g));

    }

}
