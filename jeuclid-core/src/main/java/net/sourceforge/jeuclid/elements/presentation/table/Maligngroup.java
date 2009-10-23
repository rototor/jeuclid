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

package net.sourceforge.jeuclid.elements.presentation.table;

import net.sourceforge.jeuclid.elements.AbstractInvisibleJEuclidElement;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.Node;
import org.w3c.dom.mathml.MathMLAlignGroupElement;

/**
 * This class represents the maligngroup tag.
 * 
 * @version $Revision$
 */

public final class Maligngroup extends AbstractInvisibleJEuclidElement
        implements MathMLAlignGroupElement {

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "maligngroup";

    /** The groupalign attribute. */
    public static final String ATTR_GROUPALIGN = "groupalign";

    private static final long serialVersionUID = 1L;

    /**
     * Default constructor. Sets MathML Namespace.
     * 
     * @param qname
     *            Qualified name.
     * @param odoc
     *            Owner Document.
     */
    public Maligngroup(final String qname, final AbstractDocument odoc) {
        super(qname, odoc);
    }

    /** {@inheritDoc} */
    @Override
    protected Node newNode() {
        return new Maligngroup(this.nodeName, this.ownerDocument);
    }

    /** {@inheritDoc} */
    public String getGroupalign() {
        return this.getMathAttribute(Maligngroup.ATTR_GROUPALIGN);
    }

    /** {@inheritDoc} */
    public void setGroupalign(final String groupalign) {
        this.setAttribute(Maligngroup.ATTR_GROUPALIGN, groupalign);
    }
}
