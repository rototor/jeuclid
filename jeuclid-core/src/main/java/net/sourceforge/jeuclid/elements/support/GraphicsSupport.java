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
import net.sourceforge.jeuclid.elements.JEuclidElement;

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
     * Retrieve the width of a line that would be 1pt if unscaled.
     * 
     * @param context
     *            the context element
     * @return linewidth as float
     */
    public static float lineWidth(final JEuclidElement context) {
        final float lineSize = context.getFontsizeInPoint()
                / Constants.DEFAULT_FONTSIZE;
        // Maybe enable this... probably not.
        // if (lineSize < 1.0f) {
        // lineSize = 1.0f;
        // }
        return lineSize;
    }

}
