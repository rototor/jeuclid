/*
 * Copyright 2008 - 2008 JEuclid, http://jeuclid.sf.net
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

package net.sourceforge.jeuclid.context;

import net.sourceforge.jeuclid.context.typewrapper.BooleanTypeWrapper;
import net.sourceforge.jeuclid.context.typewrapper.ColorTypeWrapper;
import net.sourceforge.jeuclid.context.typewrapper.EnumTypeWrapper;
import net.sourceforge.jeuclid.context.typewrapper.NumberTypeWrapper;
import net.sourceforge.jeuclid.context.typewrapper.TLIListTypeWrapper;
import net.sourceforge.jeuclid.context.typewrapper.TypeWrapper;

/**
 * Possible parameters for the LayoutContext.
 * 
 * @version $Revision$
 */
public enum Parameter {
    /**
     * Display style (Display).
     */
    DISPLAY(EnumTypeWrapper.getInstance(Display.class), false, "display",
            "display style"),

    /**
     * Font size (Float) used for the output. Defaults to 12.0pt. Please Note:
     * You may also want to set SCRIPTMINZISE.
     */
    MATHSIZE(NumberTypeWrapper.getInstance(Float.class), false, "fontSize",
            "font size used for the output (mathsize)"),

    /**
     * Font size (Float) for smallest script used. Defaults to 8.0pt.
     */
    SCRIPTMINSIZE(NumberTypeWrapper.getInstance(Float.class), false,
            "scriptMinSize", "font size to be used for smallest script"),

    /** Script size multiplier (Float), defaults to 0.71. */
    SCRIPTSIZEMULTIPLIER(NumberTypeWrapper.getInstance(Float.class), false,
            "scriptSizeMult", "script size multiplier"),

    /** Script level (Integer), defaults to 0. */
    SCRIPTLEVEL(NumberTypeWrapper.getInstance(Integer.class), false,
            "scriptLevel", "script level"),

    /**
     * Minimum font size for which anti-alias is turned on. Defaults to 10.0pt
     */
    ANTIALIAS_MINSIZE(NumberTypeWrapper.getInstance(Float.class), false,
            "antiAliasMinSize",
            "minimum font size for which anti-alias is turned on"),

    /**
     * Debug mode (Boolean). If true, elements will have borders drawn around
     * them.
     */
    DEBUG(BooleanTypeWrapper.getInstance(), false, "debug",
            "debug mode - if on, elements will have borders drawn around them"),

    /**
     * Anti-Alias mode (Boolean) for rendering.
     */
    ANTIALIAS(BooleanTypeWrapper.getInstance(), false, "antiAlias",
            "anti-alias mode"),

    /**
     * Default foreground color (Color). See 3.2.2.2
     */
    MATHCOLOR(ColorTypeWrapper.getInstance(), false, "foregroundColor",
            "default foreground color (mathcolor)"),

    /**
     * Default background color (Color), may be null. See 3.2.2.2
     */
    MATHBACKGROUND(ColorTypeWrapper.getInstance(), true, "backgroundColor",
            "default background color (mathbackground)"),

    /**
     * List&lt;String&gt; of font families for sans-serif.
     * 
     * @see Parameter
     */
    FONTS_SANSSERIF(TLIListTypeWrapper.getInstance(), false,
            "fontsSansSerif", "list of font families for Sans-Serif"),

    /**
     * List&lt;String&gt; of font families for serif.
     * 
     * @see Parameter
     */
    FONTS_SERIF(TLIListTypeWrapper.getInstance(), false, "fontsSerif",
            "list of font families for Serif"),

    /**
     * List&lt;String&gt; of font families for monospaced.
     * 
     * @see Parameter
     */
    FONTS_MONOSPACED(TLIListTypeWrapper.getInstance(), false,
            "fontsMonospaced", "list of font families for Monospaced"),

    /**
     * CList&lt;String&gt; of font families for script.
     * 
     * @see Parameter
     */
    FONTS_SCRIPT(TLIListTypeWrapper.getInstance(), false, "fontsScript",

    "list of font families for Script"),
    /**
     * List&lt;String&gt; of font families for fraktur.
     * 
     * @see Parameter
     */
    FONTS_FRAKTUR(TLIListTypeWrapper.getInstance(), false, "fontsFraktur",
            "list of font families for Fraktur"),

    /**
     * List&lt;String&gt; of font families for double-struck.
     * 
     * @see Parameter
     */
    FONTS_DOUBLESTRUCK(TLIListTypeWrapper.getInstance(), false,
            "fontsDoublestruck", "list of font families for Double-Struck"),

    /**
     * If true, &lt;mfrac&gt; element will NEVER increase children's
     * scriptlevel (in violation of the spec); otherwise it will behave with
     * accordance to the spec.
     */
    MFRAC_KEEP_SCRIPTLEVEL(
            BooleanTypeWrapper.getInstance(),
            false,
            "mfracKeepScriptLevel",
            "if true, <mfrac> element will NEVER increase children's scriptlevel (in violation of the spec)");

    private final TypeWrapper typeWrapper;

    private final boolean nullAllowed;

    private final String optionName;

    private final String optionDesc;

    private Parameter(final TypeWrapper aTypeWrapper,
            final boolean nullIsAllowed, final String oName,
            final String oDesc) {
        this.typeWrapper = aTypeWrapper;
        this.nullAllowed = nullIsAllowed;
        this.optionName = oName;
        this.optionDesc = oDesc;
    }

    /**
     * @return TypeWrapper instance used for this parameter
     */
    public TypeWrapper getTypeWrapper() {
        return this.typeWrapper;
    }

    /**
     * @return user-friendly option name associated with this parameter
     */
    public String getOptionName() {
        return this.optionName;
    }

    /**
     * @return user-friendly option name associated with this parameter
     */
    public String getOptionDesc() {
        return this.optionDesc;
    }

    /**
     * Checks if the object is of a valid type for this parameter.
     * 
     * @param o
     *            the object to check
     * @return true if the parameter can be set.
     */
    public boolean valid(final Object o) {
        return o == null && this.nullAllowed || this.typeWrapper.valid(o);
    }

    /**
     * Attempts to convert a parameter value expressed as string into an
     * instance of the appropriate (for this parameter) type.
     * 
     * @param value
     *            parameter value as string
     * @return parameter value as an instance of the proper type
     */
    public Object fromString(final String value) {
        return this.typeWrapper.fromString(value);
    }

    /**
     * Attempts to convert a parameter value expressed as an object of the
     * appropriate (for this parameter) type into a string representation.
     * 
     * @param value
     *            parameter value as object
     * @return parameter value as string
     */
    public String toString(final Object value) {
        return this.typeWrapper.toString(value);
    }
}
