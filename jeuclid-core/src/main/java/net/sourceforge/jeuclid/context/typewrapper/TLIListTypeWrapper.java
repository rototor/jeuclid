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

package net.sourceforge.jeuclid.context.typewrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * List is converted to String and back by using comma-separated
 * representation.
 * 
 * Strings are Stored Trimmed, Lower-cased, Interned.
 * 
 * @version $Revision$
 */
public final class TLIListTypeWrapper extends AbstractSimpleTypeWrapper {

    /**
     * separator to be used when converting to string or parsing string.
     */
    public static final String SEPARATOR = ",";

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final TypeWrapper INSTANCE = new TLIListTypeWrapper();

    /** Simple constructor. */
    private TLIListTypeWrapper() {
        super(List.class);
    }

    /**
     * @return the singleton instance.
     */
    public static TypeWrapper getInstance() {
        return TLIListTypeWrapper.INSTANCE;
    }

    /** {@inheritDoc} */
    @Override
    public Object fromString(final String value) {
        if (value == null) {
            return null;
        } else {
            final String whitespace = "\\s*";
            final String[] strList = value.split(whitespace
                    + TLIListTypeWrapper.SEPARATOR + whitespace);
            final List<String> retVal = new ArrayList<String>(strList.length);
            for (final String str : strList) {
                retVal.add(str.trim().toLowerCase(Locale.ENGLISH).intern());
            }
            return retVal;
        }
    }

    /** {@inheritDoc} */
    @Override
    public String toString(final Object value) {
        if (value == null) {
            return null;
        } else {
            final StringBuilder b = new StringBuilder();
            boolean first = true;
            for (final Object o : (List<?>) value) {
                if (first) {
                    first = false;
                } else {
                    b.append(TLIListTypeWrapper.SEPARATOR);
                    b.append(' ');
                }
                b.append(o);
            }
            return b.toString();
        }
    }
}
