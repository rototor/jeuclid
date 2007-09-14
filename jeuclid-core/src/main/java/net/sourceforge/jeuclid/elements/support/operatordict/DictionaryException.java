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

/**
 * Exception during loading from dictionary.
 * 
 * @version $Revision$
 */
public class DictionaryException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 7722130827768612438L;

    /**
     * Default constructor.
     * 
     * @param message
     *            Error message
     */
    public DictionaryException(final String message) {
        super(message);
    }

    /**
     * Error based on another cause.
     * 
     * @param message
     *            Error Message
     * @param cause
     *            The real Cause
     */
    public DictionaryException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
