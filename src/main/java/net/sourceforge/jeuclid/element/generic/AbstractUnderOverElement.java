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
import net.sourceforge.jeuclid.element.MathOperator;
import net.sourceforge.jeuclid.element.MathSubSup;
import net.sourceforge.jeuclid.element.helpers.AttributesHelper;

import org.w3c.dom.mathml.MathMLElement;
import org.w3c.dom.mathml.MathMLUnderOverElement;

/**
 * Implementation and helper methods for munder, mover, and munderover.
 * 
 * @todo This class has common functionality with
 *       AbstractMathElementWithSubSuper
 * @author Max Berger
 */
public abstract class AbstractUnderOverElement extends AbstractMathElement
        implements MathMLUnderOverElement {

    /**
     * Space between base and under/over for accents.
     */
    public static final String UNDER_OVER_SPACE = "0.1ex";

    /** Space for non-accents multiplied by this value. */
    public static final float NON_ACCENT_MULTIPLIER = 5.0f;

    /** attribute for accent property. */
    public static final String ATTR_ACCENT = "accent";

    /** attribute for accentunder property. */
    public static final String ATTR_ACCENTUNDER = "accentunder";

    /**
     * default constructor.
     * 
     * @param base
     *            Mathbase to use.
     */
    public AbstractUnderOverElement(final MathBase base) {
        super(base);
    }

    /** {@inheritDoc} */
    public String getAccent() {
        // TODO: Accent also depends on the content. See spec 3.4.4 - 3.4.6
        return this.getMathAttribute(AbstractUnderOverElement.ATTR_ACCENT);
    }

    /**
     * returns the accent property as boolean.
     * 
     * @return accent
     */
    protected boolean getAccentAsBoolean() {
        return Boolean.parseBoolean(this.getAccent());
    }

    /**
     * @return true if limits are moved (behave like under/over).
     */
    protected boolean limitsAreMoved() {
        return (!this.getAccentAsBoolean())
                && (this.getBase() instanceof MathOperator)
                && Boolean.parseBoolean(((MathOperator) this.getBase())
                        .getMovablelimits());
    }

    /**
     * @param g
     *            Graphics Context.
     * @return the amount the underbaseline is shifted. Must only be called if
     *         an under element exists!
     */
    protected float getUnderBaselineShift(final Graphics2D g) {
        final MathElement base = this.getBase();
        final MathElement underScript = this.getUnderscript();
        final float baseshift = base.getDescentHeight(g);
        final float shift;
        if (this.limitsAreMoved()) {

            final int middleshift = (int) (base.getHeight(g) * MathSubSup.DEFAULT_SCRIPTSHIFT);
            int e1DescentHeight = (int) baseshift;
            if (e1DescentHeight == 0) {
                e1DescentHeight = this.getFontMetrics(g).getDescent();
            }
            shift = +e1DescentHeight + underScript.getAscentHeight(g)
                    - middleshift - 1;
        } else {
            float extraShift = AttributesHelper.convertSizeToPt(
                    AbstractUnderOverElement.UNDER_OVER_SPACE, this,
                    AttributesHelper.PT);
            if (!this.getAccentunderAsBoolean()) {
                extraShift *= AbstractUnderOverElement.NON_ACCENT_MULTIPLIER;
            }
            shift = baseshift + extraShift + underScript.getAscentHeight(g);
        }
        return shift;
    }

    /**
     * @param g
     *            Graphics Context.
     * @return the amount the overbaseline is shifted. Must only be called if
     *         an over element exists!
     */
    protected float getOverBaselineShift(final Graphics2D g) {
        final MathElement base = this.getBase();
        final MathElement overScript = this.getOverscript();
        final float baseAHeight = base.getAscentHeight(g);
        final float shift;
        if (this.limitsAreMoved()) {
            final int middleshift = (int) (base.getHeight(g) * MathSubSup.DEFAULT_SCRIPTSHIFT);
            float e1AscentHeight = baseAHeight;
            if (e1AscentHeight == 0) {
                e1AscentHeight = this.getFontMetrics(g).getAscent();
            }
            shift = e1AscentHeight - middleshift
                    + overScript.getDescentHeight(g);
        } else {
            float extraShift = AttributesHelper.convertSizeToPt(
                    AbstractUnderOverElement.UNDER_OVER_SPACE, this,
                    AttributesHelper.PT);
            if (!this.getAccentAsBoolean()) {
                extraShift *= AbstractUnderOverElement.NON_ACCENT_MULTIPLIER;
            }
            shift = baseAHeight + overScript.getDescentHeight(g) + extraShift;
        }
        return shift;
    }

    /** {@inheritDoc} */
    @Override
    public final int calculateAscentHeight(final Graphics2D g) {
        final MathElement over = this.getOverscript();
        final int baseAscent = this.getBase().getAscentHeight(g);
        final int overAscent;
        if (over != null) {
            overAscent = (int) (this.getOverBaselineShift(g) + over
                    .getAscentHeight(g));
        } else {
            overAscent = 0;
        }
        return Math.max(baseAscent, overAscent);
    }

    /** {@inheritDoc} */
    @Override
    public final int calculateDescentHeight(final Graphics2D g) {
        final MathElement under = this.getUnderscript();
        final int baseDescent = this.getBase().getDescentHeight(g);
        final int underDescent;
        if (under != null) {
            underDescent = (int) (this.getUnderBaselineShift(g) + under
                    .getDescentHeight(g));
        } else {
            underDescent = 0;
        }
        return Math.max(baseDescent, underDescent);
    }

    /** {@inheritDoc} */
    @Override
    public int calculateWidth(final Graphics2D g) {

        final int baseWidth = this.getBase().getWidth(g);
        final MathElement underElement = this.getUnderscript();
        final int underWidth;
        if (underElement != null) {
            underWidth = underElement.getWidth(g);
        } else {
            underWidth = 0;
        }
        final MathElement overElement = this.getOverscript();
        final int overWidth;
        if (overElement != null) {
            overWidth = overElement.getWidth(g);
        } else {
            overWidth = 0;
        }

        if (this.limitsAreMoved()) {
            return baseWidth + Math.max(underWidth, overWidth) + 1;
        }
        return Math.max(baseWidth, Math.max(overWidth, underWidth));
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
    public final void paint(final Graphics2D g, final int posX, final int posY) {
        super.paint(g, posX, posY);

        final float baseOffsetX;
        final float underOffsetX;
        final float overOffsetX;

        final MathElement base = this.getBase();
        final MathElement under = this.getUnderscript();
        final MathElement over = this.getOverscript();

        if (this.limitsAreMoved()) {
            baseOffsetX = 0;
            underOffsetX = base.getWidth(g);
            overOffsetX = base.getWidth(g);
        } else {
            final int width = this.getWidth(g);
            baseOffsetX = (width - base.getWidth(g)) / 2.0f;
            if (under != null) {
                underOffsetX = (width - under.getWidth(g)) / 2.0f;
            } else {
                underOffsetX = 0;
            }
            if (over != null) {
                overOffsetX = (width - over.getWidth(g)) / 2.0f;
            } else {
                overOffsetX = 0;
            }
        }
        base.paint(g, posX + (int) baseOffsetX, posY);
        if (under != null) {
            under.paint(g, posX + (int) underOffsetX, posY
                    + (int) this.getUnderBaselineShift(g));
        }
        if (over != null) {
            over.paint(g, posX + (int) overOffsetX, posY
                    - (int) this.getOverBaselineShift(g));
        }
    }

    /** {@inheritDoc} */
    public String getAccentunder() {
        // TODO: Accent also depends on the content. See spec 3.4.4 - 3.4.6
        return this
                .getMathAttribute(AbstractUnderOverElement.ATTR_ACCENTUNDER);
    }

    /** {@inheritDoc} */
    @Override
    public int getScriptlevelForChild(final MathElement child) {
        if (child.isSameNode(this.getBase())) {
            return this.getAbsoluteScriptLevel();
        } else {
            // TODO: Should depend on type and accent
            return this.getAbsoluteScriptLevel() + 1;
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean isChildBlock(final MathElement child) {
        if (child.isSameNode(this.getBase())) {
            return super.isChildBlock(child);
        } else {
            return false;
        }
    }

    /**
     * returns the accentunder property as boolean.
     * 
     * @return accentunder
     */
    protected boolean getAccentunderAsBoolean() {
        return Boolean.parseBoolean(this.getAccentunder());
    }

    /** {@inheritDoc} */
    public MathElement getBase() {
        return this.getMathElement(0);
    }

    /** {@inheritDoc} */
    public abstract MathElement getOverscript();

    /** {@inheritDoc} */
    public abstract MathElement getUnderscript();

    /** {@inheritDoc} */
    public void setAccent(final String accent) {
        this.setAttribute(AbstractUnderOverElement.ATTR_ACCENT, accent);
    }

    /** {@inheritDoc} */
    public void setAccentunder(final String accentunder) {
        this.setAttribute(AbstractUnderOverElement.ATTR_ACCENTUNDER,
                accentunder);
    }

    /** {@inheritDoc} */
    public void setBase(final MathMLElement base) {
        this.setMathElement(0, base);
    }

}
