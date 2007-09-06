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

import net.sourceforge.jeuclid.elements.JEuclidElement;

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

    private static float getOptimalSubBaselineShift(final Graphics2D g,
            final JEuclidElement base, final JEuclidElement sub,
            final JEuclidElement sup) {

        final float baseDescent;
        if (base != null) {
            baseDescent = base.getDescentHeight(g);
        } else {
            baseDescent = 0.0f;
        }

        final float subAscent;
        final float subDescent;
        if (sub != null) {
            subAscent = sub.getAscentHeight(g);
            subDescent = sub.getDescentHeight(g);
        } else {
            subAscent = 0.0f;
            subDescent = 0.0f;
        }
        return baseDescent + (subAscent - subDescent) / 2.0f;
    }

    private static float getOptimalSuperBaselineShift(final Graphics2D g,
            final JEuclidElement base, final JEuclidElement sub,
            final JEuclidElement sup) {
        final float baseAscent;
        if (base != null) {
            baseAscent = base.getAscentHeight(g);
        } else {
            // TODO: Use context information instead!
            baseAscent = 0.0f;
        }

        final float subAscent;
        final float subDescent;
        if (sub != null) {
            subAscent = sub.getAscentHeight(g);
            subDescent = sub.getDescentHeight(g);
        } else {
            subAscent = 0.0f;
            subDescent = 0.0f;
        }
        return baseAscent - (subAscent - subDescent) / 2.0f;
    }

    private static float getOverlap(final Graphics2D g,
            final JEuclidElement sub, final JEuclidElement sup,
            final float optimalSubBaselineShift,
            final float optimalSuperBaselineShift) {

        final float topSub = -optimalSubBaselineShift
                + sub.getAscentHeight(g) + 1.0f;
        final float bottomSuper = optimalSuperBaselineShift
                - sup.getDescentHeight(g) - 1.0f;

        final float overlap = topSub - bottomSuper;

        return Math.max(0.0f, overlap);
    }

    static float getSubBaselineShift(final Graphics2D g,
            final JEuclidElement base, final JEuclidElement sub,
            final JEuclidElement sup) {
        final float optimalSubBaselineShift = ScriptSupport
                .getOptimalSubBaselineShift(g, base, sub, sup);

        final float overlap;
        if (sup != null) {
            overlap = ScriptSupport.getOverlap(g, sub, sup,
                    optimalSubBaselineShift, ScriptSupport
                            .getOptimalSuperBaselineShift(g, base, sub, sup));
        } else {
            overlap = 0.0f;
        }

        return optimalSubBaselineShift + overlap / 2.0f;
    }

    static float getSuperBaselineShift(final Graphics2D g,
            final JEuclidElement base, final JEuclidElement sub,
            final JEuclidElement sup) {
        final float optimalSuperBaselineShift = ScriptSupport
                .getOptimalSuperBaselineShift(g, base, sub, sup);
        final float overlap;
        if (sub != null) {
            overlap = ScriptSupport.getOverlap(g, sub, sup, ScriptSupport
                    .getOptimalSubBaselineShift(g, base, sub, sup),
                    optimalSuperBaselineShift);
        } else {
            overlap = 0.0f;
        }
        return optimalSuperBaselineShift + overlap / 2.0f;
    }
}
