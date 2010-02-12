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

package net.sourceforge.jeuclid.elements.support.operatordict;

import java.util.Locale;

/**
 * @version $Revision$
 */
public enum OperatorForm {
    /** Prefix form, e.g. +a. */
    PREFIX,
    /** Infix form, e.g. a+a. */
    INFIX,
    /** Postfix form, e.g. a+. */
    POSTFIX;

    private OperatorForm() {
        // Empty on purpose.
    }

    /**
     * Parse a String into an OperatorForm.
     * 
     * @param form
     *            the string to parse
     * @return an OperatorForm
     */
    public static OperatorForm parseOperatorForm(final String form) {
        try {
            return OperatorForm.valueOf(form.toUpperCase(Locale.US));
        } catch (final IllegalArgumentException iae) {
            return OperatorForm.INFIX;
        }
    }
}
