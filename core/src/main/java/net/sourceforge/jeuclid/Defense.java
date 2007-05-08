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

/* $Id: JMathComponent.java 172 2007-05-05 13:30:28Z maxberger $ */

package net.sourceforge.jeuclid;

import java.io.File;

/**
 * Internal class for defensive programming.
 * http://en.wikipedia.org/wiki/Defensive_programming
 * @author putrycze
 * @version $Revision: 172 $
 *
 */
public class Defense {

    /**
     * Makes sure a parameter is not null.
     * @param o parameter
     * @param name name of the parameter
     */
	public static final void NotNull(Object o, String name) {
		if (o != null)
			return;
		throw new NullPointerException("'" + name + "' cannot be null");
	}
	
	

	/**
     * Makes sure a file exists.
	 * @param file the file
	 */
	public static void FileExists(File file) {
		if (!file.exists())
			throw new AssertionError(file.toString() + " doesn't exist");
		
	}



	/**
     * Makes sure a condition is true.
	 * @param b condition
	 * @param string value
	 */
	public static void assertTrue(final boolean b, final String string) {
		if (!b)
			throw new AssertionError("Condition failed:" + string);		
	}
	
}
