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

import java.awt.geom.Dimension2D;

import net.sourceforge.jeuclid.LayoutContext;
import net.sourceforge.jeuclid.context.Display;
import net.sourceforge.jeuclid.context.InlineLayoutContext;
import net.sourceforge.jeuclid.context.Parameter;
import net.sourceforge.jeuclid.context.RelativeScriptlevelLayoutContext;
import net.sourceforge.jeuclid.elements.AbstractJEuclidElement;
import net.sourceforge.jeuclid.elements.JEuclidElement;
import net.sourceforge.jeuclid.elements.support.Dimension2DImpl;
import net.sourceforge.jeuclid.elements.support.ElementListSupport;
import net.sourceforge.jeuclid.elements.support.attributes.AttributesHelper;
import net.sourceforge.jeuclid.layout.LayoutInfo;
import net.sourceforge.jeuclid.layout.LayoutStage;
import net.sourceforge.jeuclid.layout.LayoutView;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.mathml.MathMLElement;
import org.w3c.dom.mathml.MathMLOperatorElement;
import org.w3c.dom.mathml.MathMLUnderOverElement;

/**
 * Implementation and helper methods for munder, mover, and munderover.
 * 
 * @todo some operators should "default" to being an accent, but currently they
 *       don't
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
     * Default constructor. Sets MathML Namespace.
     * 
     * @param qname
     *            Qualified name.
     * @param odoc
     *            Owner Document.
     */
    public AbstractUnderOver(final String qname, final AbstractDocument odoc) {
        super(qname, odoc);
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
     * @param context
     *            LayoutContext
     * @return true if limits are moved (behave like under/over).
     */
    private boolean limitsAreMoved(final LayoutContext now) {
        return (!this.getAccentAsBoolean())
                && (this.getBase() instanceof MathMLOperatorElement)
                && Boolean
                        .parseBoolean(((MathMLOperatorElement) this.getBase())
                                .getMovablelimits())
                && (Display.INLINE.equals(now.getParameter(Parameter.DISPLAY)));
    }

    /** {@inheritDoc} */
    public String getAccentunder() {
        // TODO: Accent also depends on the content. See spec 3.4.4 - 3.4.6
        return this.getMathAttribute(AbstractUnderOver.ATTR_ACCENTUNDER);
    }

    /** {@inheritDoc} */
    @Override
    public LayoutContext getChildLayoutContext(final int childNum,
            final LayoutContext context) {
        final LayoutContext now = this.applyLocalAttributesToContext(context);
        if (childNum == 0) {
            return now;
        } else {
            // TODO: Should depend on type and accent
            return new RelativeScriptlevelLayoutContext(
                    new InlineLayoutContext(now), 1);
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
    public boolean hasChildPostscripts(final JEuclidElement child,
            final LayoutContext context) {
        return this.limitsAreMoved(context) && child.isSameNode(this.getBase());
    }

    /** {@inheritDoc} */
    @Override
    protected void layoutStageInvariant(final LayoutView view,
            final LayoutInfo info, final LayoutStage stage,
            final LayoutContext context) {
        final LayoutContext now = this.applyLocalAttributesToContext(context);
        if (this.limitsAreMoved(now)) {
            ScriptSupport.layout(view, info, stage, now, this, this.getBase(),
                    this.getUnderscript(), this.getOverscript(), null, null);
        } else {
            this.layoutUnderOver(view, info, stage, now);
        }
    }

    private void layoutUnderOver(final LayoutView view, final LayoutInfo info,
            final LayoutStage stage, final LayoutContext now) {

        final JEuclidElement base = this.getBase();
        final JEuclidElement under = this.getUnderscript();
        final JEuclidElement over = this.getOverscript();

        final LayoutInfo baseInfo = view.getInfo(base);
        final LayoutInfo underInfo;
        final LayoutInfo overInfo;

        float width = baseInfo.getWidth(stage);

        final float extraShift = AttributesHelper.convertSizeToPt(
                AbstractUnderOver.UNDER_OVER_SPACE, now, AttributesHelper.PT);

        if (under == null) {
            underInfo = null;
        } else {
            underInfo = view.getInfo(under);
            width = Math.max(width, underInfo.getWidth(stage));
        }
        if (over == null) {
            overInfo = null;
        } else {
            overInfo = view.getInfo(over);
            width = Math.max(width, overInfo.getWidth(stage));
        }
        final float middle = width / 2.0f;

        baseInfo.moveTo(middle - baseInfo.getHorizontalCenterOffset(stage), 0,
                stage);

        if (under != null) {
            this.positionUnder(stage, baseInfo, underInfo, extraShift, middle);
        }
        if (over != null) {
            this.positionOver(stage, baseInfo, overInfo, extraShift, middle);
        }

        final Dimension2D borderLeftTop = new Dimension2DImpl(0.0f, 0.0f);
        final Dimension2D borderRightBottom = new Dimension2DImpl(0.0f, 0.0f);
        ElementListSupport.fillInfoFromChildren(view, info, this, stage,
                borderLeftTop, borderRightBottom);
        info.setStretchWidth(width);
        info.setStretchAscent(baseInfo.getStretchAscent());
        info.setStretchDescent(baseInfo.getStretchDescent());
    }

    private void positionUnder(final LayoutStage stage,
            final LayoutInfo baseInfo, final LayoutInfo underInfo,
            final float extraShift, final float middle) {
        final float underextra;
        if (this.getAccentunderAsBoolean()) {
            underextra = extraShift;
        } else {
            underextra = extraShift * AbstractUnderOver.NON_ACCENT_MULTIPLIER;
        }
        final float y = baseInfo.getDescentHeight(stage)
                + underInfo.getAscentHeight(stage) + underextra;
        underInfo.moveTo(middle - underInfo.getHorizontalCenterOffset(stage),
                y, stage);
    }

    private void positionOver(final LayoutStage stage,
            final LayoutInfo baseInfo, final LayoutInfo overInfo,
            final float extraShift, final float middle) {
        final float overextra;
        if (this.getAccentAsBoolean()) {
            overextra = extraShift;
        } else {
            overextra = extraShift * AbstractUnderOver.NON_ACCENT_MULTIPLIER;
        }
        final float y = baseInfo.getAscentHeight(stage)
                + overInfo.getDescentHeight(stage) + overextra;
        overInfo.moveTo(middle - overInfo.getHorizontalCenterOffset(stage), -y,
                stage);
    }
}
