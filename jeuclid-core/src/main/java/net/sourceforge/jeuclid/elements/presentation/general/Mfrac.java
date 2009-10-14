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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;

import net.sourceforge.jeuclid.LayoutContext;
import net.sourceforge.jeuclid.context.InlineLayoutContext;
import net.sourceforge.jeuclid.context.Parameter;
import net.sourceforge.jeuclid.elements.AbstractJEuclidElement;
import net.sourceforge.jeuclid.elements.support.Dimension2DImpl;
import net.sourceforge.jeuclid.elements.support.ElementListSupport;
import net.sourceforge.jeuclid.elements.support.GraphicsSupport;
import net.sourceforge.jeuclid.elements.support.attributes.AttributesHelper;
import net.sourceforge.jeuclid.elements.support.attributes.HAlign;
import net.sourceforge.jeuclid.layout.GraphicsObject;
import net.sourceforge.jeuclid.layout.LayoutInfo;
import net.sourceforge.jeuclid.layout.LayoutStage;
import net.sourceforge.jeuclid.layout.LayoutView;
import net.sourceforge.jeuclid.layout.LayoutableNode;
import net.sourceforge.jeuclid.layout.LineObject;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.Node;
import org.w3c.dom.mathml.MathMLElement;
import org.w3c.dom.mathml.MathMLFractionElement;

/**
 * This math element presents a mathematical fraction.
 * 
 * @version $Revision$
 */
public final class Mfrac extends AbstractJEuclidElement implements
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

    /** The wrong beveled attribute. */
    public static final String ATTR_BEVELED_WRONG = "beveled";

    /** The real beveled attribute. */
    public static final String ATTR_BEVELLED = "bevelled";

    /** The numalign attribute. */
    public static final String ATTR_NUMALIGN = "numalign";

    /** The denomalign attribute. */
    public static final String ATTR_DENOMALIGN = "denomalign";

    private static final String EXTRA_SPACE_AROUND = "0.1em";

    private static final long serialVersionUID = 1L;

    /**
     * Lines thinner than this are ignored.
     */
    private static final float NOLINE_THRESHHOLD = 0.001f;

    /**
     * Default constructor. Sets MathML Namespace.
     * 
     * @param qname
     *            Qualified name.
     * @param odoc
     *            Owner Document.
     */
    public Mfrac(final String qname, final AbstractDocument odoc) {
        super(qname, odoc);

        this.setDefaultMathAttribute(Mfrac.ATTR_LINETHICKNESS, "1");
        this.setDefaultMathAttribute(Mfrac.ATTR_BEVELLED, Boolean.FALSE
                .toString());
        this.setDefaultMathAttribute(Mfrac.ATTR_NUMALIGN, HAlign.ALIGN_CENTER);
        this
                .setDefaultMathAttribute(Mfrac.ATTR_DENOMALIGN,
                        HAlign.ALIGN_CENTER);
    }

    /** {@inheritDoc} */
    @Override
    protected Node newNode() {
        return new Mfrac(this.nodeName, this.ownerDocument);
    }

    /** {@inheritDoc} */
    @Override
    public LayoutContext getChildLayoutContext(final int childNum,
            final LayoutContext context) {
        return new InlineLayoutContext(this
                .applyLocalAttributesToContext(context), !((Boolean) context
                .getParameter(Parameter.MFRAC_KEEP_SCRIPTLEVEL)).booleanValue());
    }

    /**
     * Sets the thickness of the fraction line.
     * 
     * @param newLinethickness
     *            Thickness
     */
    public void setLinethickness(final String newLinethickness) {
        this.setAttribute(Mfrac.ATTR_LINETHICKNESS, newLinethickness);
    }

    /**
     * @return thickness of the fraction line
     * @param context
     *            LayoutContext to use
     * @param g
     *            Graphics2D context to use.
     */
    public float getLinethickness(final Graphics2D g,
            final LayoutContext context) {
        final String sThickness = this.getLinethickness();
        float thickness;
        try {
            thickness = Float.parseFloat(sThickness);
            thickness *= GraphicsSupport.lineWidth(context);
        } catch (final NumberFormatException nfe) {
            thickness = AttributesHelper.convertSizeToPt(sThickness, this
                    .applyLocalAttributesToContext(context),
                    AttributesHelper.PT);
            if ((thickness < GraphicsSupport.MIN_LINEWIDTH)
                    && (thickness >= Mfrac.NOLINE_THRESHHOLD)) {
                thickness = GraphicsSupport.MIN_LINEWIDTH;
            }
        }
        return thickness;
    }

    /**
     * Set value of the beveled attribute.
     * 
     * @param bevelled
     *            Value
     */
    public void setBevelled(final String bevelled) {
        this.setAttribute(Mfrac.ATTR_BEVELLED, bevelled);
    }

    /**
     * @return Value of beveled attribute
     */
    public String getBevelled() {
        final String wrongAttr = this.getMathAttribute(
                Mfrac.ATTR_BEVELED_WRONG, false);
        if (wrongAttr == null) {
            return this.getMathAttribute(Mfrac.ATTR_BEVELLED);
        } else {
            return wrongAttr;
        }
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

    /** {@inheritDoc} */
    public String getDenomalign() {
        return this.getMathAttribute(Mfrac.ATTR_DENOMALIGN);
    }

    /** {@inheritDoc} */
    public String getNumalign() {
        return this.getMathAttribute(Mfrac.ATTR_NUMALIGN);
    }

    /** {@inheritDoc} */
    public void setDenomalign(final String denomalign) {
        this.setAttribute(Mfrac.ATTR_DENOMALIGN, denomalign);
    }

    /** {@inheritDoc} */
    public void setNumalign(final String numalign) {
        this.setAttribute(Mfrac.ATTR_NUMALIGN, numalign);
    }

    /** {@inheritDoc} */
    @Override
    protected void layoutStageInvariant(final LayoutView view,
            final LayoutInfo info, final LayoutStage stage,
            final LayoutContext context) {
        final Graphics2D g = view.getGraphics();

        final float middleShift = this.getMiddleShift(g, context);
        final boolean beveled = Boolean.parseBoolean(this.getBevelled());
        final float linethickness = this.getLinethickness(g, context);
        final float extraSpace = AttributesHelper.convertSizeToPt(
                Mfrac.EXTRA_SPACE_AROUND, this
                        .applyLocalAttributesToContext(context), "");

        final LayoutInfo numerator = view.getInfo((LayoutableNode) this
                .getNumerator());
        final LayoutInfo denominator = view.getInfo((LayoutableNode) this
                .getDenominator());

        if (beveled) {
            this.layoutBeveled(info, stage, middleShift, linethickness,
                    extraSpace, numerator, denominator, context);
        } else {
            this.layoutStacked(info, stage, middleShift, linethickness,
                    extraSpace, numerator, denominator, context);
        }

        final Dimension2D borderLeftTop = new Dimension2DImpl(extraSpace
                + linethickness, 0.0f);
        final Dimension2D borderRightBottom = new Dimension2DImpl(extraSpace
                + linethickness, 0.0f);
        ElementListSupport.fillInfoFromChildren(view, info, this, stage,
                borderLeftTop, borderRightBottom);
    }

    // CHECKSTYLE:OFF
    // More than 7 parameters - but only used here, so this is ok.
    private void layoutStacked(final LayoutInfo info, final LayoutStage stage,
            final float middleShift, final float linethickness,
            final float extraSpace, final LayoutInfo numerator,
            final LayoutInfo denominator, final LayoutContext context) {
        // CHECKSTLYE:ON
        final float numWidth = numerator.getWidth(stage);
        final float denumWidth = denominator.getWidth(stage);
        final float width = Math.max(denumWidth, numWidth);

        final float numOffset = HAlign.parseString(this.getNumalign(),
                HAlign.CENTER).getHAlignOffset(stage, numerator, width);
        final float denumOffset = HAlign.parseString(this.getDenomalign(),
                HAlign.CENTER).getHAlignOffset(stage, denominator, width);

        numerator
                .moveTo(
                        numOffset + extraSpace,
                        -(middleShift + linethickness / 2.0f + extraSpace + numerator
                                .getDescentHeight(stage)), stage);

        denominator.moveTo(denumOffset + extraSpace, -middleShift
                + linethickness / 2.0f + extraSpace
                + denominator.getAscentHeight(stage), stage);

        if (linethickness > Mfrac.NOLINE_THRESHHOLD) {
            final GraphicsObject line = new LineObject(extraSpace,
                    -middleShift, extraSpace + width, -middleShift,
                    linethickness, (Color) this.applyLocalAttributesToContext(
                            context).getParameter(Parameter.MATHCOLOR));
            info.setGraphicsObject(line);
        }
    }

    // CHECKSTYLE:OFF
    // More than 7 parameters - but only used here, so this is ok.
    private void layoutBeveled(final LayoutInfo info, final LayoutStage stage,
            final float middleShift, final float linethickness,
            final float extraSpace, final LayoutInfo numerator,
            final LayoutInfo denominator, final LayoutContext context) {
        // CHECKSTYLE:ON
        final float numPosY = -middleShift / 2.0f
                + numerator.getDescentHeight(stage);
        final float denPosY = middleShift / 2.0f
                + denominator.getDescentHeight(stage);

        final float totalAscent = Math.max(-numPosY
                + numerator.getAscentHeight(stage), -denPosY
                + denominator.getAscentHeight(stage));
        final float totalDescent = Math.max(numPosY
                + numerator.getDescentHeight(stage), denPosY
                + denominator.getDescentHeight(stage));

        final float totalHeight = totalAscent + totalDescent;
        final float lineWidth = totalHeight * Mfrac.FRAC_TILT_ANGLE;

        numerator.moveTo(extraSpace, numPosY, stage);
        float posX = numerator.getWidth(stage) + extraSpace;
        if (linethickness > Mfrac.NOLINE_THRESHHOLD) {
            final GraphicsObject line = new LineObject(posX, totalDescent,
                    lineWidth + posX, totalDescent - totalHeight,
                    linethickness, (Color) this.applyLocalAttributesToContext(
                            context).getParameter(Parameter.MATHCOLOR));
            info.setGraphicsObject(line);
        }
        posX += lineWidth;
        denominator.moveTo(posX, denPosY, stage);
    }
}
