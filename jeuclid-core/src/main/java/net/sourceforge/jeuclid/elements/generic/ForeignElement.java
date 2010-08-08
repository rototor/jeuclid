/*
 * Copyright 2002 - 2009 JEuclid, http://jeuclid.sf.net
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

import net.sourceforge.jeuclid.elements.AbstractJEuclidElement;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.Node;

/**
 * This class represents a foreign element. It is rendered like a row.
 * 
 * @version $Revision$
 */
public final class ForeignElement extends AbstractJEuclidElement {

    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     * 
     * @param nsUri
     *            Namespace URI.
     * @param qname
     *            Qualified name.
     * @param odoc
     *            Owner Document.
     */
    public ForeignElement(final String nsUri, final String qname,
            final AbstractDocument odoc) {
        super(nsUri, qname, odoc);
    }

    /** {@inheritDoc} */
    @Override
    protected Node newNode() {
        return new ForeignElement(this.namespaceURI, this.nodeName,
                this.ownerDocument);
    }

}
