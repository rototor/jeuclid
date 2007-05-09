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

package net.sourceforge.jeuclid;

import java.io.File;

/**
 * Internal class for defensive programming.
 * <p>
 * Even though these methods are declared as public, there are not guaranteed
 * to be stable or work outside JEuclid.
 * <p>
 * http://en.wikipedia.org/wiki/Defensive_programming
 * 
 * @author putrycze
 * @version $Revision$
 * 
 */
public final class Defense {

    /** Default constructor. */
    private Defense() {
        // Empty on purpose
    }

    /**
     * Makes sure a parameter is not null.
     * 
     * @param o
     *            parameter
     * @param name
     *            name of the parameter
     */
    public static void notNull(final Object o, final String name) {
        if (o != null) {
            return;
        }
        throw new NullPointerException("'" + name + "' cannot be null");
    }

    /**
     * Makes sure a file exists.
     * 
     * @param file
     *            the file
     */
    public static void fileExists(final File file) {
        if (!file.exists()) {
            throw new AssertionError(file.toString() + " doesn't exist");
        }

    }

    /**
     * Makes sure a condition is true.
     * 
     * @param b
     *            condition
     * @param string
     *            value
     */
    public static void assertTrue(final boolean b, final String string) {
        if (!b) {
            throw new AssertionError("Condition failed:" + string);
        }
    }

}
