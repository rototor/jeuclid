/*
 * Copyright 2002 - 2006 JEuclid, http://jeuclid.sf.net
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
 * This is just a marker class.
 * 
 * @version $Revision$
 */
public class UnknownAttributeException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 9010017153628193810L;

    /**
     * Default constructor.
     * 
     * @param wrongAttributeName
     *            name of attribute
     * @param cause
     *            original Exception
     */
    public UnknownAttributeException(final String wrongAttributeName,
            final Exception cause) {
        super("Unknown attribute name: " + wrongAttributeName, cause);
    }
}
