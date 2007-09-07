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

package net.sourceforge.jeuclid.elements.support;

import net.sourceforge.jeuclid.Constants;
import net.sourceforge.jeuclid.LayoutContext;
import net.sourceforge.jeuclid.LayoutContext.Parameter;

/**
 * This class contains helper functions for graphical calculations.
 * 
 * @author Max Berger
 * @version $Revision$
 */
public final class GraphicsSupport {

    private GraphicsSupport() {
        // Empty on purpose.
    }

    /**
     * Gets the size of the actual font used (including scriptsizemultiplier).
     * 
     * @param context
     *            Layout context to use.
     * @return size of the current font.
     */
    public static float getFontsizeInPoint(final LayoutContext context) {
        final float scriptMultiplier = (float) Math.pow((Float) context
                .getParameter(Parameter.SCRIPTSIZEMULTIPLIER),
                (Integer) context.getParameter(Parameter.SCRIPTLEVEL));
        final float mathsize = (Float) context
                .getParameter(Parameter.MATHSIZE);
        final float scriptminsize = (Float) context
                .getParameter(Parameter.SCRIPTMINSIZE);

        final float scriptsize = mathsize * scriptMultiplier;

        return Math.max(Math.min(scriptminsize, mathsize), scriptsize);
    }

    /**
     * Retrieve the width of a line that would be 1pt if unscaled.
     * 
     * @param context
     *            Layout context to use.
     * @return linewidth as float
     */
    public static float lineWidth(final LayoutContext context) {
        final float lineSize = GraphicsSupport.getFontsizeInPoint(context)
                / Constants.DEFAULT_FONTSIZE;
        // Maybe enable this... probably not.
        // if (lineSize < 1.0f) {
        // lineSize = 1.0f;
        // }
        return lineSize;
    }

}
