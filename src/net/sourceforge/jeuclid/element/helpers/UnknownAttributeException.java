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

/* $Id: UnknownAttributeException.java,v 1.4 2006/08/07 18:27:47 maxberger Exp $ */

package net.sourceforge.jeuclid.element.helpers;

/**
 * Author: PG
 * Date: Feb 4, 2005
 * This is just a marker class.
 */
public class UnknownAttributeException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 9010017153628193810L;

    /**
     * Default constructor.
     *
     * @param wrongAttributeName name of attribute
     */
    public UnknownAttributeException(String wrongAttributeName) {
        super("Unknown attribute name: " + wrongAttributeName);
    }
}
