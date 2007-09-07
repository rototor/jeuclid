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

import java.awt.Dimension;
import java.io.IOException;
import java.io.OutputStream;

import net.sourceforge.jeuclid.LayoutContext;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Describes an Image converter.
 * 
 * @author Max Berger
 * @version $Revision$
 */
public interface ConverterPlugin {
    /**
     * Write the given MathBase object with its rendering parameters into the
     * given output stream.
     * 
     * @param doc
     *            A JEuclid DocumentElement
     * @param outStream
     *            Target output stream.
     * @param context
     *            LayoutContext to use.
     * @return Rendering's dimension based on the spefic plugin's Graphics2D
     *         implementation.
     * @throws IOException
     *             if an I/O error occurred during write.
     */
    Dimension convert(Node doc, LayoutContext context, OutputStream outStream)
            throws IOException;

    /**
     * Convert from the given Math Object to an XML DOM Document.
     * 
     * @param doc
     *            A JEuclid DocumentElement
     * @param context
     *            LayoutContext to use.
     * @return an instance of Document, or the appropriate subtype for this
     *         format (e.g. SVGDocument). If conversion is not supported by
     *         this plugin, it may return null.
     */
    Document convert(final Node doc, final LayoutContext context);

}
