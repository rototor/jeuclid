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

package net.sourceforge.jeuclid.elements.presentation.script;

import net.sourceforge.jeuclid.elements.JEuclidElement;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.mathml.MathMLElement;

/**
 * This class arranges an element lower to an other element.
 * 
 * @version $Revision$
 */
public final class Msub extends AbstractSubSuper {

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "msub";

    private static final long serialVersionUID = 1L;

    /**
     * Default constructor. Sets MathML Namespace.
     * 
     * @param qname
     *            Qualified name.
     * @param odoc
     *            Owner Document.
     */
    public Msub(final String qname, final AbstractDocument odoc) {
        super(qname, odoc);
    }

    /** {@inheritDoc} */
    @Override
    protected Node newNode() {
        return new Msub(this.nodeName, this.ownerDocument);
    }

    /** {@inheritDoc} */
    @Override
    public JEuclidElement getBase() {
        return this.getMathElement(0);
    }

    /** {@inheritDoc} */
    @Override
    public JEuclidElement getSubscript() {
        return this.getMathElement(1);
    }

    /** {@inheritDoc} */
    @Override
    public JEuclidElement getSuperscript() {
        return null;
    }

    /** {@inheritDoc} */
    public void setBase(final MathMLElement base) {
        this.setMathElement(0, base);
    }

    /** {@inheritDoc} */
    public void setSubscript(final MathMLElement subscript) {
        this.setMathElement(1, subscript);
    }

    /** {@inheritDoc} */
    public void setSuperscript(final MathMLElement superscript) {
        throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR,
                "msub does not have superscript");
    }

}
