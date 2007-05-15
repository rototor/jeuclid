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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * A registry for image converters.
 * <p>
 * Please note: THIS API IS NOT TO BE CONSIDERED STABLE! IT IS STILL
 * EXPERIMENTAL.
 * 
 * @author Max Berger
 * @version $Revision$
 */
public final class ConverterRegistry {

    private static ConverterRegistry converterRegisty;

    private final Map<String, ConverterPlugin> mimetype2converter = new HashMap<String, ConverterPlugin>();

    private final Map<String, String> mimetype2suffix = new HashMap<String, String>();

    private final Map<String, String> suffix2mimetype = new HashMap<String, String>();

    private ConverterRegistry() {
    }

    /**
     * Retrieve the default registry instance.
     * 
     * @return the ConverterRegistry.
     */
    public static ConverterRegistry getRegisty() {
        if (ConverterRegistry.converterRegisty == null) {
            ConverterRegistry.converterRegisty = new ConverterRegistry();
            ImageIODetector
                    .detectConversionPlugins(ConverterRegistry.converterRegisty);
            BatikDetector
                    .detectConversionPlugins(ConverterRegistry.converterRegisty);
        }
        return ConverterRegistry.converterRegisty;
    }

    /**
     * Retrieve a list of available mime types for conversion.
     * 
     * @return a Set&lt;String&gt; containing all valid mime-types.
     */
    public Set<String> getAvailableOutfileTypes() {
        return this.mimetype2converter.keySet();
    }

    /**
     * Returns the file suffix suitable for the given mime type.
     * <p>
     * This function is not fully implemented yet
     * 
     * @param mimeType
     *            a mimetype, as returned by
     *            {@link #getAvailableOutfileTypes()}, or null if unknown.
     * @return the three letter suffix common for this type.
     */
    public String getSuffixForMimeType(final String mimeType) {
        return this.mimetype2suffix.get(mimeType.toLowerCase(Locale.ENGLISH));
    }

    /**
     * Returns the MimeType for a given suffix.
     * 
     * @param suffix
     *            the suffix, e.g. png, or null if unknown.
     * @return the mime-type
     */
    public String getMimeTypeForSuffix(final String suffix) {
        return this.suffix2mimetype.get(suffix.toLowerCase(Locale.ENGLISH));
    }

    /**
     * Registers a new MimeType and it's suffix.
     * 
     * @param mimeType
     *            the Mime-Type
     * @param suffix
     *            The Suffix
     * @param primary
     *            If true, old mappings will be overwritten. If false and a
     *            mapping already exists, it will stay the same.
     */
    public void registerMimeTypeAndSuffix(final String mimeType,
            final String suffix, final boolean primary) {

        final String lMimeType = mimeType.toLowerCase(Locale.ENGLISH);
        final String lSuffix = suffix.toLowerCase(Locale.ENGLISH);

        if (primary || !this.suffix2mimetype.containsKey(lSuffix)) {
            this.suffix2mimetype.put(lSuffix, lMimeType);
        }
        if (primary || !this.mimetype2suffix.containsKey(lMimeType)) {
            this.mimetype2suffix.put(lMimeType, lSuffix);
        }
    }

    /**
     * Registers a converter for the given mime type.
     * 
     * @param mimeType
     *            The mime type.
     * @param converter
     *            The converter to register.
     * @param primary
     *            Converter for this type. If true, old mappings will be
     *            overwritten. If false and a mapping already exists, it will
     *            stay the same.
     */
    public void registerConverter(final String mimeType,
            final ConverterPlugin converter, final boolean primary) {
        if (primary || !this.mimetype2converter.containsKey(mimeType)) {
            this.mimetype2converter.put(mimeType, converter);
        }
    }

    /**
     * Retrieve the converter for a given mime-type.
     * 
     * @param mimeType
     *            the Mime-Type.
     * @return a Converter instance
     */
    public ConverterPlugin getConverter(final String mimeType) {
        return this.mimetype2converter.get(mimeType
                .toLowerCase(Locale.ENGLISH));
    }
}
