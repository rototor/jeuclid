/*
 * Copyright 2002 - 2008 JEuclid, http://jeuclid.sf.net
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

/**
 * Interface for operator dictionaries.
 * 
 * @version $Revision$
 */
public interface OperatorDictionary {

    /**
     * name for VERYVERYTHINMATHSPACE size of math space.
     */
    String NAME_VERYVERYTHINMATHSPACE = "veryverythinmathspace";

    /**
     * name for VERYTHINMATHSPACE size of math space.
     */
    String NAME_VERYTHINMATHSPACE = "verythinmathspace";

    /**
     * name for THINMATHSPACE size of math space.
     */
    String NAME_THINMATHSPACE = "thinmathspace";

    /**
     * name for MEDIUMMATHSPACE size of math space.
     */
    String NAME_MEDIUMMATHSPACE = "mediummathspace";

    /**
     * name for THICKMATHSPACE size of math space.
     */
    String NAME_THICKMATHSPACE = "thickmathspace";

    /**
     * name for VERYTHICKMATHSPACE size of math space.
     */
    String NAME_VERYTHICKMATHSPACE = "verythickmathspace";

    /**
     * name for VERYVERYTHICKMATHSPACE size of math space.
     */
    String NAME_VERYVERYTHICKMATHSPACE = "veryverythickmathspace";

    /**
     * name for INFINITY size of math space.
     */
    String NAME_INFINITY = "infinity";

    /** Form value for prefix. */
    String FORM_PREFIX = "prefix";

    /** form value for infix. */
    String FORM_INFIX = "infix";

    /** form value for postfix. */
    String FORM_POSTFIX = "postfix";

    /**
     * This value is returned, when default value of operator attribute doesn't
     * exist in this dictionary so far.
     */
    String VALUE_UNKNOWN = "NULL";

    /**
     * Determines default value of the operator attribute.
     * 
     * @param operator
     *            operator character
     * @param form
     *            form string
     * @param attributeName
     *            name of attribute
     * @return VALUE_UNKOWN or value from dict.
     * @throws UnknownAttributeException
     *             Raised, if wrong attributeName was provided.
     */
    String getDefaultAttributeValue(final String operator, final String form,
            final String attributeName) throws UnknownAttributeException;

}
