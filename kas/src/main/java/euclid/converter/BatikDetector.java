/*
 * Copyright 2007 - 2007 JEuclid, http://jeuclid.sf.net
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
/* reduziert von Kuenzel */

package euclid.converter;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.DOMImplementation;

import euclid.elements.generic.JEuclidDOMImplementation;
import euclid.elements.support.ClassLoaderSupport;

/**
 * Detects if Batik is in the class path and registers it if its available.
 * 
 * @version $Revision$
 */
public final class BatikDetector {
    /**
     * Logger for this class
     */
    private static final Log LOGGER = LogFactory.getLog(BatikDetector.class);

    private BatikDetector() {
        // Empty on purpose
    }
    // Von Kü
    private static DOMImplementation findSVGDOMImplementation() {
    	return JEuclidDOMImplementation.getInstance();

    }
//    private static DOMImplementation findSVGDOMImplementation() {
//    	return SVGDOMImplementation.getDOMImplementation();
//
//    }
    //original
//    private static DOMImplementation findSVGDOMImplementation() {
//        DOMImplementation impl;
//        try {
//        	
//
//            final Class<?> svgdomimpl = ClassLoaderSupport.getInstance()
//                    .loadClass(
//                            "org.apache.batik.dom.svg.SVGDOMImplementation");
//            final Method getDOMimpl = svgdomimpl.getMethod(
//                    "getDOMImplementation", new Class<?>[] {});
//            impl = (DOMImplementation) getDOMimpl.invoke(null,
//                    (Object[]) null);
//        } catch (final RuntimeException e) {
//            impl = null;
//        } catch (final LinkageError e) {
//            impl = null;
//        } catch (final ClassNotFoundException e) {
//            impl = null;
//        } catch (final NoSuchMethodException e) {
//            impl = null;
//        } catch (final IllegalAccessException e) {
//            impl = null;
//        } catch (final InvocationTargetException e) {
//            impl = null;
//        }
//        if (impl == null) {
//            impl = JEuclidDOMImplementation.getInstance();
//        }
//        return impl;
//    }

    /**
     * Detects if Batik is in the classpath.
     * 
     * @param registry
     *            ConverterRegisty to register with.
     */
    public static void detectConversionPlugins(
            final ConverterRegistry registry) {
        try {
            ClassLoaderSupport.getInstance().loadClass(
                    "org.apache.batik.svggen.SVGGraphics2D");
            BatikDetector.LOGGER.debug("Batik detected!");
            registry
                    .registerMimeTypeAndSuffix(
                            euclid.converter.Converter.TYPE_SVG,
                            euclid.converter.Converter.EXTENSION_SVG,
                            true);
            final DOMImplementation impl = BatikDetector
                    .findSVGDOMImplementation();
            if (impl != null) {
                registry.registerConverter(
                        euclid.converter.Converter.TYPE_SVG,
                        new BatikConverter(impl), true);
            }
        } catch (final ClassNotFoundException e) {
            BatikDetector.LOGGER.debug("Batik is not in classpath!");
        }
    }

}
