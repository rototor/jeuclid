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

package net.sourceforge.jeuclid.xxe;

import java.io.File;
import java.util.Locale;

import net.sourceforge.jeuclid.Converter;

import com.xmlmind.xmledit.imagetoolkit.ImageConverter;
import com.xmlmind.xmledit.util.Console;

/**
 * Implements an ImageConverter for XXE.
 * 
 * @author Max Berger
 * @version $Revision$
 */
public final class JEuclidImageConverter implements ImageConverter {

    private static JEuclidImageConverter converter;

    private JEuclidImageConverter() {
        // Empty on purpose
    }

    /** {@inheritDoc} */
    public void convertImage(final File inFile, final File outFile,
            final String[] parameters, final Console console)
            throws Exception {

        final String outFileName = outFile.getName();
        final String extension = outFileName.substring(
                outFileName.lastIndexOf('.') + 1).toLowerCase(Locale.ENGLISH);
        final String outMimeType = Converter.getMimeTypeForSuffix(extension);
        Converter.convert(inFile, outFile, outMimeType);
    }

    /**
     * Retrieve the singleton instance of this converter.
     * 
     * @return the ImageConverter.
     */
    public static ImageConverter getConverter() {
        if (JEuclidImageConverter.converter == null) {
            JEuclidImageConverter.converter = new JEuclidImageConverter();
        }
        return JEuclidImageConverter.converter;
    }

}
