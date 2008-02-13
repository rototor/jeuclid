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

package net.sourceforge.jeuclid.elements.support;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;

/**
 * @version $Revision$
 */
public class ClassLoaderSupport {

    private static ClassLoaderSupport instance;

    private final DOMImplementation domImpl;

    /**
     * Default Constructor.
     */
    private ClassLoaderSupport() {
        DOMImplementation impl;
        try {
            final DOMImplementationRegistry reg = DOMImplementationRegistry
                    .newInstance();
            impl = reg.getDOMImplementation("");
            if (impl == null) {
                // This is due to a bug in JDK 1.5 on the Mac: It seems like
                // the default DOMImplementations are not registered in the
                // DOMImplementationRegistry
                impl = (DOMImplementation) this
                        .loadClass(
                                "com.sun.org.apache.xerces.internal.dom.CoreDOMImplementationImpl")
                        .newInstance();
            }
        } catch (final ClassCastException e) {
            impl = null;
        } catch (final ClassNotFoundException e) {
            impl = null;
        } catch (final InstantiationException e) {
            impl = null;
        } catch (final IllegalAccessException e) {
            impl = null;
        }
        this.domImpl = impl;
    }

    public synchronized static ClassLoaderSupport getInstance() {
        if (ClassLoaderSupport.instance == null) {
            ClassLoaderSupport.instance = new ClassLoaderSupport();
        }
        return ClassLoaderSupport.instance;
    }

    public Class<?> loadClass(final String className)
            throws ClassNotFoundException {
        Class<?> retVal;
        try {
            retVal = Thread.currentThread().getContextClassLoader()
                    .loadClass(className);
        } catch (final ClassNotFoundException e) {
            retVal = ClassLoaderSupport.class.getClassLoader().loadClass(
                    className);
        }
        return retVal;
    }

    public DOMImplementation getGenericDOMImplementation() {
        return this.domImpl;
    }
}
