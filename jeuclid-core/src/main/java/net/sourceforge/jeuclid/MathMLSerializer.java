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

package net.sourceforge.jeuclid;

import java.io.StringWriter;

import javax.annotation.concurrent.ThreadSafe;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Node;

/**
 * Utility class to serialize DOM documents back into Strings.
 * <p>
 * This class can be used to generate String representations for an existing DOM
 * Tree. The functionality is not restricted to JEuclid, and can be used for
 * other DOM trees as well. In this case, you should set the addDoctype
 * parameter to false.
 * 
 * @version $Revision$
 */
@ThreadSafe
public final class MathMLSerializer {

    /**
     * Logger for this class
     */
    private static final Log LOGGER = LogFactory.getLog(MathMLSerializer.class);

    private MathMLSerializer() {
        // empty on purpose
    }

    /**
     * Serialize a document back into a String.
     * 
     * @param doc
     *            a DOM model of a document, or a node in a document
     * @param addDoctype
     *            if true, extra attributes such as docType will be set. This
     *            ensures maximum MathML compatibility. Use only with MathML DOM
     *            trees.
     * @param format
     *            if true, result will be nicely formatted.
     * @return the document serialized to a string
     * @see #serializeDocument(Node, boolean, boolean, boolean)
     */
    public static String serializeDocument(final Node doc,
            final boolean addDoctype, final boolean format) {
        return MathMLSerializer.serializeDocument(doc, addDoctype, format,
                false);
    }

    /**
     * Serialize a document back into a String.
     * 
     * @param doc
     *            a DOM model of a document.
     * @param addDoctype
     *            if true, extra attributes such as docType will be set. This
     *            ensures maximum MathML compatibility. Use only with MathML DOM
     *            trees.
     * @param format
     *            if true, result will be nicely formatted.
     * @param omitXMLDecl
     *            if true, there will be no XML declaration.
     * @return the document serialized to a string
     */
    public static String serializeDocument(final Node doc,
            final boolean addDoctype, final boolean format,
            final boolean omitXMLDecl) {
        final StringWriter writer = new StringWriter();
        try {
            final Transformer transformer = TransformerFactory.newInstance()
                    .newTransformer();
            final DOMSource source = new DOMSource(doc);
            final StreamResult result = new StreamResult(writer);

            if (addDoctype) {
                transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC,
                        ResourceEntityResolver.MML2_PUBLICID);
                transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,
                        ResourceEntityResolver.MML2_SYSTEMID);
                transformer.setOutputProperty(OutputKeys.MEDIA_TYPE,
                        Constants.MATHML_MIMETYPE);
            }
            MathMLSerializer.boolToProperty(format, OutputKeys.INDENT,
                    transformer);
            MathMLSerializer.boolToProperty(omitXMLDecl,
                    OutputKeys.OMIT_XML_DECLARATION, transformer);
            transformer.transform(source, result);
        } catch (final TransformerException e) {
            MathMLSerializer.LOGGER.warn(e.getMessage(), e);
        }
        return writer.toString();

    }

    private static void boolToProperty(final boolean bool, final String key,
            final Transformer transformer) {
        if (bool) {
            transformer.setOutputProperty(key, "yes");
        } else {
            transformer.setOutputProperty(key, "no");
        }
    }

}
