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

/* $Id: ClassLoaderSupport.java 783 2008-06-07 14:12:27Z maxberger $ */

package euclid.elements.support;

/**
 * Support utilities for classloading.
 * 
 * @version $Revision: 783 $
 */
public final class ClassLoaderSupport {

    private static final class SingletonHolder {
        private static final ClassLoaderSupport INSTANCE = new ClassLoaderSupport();

        private SingletonHolder() {
        }
    }

    /**
     * Default Constructor.
     */
    protected ClassLoaderSupport() {
        // Empty on purporse.
    }

    /**
     * accessor for singleton instance.
     * 
     * @return an instance of this class.
     */
    public static ClassLoaderSupport getInstance() {
        return ClassLoaderSupport.SingletonHolder.INSTANCE;
    }

    /**
     * Try to load the given class from the current context.
     * 
     * @param className
     *            name of the class to load
     * @return a Class object for the given class if possible.
     * @throws ClassNotFoundException
     *             if the class could not be found.
     * @see ClassLoader#loadClass(String)
     */
    public Class<?> loadClass(final String className)
            throws ClassNotFoundException {
        Class<?> retVal;
        try {
        	System.out.println("Loading UI from Thread"); 
            retVal = Thread.currentThread().getContextClassLoader().loadClass(
                    className);
        } catch (final ClassNotFoundException e) {
        	System.out.println("Loading UI from Class"); 
            retVal = ClassLoaderSupport.class.getClassLoader().loadClass(
                    className);
        }
        return retVal;
    }

}
