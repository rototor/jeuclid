/*
 * Copyright 2007 - 2009 JEuclid, http://jeuclid.sf.net
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

/**
 * Interface for all Image Conversion Detectors.
 * 
 * @version $Revision$
 */
public interface ConverterDetector {

    /**
     * Detects conversions available through this plugin and registers them
     * with the given registry.
     * <p>
     * The detector should only register the conversions which can be carried
     * out without Exceptions. The detection process itself should not throw
     * an exception either. If a detector cannot find the necessary classes,
     * it should register nothing.
     * 
     * @param registry
     *            ConverterRegistry to use.
     */
    void detectConversionPlugins(final ConverterRegistry registry);

}
