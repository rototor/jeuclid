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
import net.sourceforge.jeuclid.elements.JEuclidElement;
import net.sourceforge.jeuclid.elements.support.Dimension2DImpl;
import net.sourceforge.jeuclid.elements.support.ElementListSupport;
import net.sourceforge.jeuclid.elements.support.attributes.AttributesHelper;
import net.sourceforge.jeuclid.layout.LayoutInfo;
import net.sourceforge.jeuclid.layout.LayoutStage;
import net.sourceforge.jeuclid.layout.LayoutView;

/**
 * Support class for script elements.
 * 
 * @see AbstractSubSuper
 * @see AbstractUnderOver
 * @author Max Berger
 * @version $Revision$
 */
public final class ScriptSupport {
    /** Default Constructor. */
    private ScriptSupport() {
        // Empty on purpose.
    }

    // CHECKSTYLE:OFF
    // More than 7 parameters. But only used internally, so that's ok.
    static void layout(final LayoutView view, final LayoutInfo info,
            final LayoutStage stage, final LayoutContext now,
            final JEuclidElement parent, final JEuclidElement base,
            final JEuclidElement sub, final JEuclidElement sup,
            final String subScriptShift, final String superScriptShift) {
        // CHECKSTYLE:ON
        final LayoutInfo baseInfo = view.getInfo(base);
        final LayoutInfo subInfo;
        final LayoutInfo superInfo;
        final float width = baseInfo.getWidth(stage);

        float subShift = 0.0f;
        float superShift = 0.0f;

        if (sub != null) {
            subInfo = view.getInfo(sub);
            subShift = Math.max(baseInfo.getDescentHeight(stage)
                    + (subInfo.getAscentHeight(stage) - subInfo
                            .getDescentHeight(stage)) / 2.0f,
                    AttributesHelper.convertSizeToPt(subScriptShift, now,
                            AttributesHelper.PT));

        } else {
            subInfo = null;
        }
        if (sup != null) {
            superInfo = view.getInfo(sup);
            superShift = Math.max(baseInfo.getAscentHeight(stage)
                    - (superInfo.getAscentHeight(stage) - superInfo
                            .getDescentHeight(stage)) / 2.0f,
                    AttributesHelper.convertSizeToPt(superScriptShift, now,
                            AttributesHelper.PT));
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
        ElementListSupport.fillInfoFromChildren(view, info, parent, stage,
                borderLeftTop, borderRightBottom);
    }

}
