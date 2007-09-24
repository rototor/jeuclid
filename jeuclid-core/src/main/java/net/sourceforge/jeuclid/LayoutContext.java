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
import java.util.Collections;
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
        DISPLAY(Display.class, false),
        /**
         * Font size (Float) used for the output. Defaults to 12.0pt. Please
         * Note: You may also want to set SCRIPTMINZISE.
         */
        MATHSIZE(Float.class, false),
        /**
         * Font size (Float) for smallest script used. Defaults to 8.0pt.
         */
        SCRIPTMINSIZE(Float.class, false),
        /** Script size multiplier (Float), defaults to 0.71. */
        SCRIPTSIZEMULTIPLIER(Float.class, false),
        /** Script level (Integer), defaults to 0. */
        SCRIPTLEVEL(Integer.class, false),
        /**
         * Minimum font size for which anti-alias is turned on. Defaults to
         * 10.0pt
         */
        ANTIALIAS_MINSIZE(Float.class, false),
        /**
         * Debug mode (Boolean). If true, elements will have borders drawn
         * around them.
         */
        DEBUG(Boolean.class, false),
        /**
         * Anti-Alias mode (Boolean) for rendering.
         */
        ANTIALIAS(Boolean.class, false),
        /**
         * Default foreground color (Color). See 3.2.2.2
         */
        MATHCOLOR(Color.class, false),
        /**
         * Default background color (Color), may be null. See 3.2.2.2
         */
        MATHBACKGROUND(Color.class, true),
        /**
         * List&lt;String&gt; of font families for sans-serif.
         * 
         * @see Parameter
         */
        FONTS_SANSSERIF(List.class, false),
        /**
         * List&lt;String&gt; of font families for serif.
         * 
         * @see Parameter
         */
        FONTS_SERIF(List.class, false),
        /**
         * List&lt;String&gt; of font families for monospaced.
         * 
         * @see Parameter
         */
        FONTS_MONOSPACED(List.class, false),
        /**
         * CList&lt;String&gt; of font families for script.
         * 
         * @see Parameter
         */
        FONTS_SCRIPT(List.class, false),
        /**
         * List&lt;String&gt; of font families for fraktur.
         * 
         * @see Parameter
         */
        FONTS_FRAKTUR(List.class, false),
        /**
         * List&lt;String&gt; of font families for double-struck.
         * 
         * @see Parameter
         */
        FONTS_DOUBLESTRUCK(List.class, false);
        
        private Class valueType;
        private boolean nullAllowed;
        
        private Parameter(final Class valType, final boolean nullIsAllowed) {
            this.valueType = valType;
            this.nullAllowed = nullIsAllowed;
        }

        /**
         * Checks if the object is of a valid type for this parameter.
         * 
         * @param o
         *            the object to check
         * @return true if the parameter can be set.
         */
        public boolean valid(final Object o) {
            return o == null && this.nullAllowed || this.valueType.isInstance(o);
        }
        
        
        /**
         * Attempts to convert a parameter value expressed as string 
         * into an instance of the appropriate (for this parameter) type.
         * 
         *  @param value parameter value as string
         *  @return parameter value as an instance of the proper type
         */
        public Object fromString(final String value) {
            Object retVal;
            if (value == null) {
                retVal = null;
            } else if (this.valueType == String.class) {
                retVal = value;
            } else if (Number.class.isAssignableFrom(this.valueType)) {
                try {
                    retVal = this.valueType.getConstructor(new Class[] {String.class})
                        .newInstance(new Object[] {value});
                } catch (Exception e) {
                    throw new IllegalArgumentException("Failed to convert <" + value
                            + "> to " + this.valueType, e);
                }
            } else if (this.valueType == Boolean.class) {
                retVal = Boolean.valueOf(value);
            } else if (this.valueType == Color.class) {
                try {
                    retVal = Color.class.getField(value.toLowerCase()).get(null);    
                } catch (Exception e) {
                    throw new IllegalArgumentException("<" + value
                            + "> is not a valid color name", e);
                }
            } else if (this.valueType == List.class) {
                retVal = Collections.singletonList(value);
            } else if (this.valueType.isEnum()) {
                retVal = Enum.valueOf(this.valueType, value);
                if (retVal == null) {
                    throw new IllegalArgumentException("<" + value 
                            + "> is not a valid name for enum " + this.valueType);
                }
            } else {
                throw new IllegalArgumentException("Don't know how to convert <" + value 
                        + "> to " + this.valueType);
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
