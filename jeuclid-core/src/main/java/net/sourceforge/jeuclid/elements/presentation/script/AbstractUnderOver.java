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

package net.sourceforge.jeuclid.elements.presentation.script;

import java.awt.Graphics2D;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.elements.AbstractJEuclidElement;
import net.sourceforge.jeuclid.elements.JEuclidElement;
import net.sourceforge.jeuclid.elements.presentation.token.Mo;
import net.sourceforge.jeuclid.elements.support.attributes.AttributesHelper;

import org.w3c.dom.mathml.MathMLElement;
import org.w3c.dom.mathml.MathMLUnderOverElement;

/**
 * Implementation and helper methods for munder, mover, and munderover.
 * 
 * @todo some operators should "default" to being an accent, but currently
 *       they don't
 * @author Max Berger
 * @version $Revision$
 */
public abstract class AbstractUnderOver extends AbstractJEuclidElement
        implements MathMLUnderOverElement {

    /**
     * Space between base and under/over for accents.
     */
    public static final String UNDER_OVER_SPACE = "0.1ex";

    /** Space for non-accents multiplied by this value. */
    public static final float NON_ACCENT_MULTIPLIER = 2.5f;

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
    public AbstractUnderOver(final MathBase base) {
        super(base);
    }

    /** {@inheritDoc} */
    public String getAccent() {
        // TODO: Accent also depends on the content. See spec 3.4.4 - 3.4.6
        return this.getMathAttribute(AbstractUnderOver.ATTR_ACCENT);
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
                && (this.getBase() instanceof Mo)
                && Boolean.parseBoolean(((Mo) this.getBase())
                        .getMovablelimits())
                && !(this.getParent().isChildBlock(this));
    }

    /**
     * @param g
     *            Graphics Context.
     * @return the amount the underbaseline is shifted. Must only be called if
     *         an under element exists!
     */
    protected float getUnderBaselineShift(final Graphics2D g) {
        final JEuclidElement base = this.getBase();
        final JEuclidElement underScript = this.getUnderscript();
        final float baseshift = base.getDescentHeight(g);
        final float shift;
        if (this.limitsAreMoved()) {
            shift = ScriptSupport.getSubBaselineShift(g, base, underScript,
                    this.getOverscript());
        } else {
            float extraShift = AttributesHelper.convertSizeToPt(
                    AbstractUnderOver.UNDER_OVER_SPACE, this,
                    AttributesHelper.PT);
            if (!this.getAccentunderAsBoolean()) {
                extraShift *= AbstractUnderOver.NON_ACCENT_MULTIPLIER;
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
        final JEuclidElement base = this.getBase();
        final JEuclidElement overScript = this.getOverscript();
        final float baseAHeight = base.getAscentHeight(g);
        final float shift;
        if (this.limitsAreMoved()) {
            shift = ScriptSupport.getSuperBaselineShift(g, base, this
                    .getUnderscript(), overScript);
        } else {
            float extraShift = AttributesHelper.convertSizeToPt(
                    AbstractUnderOver.UNDER_OVER_SPACE, this,
                    AttributesHelper.PT);
            if (!this.getAccentAsBoolean()) {
                extraShift *= AbstractUnderOver.NON_ACCENT_MULTIPLIER;
            }
            shift = baseAHeight + overScript.getDescentHeight(g) + extraShift;
        }
        return shift;
    }

    /** {@inheritDoc} */
    @Override
    public final float calculateAscentHeight(final Graphics2D g) {
        final JEuclidElement over = this.getOverscript();
        final float baseAscent = this.getBase().getAscentHeight(g);
        final float overAscent;
        if (over != null) {
            overAscent = this.getOverBaselineShift(g)
                    + over.getAscentHeight(g);
        } else {
            overAscent = 0;
        }
        return Math.max(baseAscent, overAscent);
    }

    /** {@inheritDoc} */
    @Override
    public final float calculateDescentHeight(final Graphics2D g) {
        final JEuclidElement under = this.getUnderscript();
        final float baseDescent = this.getBase().getDescentHeight(g);
        final float underDescent;
        if (under != null) {
            underDescent = this.getUnderBaselineShift(g)
                    + under.getDescentHeight(g);
        } else {
            underDescent = 0;
        }
        return Math.max(baseDescent, underDescent);
    }

    /** {@inheritDoc} */
    @Override
    public float getXCenter(final Graphics2D g) {

        final float baseCenter = this.getBase().getXCenter(g);

        if (this.limitsAreMoved()) {
            return baseCenter;
        } else {
            final JEuclidElement underElement = this.getUnderscript();
            final float underCenter;
            if (underElement != null) {
                underCenter = underElement.getXCenter(g);
            } else {
                underCenter = 0;
            }
            final JEuclidElement overElement = this.getOverscript();
            final float overCenter;
            if (overElement != null) {
                overCenter = overElement.getXCenter(g);
            } else {
                overCenter = 0;
            }
            return Math.max(baseCenter, Math.max(overCenter, underCenter));
        }
    }

    /** {@inheritDoc} */
    @Override
    public float calculateWidth(final Graphics2D g) {

        final float baseWidth = this.getBase().getWidth(g);
        final JEuclidElement underElement = this.getUnderscript();
        final float underWidth;
        if (underElement != null) {
            underWidth = underElement.getWidth(g);
        } else {
            underWidth = 0;
        }
        final JEuclidElement overElement = this.getOverscript();
        final float overWidth;
        if (overElement != null) {
            overWidth = overElement.getWidth(g);
        } else {
            overWidth = 0;
        }

        final Offsets o = this.calculateOffsets(g);
        return Math.max(baseWidth + o.getBase(), Math.max(overWidth
                + o.getOver(), underWidth + o.getUnder()));
    }

    private static class Offsets {
        private final float base;

        private final float under;

        private final float over;

        public Offsets(final float b, final float u, final float o) {
            this.base = b;
            this.under = u;
            this.over = o;
        }

        public float getBase() {
            return this.base;
        }

        public float getUnder() {
            return this.under;
        }

        public float getOver() {
            return this.over;
        }
    }

    private Offsets calculateOffsets(final Graphics2D g) {
        final float baseOffsetX;
        final float underOffsetX;
        final float overOffsetX;

        final JEuclidElement base = this.getBase();
        final JEuclidElement under = this.getUnderscript();
        final JEuclidElement over = this.getOverscript();

        if (this.limitsAreMoved()) {
            baseOffsetX = 0;
            final float baseWidth = base.getWidth(g);
            underOffsetX = baseWidth;
            overOffsetX = baseWidth;
        } else {
            final float baseCenter = base.getXCenter(g);
            final float underCenter;
            final float overCenter;
            if (under != null) {
                underCenter = under.getXCenter(g);
            } else {
                underCenter = 0;
            }
            if (over != null) {
                overCenter = over.getXCenter(g);
            } else {
                overCenter = 0;
            }

            final float totalXCenter = Math.max(baseCenter, Math.max(
                    underCenter, overCenter));

            underOffsetX = totalXCenter - underCenter;
            overOffsetX = totalXCenter - overCenter;
            baseOffsetX = totalXCenter - baseCenter;
        }
        return new Offsets(baseOffsetX, underOffsetX, overOffsetX);
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
    public final void paint(final Graphics2D g, final float posX,
            final float posY) {
        super.paint(g, posX, posY);

        final Offsets o = this.calculateOffsets(g);
        final float baseOffsetX = o.getBase();
        final float underOffsetX = o.getUnder();
        final float overOffsetX = o.getOver();

        final JEuclidElement base = this.getBase();
        final JEuclidElement under = this.getUnderscript();
        final JEuclidElement over = this.getOverscript();

        base.paint(g, posX + baseOffsetX, posY);
        if (under != null) {
            under.paint(g, posX + underOffsetX, posY
                    + this.getUnderBaselineShift(g));
        }
        if (over != null) {
            over.paint(g, posX + overOffsetX, posY
                    - this.getOverBaselineShift(g));
        }
    }

    /** {@inheritDoc} */
    public String getAccentunder() {
        // TODO: Accent also depends on the content. See spec 3.4.4 - 3.4.6
        return this.getMathAttribute(AbstractUnderOver.ATTR_ACCENTUNDER);
    }

    /** {@inheritDoc} */
    @Override
    public int getScriptlevelForChild(final JEuclidElement child) {
        if (child.isSameNode(this.getBase())) {
            return this.getAbsoluteScriptLevel();
        } else {
            // TODO: Should depend on type and accent
            return this.getAbsoluteScriptLevel() + 1;
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean isChildBlock(final JEuclidElement child) {
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
    public JEuclidElement getBase() {
        return this.getMathElement(0);
    }

    /** {@inheritDoc} */
    public abstract JEuclidElement getOverscript();

    /** {@inheritDoc} */
    public abstract JEuclidElement getUnderscript();

    /** {@inheritDoc} */
    public void setAccent(final String accent) {
        this.setAttribute(AbstractUnderOver.ATTR_ACCENT, accent);
    }

    /** {@inheritDoc} */
    public void setAccentunder(final String accentunder) {
        this.setAttribute(AbstractUnderOver.ATTR_ACCENTUNDER, accentunder);
    }

    /** {@inheritDoc} */
    public void setBase(final MathMLElement base) {
        this.setMathElement(0, base);
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasChildPostscripts(final JEuclidElement child) {
        return this.limitsAreMoved() && child.isSameNode(this.getBase());
    }

}
