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

package net.sourceforge.jeuclid.elements.generic;

import org.apache.batik.dom.AbstractDOMImplementation;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;

/**
 * @version $Revision$
 */
public final class JEuclidDOMImplementation extends AbstractDOMImplementation {

    private static final long serialVersionUID = 1L;

    private static final class SingletonHolder {
        private static final JEuclidDOMImplementation INSTANCE = new JEuclidDOMImplementation();

        private SingletonHolder() {
        }
    }

    /**
     * Default Constructor.
     */
    protected JEuclidDOMImplementation() {
        super();
    }

    /** {@inheritDoc} */
    public Document createDocument(final String namespaceURI,
            final String qualifiedName, final DocumentType doctype) {
        final Document result = new DocumentElement(doctype);
        result.appendChild(result.createElementNS(namespaceURI, qualifiedName));
        return result;
    }

    /** {@inheritDoc} */
    public DocumentType createDocumentType(final String qualifiedName,
            final String publicId, final String systemId) {
        throw new UnsupportedOperationException();
    }

    /**
     * Retrieve the singleton instance of this {@link DOMImplementation}.
     * 
     * @return a {@link DOMImplementation} implementing MathML DOM.
     */
    public static DOMImplementation getInstance() {
        return JEuclidDOMImplementation.SingletonHolder.INSTANCE;
    }
}
