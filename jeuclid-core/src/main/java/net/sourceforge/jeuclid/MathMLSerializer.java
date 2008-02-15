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
 * 
 * @version $Revision$
 */
public final class MathMLSerializer {

    /**
     * Logger for this class
     */
    private static final Log LOGGER = LogFactory
            .getLog(MathMLSerializer.class);

    private MathMLSerializer() {
        // empty on purpose
    }

    /**
     * Serialize a document back into a String.
     * 
     * @param doc
     *            a DOM model of a document.
     * @param addDoctype
     *            if true, extra attributes such as docType will be set. This
     *            ensures maximum MathML compatibility
     * @param format
     *            if true, result will be nicely formatted.
     * @return the document serialized as a string
     */
    public static String serializeDocument(final Node doc,
            final boolean addDoctype, final boolean format) {
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
            if (format) {
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            } else {
                transformer.setOutputProperty(OutputKeys.INDENT, "no");
            }
            transformer.transform(source, result);
        } catch (final TransformerException e) {
            MathMLSerializer.LOGGER.warn(e);
        }
        return writer.toString();
    }
}
