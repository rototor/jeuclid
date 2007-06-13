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
import java.net.URL;
import java.util.Locale;

import com.xmlmind.xmledit.doc.Element;
import com.xmlmind.xmledit.imagetoolkit.ImageConverter;
import com.xmlmind.xmledit.imagetoolkit.ImageRenderer;
import com.xmlmind.xmledit.imagetoolkit.ImageToolkit;

/**
 * Implements an ImageToolkit for XXE.
 * 
 * @author Max Berger
 * @version $Revision$
 */
public class JEuclidImageToolkit implements ImageToolkit {

// private static PrintStream DEBUG;
// static {
// try {
// JEuclidImageToolkit.DEBUG = new PrintStream("/tmp/Mylog", "UTF-8");
// } catch (final FileNotFoundException e) {
// } catch (final UnsupportedEncodingException e) {
// }
// }

    /**
     * Default constructor.
     */
    public JEuclidImageToolkit() {
        // Empty on purpose.
    }

    /** {@inheritDoc} */
    public String getDescription() {
        // JEuclidImageToolkit.DEBUG.println("getDescription");
        return "Support MathML content through the use of JEuclid.\n"
                + "Please see http://jeuclid.sf.net/ for more information.";
    }

    private boolean hasRightExtension(final String filename) {
        final String extension = filename.substring(
                filename.lastIndexOf('.') + 1).toLowerCase(Locale.ENGLISH);
        return "mml".equals(extension) || "odf".equals(extension);

    }

    /** {@inheritDoc} */
    public ImageConverter getImageConverter(final File inFile,
            final File outFile) {
        if (this.hasRightExtension(inFile.getName())) {
            return JEuclidImageConverter.getConverter();
        } else {
            return null;
        }
    }

    /** {@inheritDoc} */
    public ImageRenderer getImageRenderer(final URL url) {
        final String path = url.getPath();
        if (this.hasRightExtension(path)) {
            return JEuclidImageRenderer.getRenderer();
        } else {
            return null;
        }
    }

    /** {@inheritDoc} */
    public ImageRenderer getImageRenderer(final byte[] arg0) {
        // JEuclidImageToolkit.DEBUG.println("getImageRenderer/byte[]");
        return null;
    }

    /** {@inheritDoc} */
    public ImageRenderer getImageRenderer(final Element arg0) {
        // JEuclidImageToolkit.DEBUG.println("getImageRenderer/Element " +
        // arg0);
        return null;
    }

    /** {@inheritDoc} */
    public String getName() {
        // JEuclidImageToolkit.DEBUG.println("getName");
        return "JEuclid MathML image plugin";
    }

}
