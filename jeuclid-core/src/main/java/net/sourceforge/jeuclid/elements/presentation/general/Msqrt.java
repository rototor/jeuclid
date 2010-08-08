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

package net.sourceforge.jeuclid.elements.presentation.general;

import java.util.List;

import net.sourceforge.jeuclid.elements.JEuclidElement;
import net.sourceforge.jeuclid.layout.LayoutableNode;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.Node;
import org.w3c.dom.mathml.MathMLElement;

/**
 * This class presents a mathematical square root.
 * 
 * @version $Revision$
 */
public final class Msqrt extends AbstractRoot {

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "msqrt";

    private static final long serialVersionUID = 1L;

    /**
     * Default constructor. Sets MathML Namespace.
     * 
     * @param qname
     *            Qualified name.
     * @param odoc
     *            Owner Document.
     */
    public Msqrt(final String qname, final AbstractDocument odoc) {
        super(qname, odoc);
    }

    /** {@inheritDoc} */
    @Override
    protected Node newNode() {
        return new Msqrt(this.nodeName, this.ownerDocument);
    }

    /** {@inheritDoc} */
    @Override
    protected List<LayoutableNode> getContent() {
        return this.getChildrenToLayout();
    }

    /** {@inheritDoc} */
    public MathMLElement getIndex() {
        return null;
    }

    /** {@inheritDoc} */
    public MathMLElement getRadicand() {
        JEuclidElement retVal;
        if (this.getMathElementCount() == 1) {
            retVal = this.getMathElement(0);
        } else {
            retVal = new Mrow(Mrow.ELEMENT, this.ownerDocument);
            retVal.setFakeParent(this);
            for (int i = 0; i < this.getMathElementCount(); i++) {
                retVal.appendChild(this.getMathElement(i));
            }
        }
        return retVal;
    }

    /** {@inheritDoc} */
    public void setIndex(final MathMLElement index) {
        // Do nothing. There is no index for sqrt elements.
    }

    /** {@inheritDoc} */
    public void setRadicand(final MathMLElement radicand) {
        while (this.getMathElementCount() > 0) {
            this.removeChild(this.getMathElement(0));
        }
        this.setMathElement(0, radicand);
    }
}
