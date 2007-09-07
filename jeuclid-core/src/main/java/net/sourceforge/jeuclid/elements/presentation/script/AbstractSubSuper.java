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
import net.sourceforge.jeuclid.context.InlineLayoutContext;
import net.sourceforge.jeuclid.elements.JEuclidElement;
import net.sourceforge.jeuclid.elements.JEuclidNode;
import net.sourceforge.jeuclid.elements.support.Dimension2DImpl;
import net.sourceforge.jeuclid.elements.support.ElementListSupport;
import net.sourceforge.jeuclid.layout.LayoutInfo;
import net.sourceforge.jeuclid.layout.LayoutStage;
import net.sourceforge.jeuclid.layout.LayoutView;

import org.w3c.dom.mathml.MathMLScriptElement;

/**
 * Generic support for all elements that have a subscript or a superscript
 * attribute.
 * <p>
 * Supported elements: msub, msup, msubsup.
 * 
 * @author Max Berger
 * @version $Revision$
 */
public abstract class AbstractSubSuper extends AbstractScriptElement
        implements MathMLScriptElement {

    /**
     * Default constructor.
     */
    public AbstractSubSuper() {
        super();
    }

    /** {@inheritDoc} */
    public abstract JEuclidElement getBase();

    /** {@inheritDoc} */
    public abstract JEuclidElement getSuperscript();

    /** {@inheritDoc} */
    public abstract JEuclidElement getSubscript();

    // /**
    // * Retrieve the amount of pts by which the baseline of the super element
    // * is shifted.
    // *
    // * @param g
    // * Graphics context to use
    // * @return baseline shift for super elements
    // */
    // protected float getSuperBaseLineShift(final Graphics2D g) {
    //
    // return Math.max(ScriptSupport.getSuperBaselineShift(g,
    // this.getBase(), this.getSubscript(), this.getSuperscript()),
    // AttributesHelper.convertSizeToPt(this.getSuperscriptshift(),
    // this.getCurrentLayoutContext(), AttributesHelper.PT));
    // }

    // /**
    // * Retrieve the amount of pts by which the baseline of the sub element
    // is
    // * shifted.
    // *
    // * @param g
    // * Graphics context to use
    // * @return baseline shift for super elements
    // */
    // protected float getSubBaseLineShift(final Graphics2D g) {
    // return Math.max(ScriptSupport.getSubBaselineShift(g, this.getBase(),
    // this.getSubscript(), this.getSuperscript()), AttributesHelper
    // .convertSizeToPt(this.getSubscriptshift(), this
    // .getCurrentLayoutContext(), AttributesHelper.PT));
    // }

    // /**
    // * Calculate the ascent height, taking a super element into account.
    // *
    // * @param g
    // * Graphics context to use
    // * @return ascent height
    // */
    // protected float caclulateAscentHeightWithSuper(final Graphics2D g) {
    // final JEuclidElement supElement = this.getSuperscript();
    // final JEuclidElement baseElement = this.getBase();
    // return Math.max(baseElement.getAscentHeight(g), this
    // .getSuperBaseLineShift(g)
    // + supElement.getAscentHeight(g));
    //
    // }

    // /**
    // * Calculate the descent height, taking a sub element into account.
    // *
    // * @param g
    // * Graphics context to use
    // * @return descent height
    // */
    // protected float caclulateDescentHeightWithSub(final Graphics2D g) {
    // final JEuclidElement subElement = this.getSubscript();
    // final JEuclidElement baseElement = this.getBase();
    // return Math.max(baseElement.getDescentHeight(g), this
    // .getSubBaseLineShift(g)
    // + subElement.getDescentHeight(g));
    //
    // }

    /** {@inheritDoc} */
    @Override
    public boolean hasChildPostscripts(final JEuclidElement child) {
        return child.isSameNode(this.getBase());
    }

    /** {@inheritDoc} */
    @Override
    public LayoutContext getChildLayoutContext(final JEuclidNode child) {
        if (child.equals(this.getFirstChild())) {
            return this.getCurrentLayoutContext();
        } else {
            return new InlineLayoutContext(this.getCurrentLayoutContext());
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void layoutStageInvariant(final LayoutView view,
            final LayoutInfo info, final LayoutStage stage) {
        // TODO Incomplete
        // TODO Move to ScriptSupport
        final JEuclidElement base = this.getBase();
        final JEuclidElement sub = this.getSubscript();
        final JEuclidElement sup = this.getSuperscript();

        final LayoutInfo baseInfo = view.getInfo(base);
        final LayoutInfo subInfo;
        final LayoutInfo superInfo;
        final float width = baseInfo.getWidth(stage);

        float subShift = 0.0f;
        float superShift = 0.0f;

        if (sub != null) {
            subInfo = view.getInfo(sub);
            subShift = baseInfo.getDescentHeight(stage)
                    + (subInfo.getAscentHeight(stage) - subInfo
                            .getDescentHeight(stage)) / 2.0f;
        } else {
            subInfo = null;
        }
        if (sup != null) {
            superInfo = view.getInfo(sup);
            superShift = baseInfo.getAscentHeight(stage)
                    - (superInfo.getAscentHeight(stage) - superInfo
                            .getDescentHeight(stage)) / 2.0f;
        } else {
            superInfo = null;
        }

        if ((subInfo != null) && (superInfo != null)) {
            final float topSub = -subShift + subInfo.getAscentHeight(stage)
                    + 1.0f;
            final float bottomSuper = superShift
                    - superInfo.getDescentHeight(stage) - 1.0f;

            final float overlap = Math.max(0.0f, topSub - bottomSuper);
            final float overlapShift = overlap / 2.0f;

            superShift += overlapShift;
            subShift += overlapShift;
        }

        if (subInfo != null) {
            subInfo.moveTo(width, subShift, stage);
        }
        if (superInfo != null) {
            superInfo.moveTo(width, -superShift, stage);
        }
        final Dimension2D borderLeftTop = new Dimension2DImpl(0.0f, 0.0f);
        final Dimension2D borderRightBottom = new Dimension2DImpl(0.0f, 0.0f);
        ElementListSupport.fillInfoFromChildren(view, info, this, stage,
                borderLeftTop, borderRightBottom);
    }
}
