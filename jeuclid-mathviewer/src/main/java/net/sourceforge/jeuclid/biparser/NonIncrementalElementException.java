/*
 * Copyright 2010 - 2010 JEuclid, http://jeuclid.sf.net
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

/* $Id $ */

package net.sourceforge.jeuclid.biparser;

import org.xml.sax.SAXParseException;

/**
 * Exception to be thrown if the subtree contains elements which are not
 * incrementally updatable.
 * 
 * @version $Revision$
 */
public class NonIncrementalElementException extends SAXParseException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Create a new NonIncrementalElementException.
     * 
     * @param element
     *            element which was tried to be created.
     */
    public NonIncrementalElementException(final String element) {
        super(element, null);
    }
}
