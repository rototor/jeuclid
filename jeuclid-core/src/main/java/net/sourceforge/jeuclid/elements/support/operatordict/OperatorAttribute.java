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

package net.sourceforge.jeuclid.elements.support.operatordict;

import java.util.Locale;

import net.sourceforge.jeuclid.Constants;

/**
 * @version $Revision$
 */
/**
 * @version $Revision$
 */
public enum OperatorAttribute {

    /** */
    FORM(OperatorDictionary.FORM_INFIX),
    /** */
    FENCE(Constants.FALSE),
    /** */
    SEPARATOR(Constants.FALSE),
    /** */
    LSPACE(OperatorDictionary.NAME_THICKMATHSPACE),
    /** */
    RSPACE(OperatorDictionary.NAME_THICKMATHSPACE),
    /** */
    STRETCHY(Constants.FALSE),
    /** */
    SYMMETRIC(Constants.TRUE),
    /** */
    MAXSIZE(OperatorDictionary.NAME_INFINITY),
    /** */
    MINSIZE("1"),
    /** */
    LARGEOP(Constants.FALSE),
    /** */
    MOVABLELIMITS(Constants.FALSE),
    /** */
    ACCENT(Constants.FALSE);

    private final String defaultValue;

    private OperatorAttribute(final String defValue) {
        this.defaultValue = defValue;
    }

    /**
     * @return the default value for this operator attribute.
     */
    public String getDefaultValue() {
        return this.defaultValue;
    }

    /**
     * Parses a String into an OperatorAttribute.
     * 
     * @param attr
     *            the String to parse
     * @return an operatorAttibute if possible
     * @throws UnknownAttributeException
     *             if the string does not represent a valid attribute.
     */
    public static OperatorAttribute parseOperatorAttribute(final String attr)
            throws UnknownAttributeException {
        try {
            return OperatorAttribute.valueOf(attr.toUpperCase(Locale.US));
        } catch (final IllegalArgumentException iae) {
            throw new UnknownAttributeException(attr, iae);
        }
    }

}
