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

package net.sourceforge.jeuclid;

import java.awt.Color;
import java.util.List;

import net.sourceforge.jeuclid.context.Display;

/**
 * @version $Revision$
 */
public interface LayoutContext {
    /**
     * Possible parameters for the LayoutContext.
     */
    public static enum Parameter {
        /**
         * Display style (Display).
         */
        DISPLAY,
        /**
         * Font size (Float) used for the output. Defaults to 12.0pt. Please
         * Note: You may also want to set SCRIPTMINZISE.
         */
        MATHSIZE,
        /**
         * Font size (Float) for smallest script used. Defaults to 8.0pt.
         */
        SCRIPTMINSIZE,
        /** Script size multiplier (Float), defaults to 0.71. */
        SCRIPTSIZEMULTIPLIER,
        /** Script level (Integer), defaults to 0. */
        SCRIPTLEVEL,
        /**
         * Minimum font size for which anti-alias is turned on. Defaults to
         * 10.0pt
         */
        ANTIALIAS_MINSIZE,
        /**
         * Debug mode (Boolean). If true, elements will have borders drawn
         * around them.
         */
        DEBUG,
        /**
         * Anti-Alias mode (Boolean) for rendering.
         */
        ANTIALIAS,
        /**
         * Default foreground color (Color). See 3.2.2.2
         */
        MATHCOLOR,
        /**
         * Default background color (Color), may be null. See 3.2.2.2
         */
        MATHBACKGROUND,
        /**
         * List&lt;String&gt; of font families for sans-serif.
         * 
         * @see Parameter
         */
        FONTS_SANSSERIF,
        /**
         * List&lt;String&gt; of font families for serif.
         * 
         * @see Parameter
         */
        FONTS_SERIF,
        /**
         * List&lt;String&gt; of font families for monospaced.
         * 
         * @see Parameter
         */
        FONTS_MONOSPACED,
        /**
         * CList&lt;String&gt; of font families for script.
         * 
         * @see Parameter
         */
        FONTS_SCRIPT,
        /**
         * List&lt;String&gt; of font families for fraktur.
         * 
         * @see Parameter
         */
        FONTS_FRAKTUR,
        /**
         * List&lt;String&gt; of font families for double-struck.
         * 
         * @see Parameter
         */
        FONTS_DOUBLESTRUCK;

        /**
         * Checks if the object is of a valid type for this parameter.
         * 
         * @param o
         *            the object to check
         * @return true if the parameter can be set.
         */
        public boolean valid(final Object o) {
            boolean retVal;
            switch (this) {
            case DISPLAY:
                retVal = o instanceof Display;
                break;
            case MATHSIZE:
            case SCRIPTMINSIZE:
            case ANTIALIAS_MINSIZE:
            case SCRIPTSIZEMULTIPLIER:
                retVal = o instanceof Float;
                break;
            case SCRIPTLEVEL:
                retVal = o instanceof Integer;
                break;
            case DEBUG:
            case ANTIALIAS:
                retVal = o instanceof Boolean;
                break;
            case MATHCOLOR:
                retVal = o instanceof Color;
                break;
            case MATHBACKGROUND:
                retVal = (o == null) || (o instanceof Color);
                break;
            case FONTS_SANSSERIF:
            case FONTS_SERIF:
            case FONTS_MONOSPACED:
            case FONTS_SCRIPT:
            case FONTS_FRAKTUR:
            case FONTS_DOUBLESTRUCK:
                retVal = o instanceof List;
                break;
            default:
                assert false;
                retVal = false;
            }
            return retVal;
        }
    }

    /**
     * Retrieve a layout parameter.
     * 
     * @param which
     *            the parameter to retrieve
     * @return current value for this parameter. Please note: Some parameters
     *         may be null.
     */
    Object getParameter(LayoutContext.Parameter which);

}
