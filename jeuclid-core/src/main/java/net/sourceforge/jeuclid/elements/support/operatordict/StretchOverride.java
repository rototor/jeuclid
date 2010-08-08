/*
 * Copyright 2009 - 2009 JEuclid, http://jeuclid.sf.net
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

package net.sourceforge.jeuclid.elements.support.operatordict;

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.jeuclid.elements.presentation.token.Mo;

/**
 * Contains information on characters which stretch vertical / horizontal only.
 * 
 * @version $Revision$
 */
public final class StretchOverride {
    private static final Map<String, String> STRETCHATTR = new HashMap<String, String>();

    private StretchOverride() {
        // nothing to do.
    }

    /**
     * Returns {@link Mo#VALUE_STRETCHY_HORIZONTAL} or
     * {@link Mo#VALUE_STRETCHY_VERTICAL} if the character is to be stretch only
     * in that direction.
     * 
     * @param operator
     *            operator to check
     * @return a String or null if not known.
     */
    public static String getStretchOverride(final String operator) {
        return StretchOverride.STRETCHATTR.get(operator);
    }

    // CHECKSTYLE:OFF
    // Method is too long... I know.
    static {
        // CHECKSTYLE:ON
        StretchOverride.STRETCHATTR.put("\ufe36", Mo.VALUE_STRETCHY_HORIZONTAL);
        StretchOverride.STRETCHATTR.put("\ufe37", Mo.VALUE_STRETCHY_HORIZONTAL);
        StretchOverride.STRETCHATTR.put("\ufe35", Mo.VALUE_STRETCHY_HORIZONTAL);
        StretchOverride.STRETCHATTR.put("\u0029", Mo.VALUE_STRETCHY_VERTICAL);
        StretchOverride.STRETCHATTR.put("\ufe38", Mo.VALUE_STRETCHY_HORIZONTAL);
        StretchOverride.STRETCHATTR.put("\u0028", Mo.VALUE_STRETCHY_VERTICAL);
        StretchOverride.STRETCHATTR.put("\u222c", Mo.VALUE_STRETCHY_VERTICAL);
        StretchOverride.STRETCHATTR.put("\u222d", Mo.VALUE_STRETCHY_VERTICAL);
        StretchOverride.STRETCHATTR.put("\u222e", Mo.VALUE_STRETCHY_VERTICAL);
        StretchOverride.STRETCHATTR.put("\u222f", Mo.VALUE_STRETCHY_VERTICAL);
        StretchOverride.STRETCHATTR.put("\u222b", Mo.VALUE_STRETCHY_VERTICAL);
        StretchOverride.STRETCHATTR.put("\u2230", Mo.VALUE_STRETCHY_VERTICAL);
        StretchOverride.STRETCHATTR.put("\u2233", Mo.VALUE_STRETCHY_VERTICAL);
        StretchOverride.STRETCHATTR.put("\u2232", Mo.VALUE_STRETCHY_VERTICAL);
        StretchOverride.STRETCHATTR.put("\u005e", Mo.VALUE_STRETCHY_HORIZONTAL);
        StretchOverride.STRETCHATTR.put("\u005d", Mo.VALUE_STRETCHY_VERTICAL);
        StretchOverride.STRETCHATTR.put("\u005b", Mo.VALUE_STRETCHY_VERTICAL);
        StretchOverride.STRETCHATTR.put("\u007d", Mo.VALUE_STRETCHY_VERTICAL);
        StretchOverride.STRETCHATTR.put("\u007c", Mo.VALUE_STRETCHY_VERTICAL);
        StretchOverride.STRETCHATTR.put("\u007b", Mo.VALUE_STRETCHY_VERTICAL);
        StretchOverride.STRETCHATTR.put("\u00af", Mo.VALUE_STRETCHY_HORIZONTAL);
        StretchOverride.STRETCHATTR.put("\u02c7", Mo.VALUE_STRETCHY_HORIZONTAL);
        StretchOverride.STRETCHATTR.put("\u02dc", Mo.VALUE_STRETCHY_HORIZONTAL);
        StretchOverride.STRETCHATTR.put("\u0332", Mo.VALUE_STRETCHY_HORIZONTAL);
        StretchOverride.STRETCHATTR.put("\u2500", Mo.VALUE_STRETCHY_HORIZONTAL);
        StretchOverride.STRETCHATTR.put("\u2502", Mo.VALUE_STRETCHY_VERTICAL);
        StretchOverride.STRETCHATTR.put("\u232a", Mo.VALUE_STRETCHY_VERTICAL);
        StretchOverride.STRETCHATTR.put("\u2329", Mo.VALUE_STRETCHY_VERTICAL);
        StretchOverride.STRETCHATTR.put("\u2758", Mo.VALUE_STRETCHY_VERTICAL);
        StretchOverride.STRETCHATTR.put("\u007c\u007c",
                Mo.VALUE_STRETCHY_VERTICAL);
        StretchOverride.STRETCHATTR.put("\u301a", Mo.VALUE_STRETCHY_VERTICAL);
        StretchOverride.STRETCHATTR.put("\u301b", Mo.VALUE_STRETCHY_VERTICAL);
        StretchOverride.STRETCHATTR.put("\u2308", Mo.VALUE_STRETCHY_VERTICAL);
        StretchOverride.STRETCHATTR.put("\u230b", Mo.VALUE_STRETCHY_VERTICAL);
        StretchOverride.STRETCHATTR.put("\u230a", Mo.VALUE_STRETCHY_VERTICAL);
        StretchOverride.STRETCHATTR.put("\u2309", Mo.VALUE_STRETCHY_VERTICAL);
        StretchOverride.STRETCHATTR.put("\u294e", Mo.VALUE_STRETCHY_HORIZONTAL);
        StretchOverride.STRETCHATTR.put("\u21d5", Mo.VALUE_STRETCHY_VERTICAL);
        StretchOverride.STRETCHATTR.put("\u21d4", Mo.VALUE_STRETCHY_HORIZONTAL);
        StretchOverride.STRETCHATTR.put("\u294f", Mo.VALUE_STRETCHY_VERTICAL);
        StretchOverride.STRETCHATTR.put("\u21d1", Mo.VALUE_STRETCHY_VERTICAL);
        StretchOverride.STRETCHATTR.put("\u21d0", Mo.VALUE_STRETCHY_HORIZONTAL);
        StretchOverride.STRETCHATTR.put("\u21d3", Mo.VALUE_STRETCHY_VERTICAL);
        StretchOverride.STRETCHATTR.put("\u21d2", Mo.VALUE_STRETCHY_HORIZONTAL);
        StretchOverride.STRETCHATTR.put("\u2954", Mo.VALUE_STRETCHY_VERTICAL);
        StretchOverride.STRETCHATTR.put("\u21cc", Mo.VALUE_STRETCHY_HORIZONTAL);
        StretchOverride.STRETCHATTR.put("\u2955", Mo.VALUE_STRETCHY_VERTICAL);
        StretchOverride.STRETCHATTR.put("\u2956", Mo.VALUE_STRETCHY_HORIZONTAL);
        StretchOverride.STRETCHATTR.put("\u2957", Mo.VALUE_STRETCHY_HORIZONTAL);
        StretchOverride.STRETCHATTR.put("\u2950", Mo.VALUE_STRETCHY_HORIZONTAL);
        StretchOverride.STRETCHATTR.put("\u2951", Mo.VALUE_STRETCHY_VERTICAL);
        StretchOverride.STRETCHATTR.put("\u2952", Mo.VALUE_STRETCHY_HORIZONTAL);
        StretchOverride.STRETCHATTR.put("\u2953", Mo.VALUE_STRETCHY_HORIZONTAL);
        StretchOverride.STRETCHATTR.put("\u21cb", Mo.VALUE_STRETCHY_HORIZONTAL);
        StretchOverride.STRETCHATTR.put("\u295c", Mo.VALUE_STRETCHY_VERTICAL);
        StretchOverride.STRETCHATTR.put("\u21c4", Mo.VALUE_STRETCHY_HORIZONTAL);
        StretchOverride.STRETCHATTR.put("\u21c5", Mo.VALUE_STRETCHY_VERTICAL);
        StretchOverride.STRETCHATTR.put("\u295d", Mo.VALUE_STRETCHY_VERTICAL);
        StretchOverride.STRETCHATTR.put("\u21c6", Mo.VALUE_STRETCHY_HORIZONTAL);
        StretchOverride.STRETCHATTR.put("\u295e", Mo.VALUE_STRETCHY_HORIZONTAL);
        StretchOverride.STRETCHATTR.put("\u295f", Mo.VALUE_STRETCHY_HORIZONTAL);
        StretchOverride.STRETCHATTR.put("\u2958", Mo.VALUE_STRETCHY_VERTICAL);
        StretchOverride.STRETCHATTR.put("\u21c0", Mo.VALUE_STRETCHY_HORIZONTAL);
        StretchOverride.STRETCHATTR.put("\u2959", Mo.VALUE_STRETCHY_VERTICAL);
        StretchOverride.STRETCHATTR.put("\u21c1", Mo.VALUE_STRETCHY_HORIZONTAL);
        StretchOverride.STRETCHATTR.put("\u21c2", Mo.VALUE_STRETCHY_VERTICAL);
        StretchOverride.STRETCHATTR.put("\u295a", Mo.VALUE_STRETCHY_HORIZONTAL);
        StretchOverride.STRETCHATTR.put("\u21c3", Mo.VALUE_STRETCHY_VERTICAL);
        StretchOverride.STRETCHATTR.put("\u295b", Mo.VALUE_STRETCHY_HORIZONTAL);
        StretchOverride.STRETCHATTR.put("\u2961", Mo.VALUE_STRETCHY_VERTICAL);
        StretchOverride.STRETCHATTR.put("\u2960", Mo.VALUE_STRETCHY_VERTICAL);
        StretchOverride.STRETCHATTR.put("\u296f", Mo.VALUE_STRETCHY_VERTICAL);
        StretchOverride.STRETCHATTR.put("\u296e", Mo.VALUE_STRETCHY_VERTICAL);
        StretchOverride.STRETCHATTR.put("\u21f5", Mo.VALUE_STRETCHY_VERTICAL);
        StretchOverride.STRETCHATTR.put("\u21e4", Mo.VALUE_STRETCHY_HORIZONTAL);
        StretchOverride.STRETCHATTR.put("\u21e5", Mo.VALUE_STRETCHY_HORIZONTAL);
        StretchOverride.STRETCHATTR.put("\u27f7", Mo.VALUE_STRETCHY_HORIZONTAL);
        StretchOverride.STRETCHATTR.put("\u27f6", Mo.VALUE_STRETCHY_HORIZONTAL);
        StretchOverride.STRETCHATTR.put("\u27f5", Mo.VALUE_STRETCHY_HORIZONTAL);
        StretchOverride.STRETCHATTR.put("\u23b4", Mo.VALUE_STRETCHY_HORIZONTAL);
        StretchOverride.STRETCHATTR.put("\u2192", Mo.VALUE_STRETCHY_HORIZONTAL);
        StretchOverride.STRETCHATTR.put("\u23b5", Mo.VALUE_STRETCHY_HORIZONTAL);
        StretchOverride.STRETCHATTR.put("\u27fa", Mo.VALUE_STRETCHY_HORIZONTAL);
        StretchOverride.STRETCHATTR.put("\u2190", Mo.VALUE_STRETCHY_HORIZONTAL);
        StretchOverride.STRETCHATTR.put("\u2193", Mo.VALUE_STRETCHY_VERTICAL);
        StretchOverride.STRETCHATTR.put("\u27f9", Mo.VALUE_STRETCHY_HORIZONTAL);
        StretchOverride.STRETCHATTR.put("\u27f8", Mo.VALUE_STRETCHY_HORIZONTAL);
        StretchOverride.STRETCHATTR.put("\u2195", Mo.VALUE_STRETCHY_VERTICAL);
        StretchOverride.STRETCHATTR.put("\u2194", Mo.VALUE_STRETCHY_HORIZONTAL);
        StretchOverride.STRETCHATTR.put("\u2912", Mo.VALUE_STRETCHY_VERTICAL);
        StretchOverride.STRETCHATTR.put("\u2913", Mo.VALUE_STRETCHY_VERTICAL);
        StretchOverride.STRETCHATTR.put("\u21bc", Mo.VALUE_STRETCHY_HORIZONTAL);
        StretchOverride.STRETCHATTR.put("\u21bf", Mo.VALUE_STRETCHY_VERTICAL);
        StretchOverride.STRETCHATTR.put("\u21be", Mo.VALUE_STRETCHY_VERTICAL);
        StretchOverride.STRETCHATTR.put("\u21bd", Mo.VALUE_STRETCHY_HORIZONTAL);
        StretchOverride.STRETCHATTR.put("\u21a6", Mo.VALUE_STRETCHY_HORIZONTAL);
        StretchOverride.STRETCHATTR.put("\u21a7", Mo.VALUE_STRETCHY_VERTICAL);
        StretchOverride.STRETCHATTR.put("\u21a4", Mo.VALUE_STRETCHY_HORIZONTAL);
        StretchOverride.STRETCHATTR.put("\u21a5", Mo.VALUE_STRETCHY_VERTICAL);
    }
}
