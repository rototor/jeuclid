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

import java.io.Serializable;

/**
 * Encapsulates information about a parameter's value type and how values
 * should be converted between strings and the appropriate object instances.
 * <p>
 * This allows elimination of an additional "evil" if-elseif...else" pattern.
 * 
 * @version $Revision$
 */
public interface TypeWrapper extends Serializable {

    /** Error message for failed to convert 1/2. */
    String FAILED_TO_CONVERT = "Failed to convert <";

    /** Error message for failed to convert 2/2. */
    String TO = "> to ";

    /**
     * @return the class instance being wrapped
     */
    Class<?> getValueType();

    /**
     * Checks if the object is of a valid type for this type info.
     * 
     * @param o
     *            the object to check
     * @return true if the parameter can be set.
     */
    boolean valid(Object o);

    /**
     * Attempts to convert a parameter value expressed as string into an
     * instance of the appropriate (for this parameter) type.
     * 
     * @param value
     *            parameter value as string
     * @return parameter value as an instance of the proper type
     */
    Object fromString(String value);

    /**
     * Attempts to convert a parameter value expressed as an object of the
     * appropriate (for this parameter) type into a string representation.
     * 
     * @param value
     *            parameter value as object
     * @return parameter value as string
     */
    String toString(Object value);
}
