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

package net.sourceforge.jeuclid.converter;

import net.sourceforge.jeuclid.elements.support.ClassLoaderSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Detects if FreeHep is in the class path and registers it if its available.
 * 
 * @version $Revision$
 */
public final class FreeHepDetector implements ConverterDetector {
    /**
     * Logger for this class
     */
    private static final Log LOGGER = LogFactory
            .getLog(FreeHepDetector.class);

    /**
     * Default constructor.
     */
    public FreeHepDetector() {
        // Empty on purpose
    }

    /**
     * Detects if FreeHep is in the classpath.
     * 
     * @param registry
     *            ConverterRegisty to register with.
     */
    public void detectConversionPlugins(final ConverterRegistry registry) {

        try {
            ClassLoaderSupport.getInstance().loadClass(
                    "org.freehep.util.export.ExportFileType");
            FreeHepInternalDetector.actuallyDetectConversionPlugins(registry);
        } catch (final ClassNotFoundException e) {
            FreeHepDetector.LOGGER.debug(e);
        }
    }
}
