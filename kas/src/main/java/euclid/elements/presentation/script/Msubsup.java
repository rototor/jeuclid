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

/* $Id: Msubsup.java 750 2008-05-18 22:22:38Z maxberger $ */

package euclid.elements.presentation.script;


import org.w3c.dom.Node;
import org.w3c.dom.mathml.MathMLElement;

import euclid.elements.JEuclidElement;

/**
 * This class arranges a element lower, and a other elements upper to an
 * element.
 * 
 * @version $Revision: 750 $
 */
public final class Msubsup extends AbstractSubSuper {

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "msubsup";

    private static final long serialVersionUID = 1L;

    /**
     * Creates a math element.
     */
    public Msubsup() {
        super();
    }

    /** {@inheritDoc} */
    @Override
    protected Node newNode() {
        return new Msubsup();
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
        return this.getMathElement(2);
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
        this.setMathElement(2, superscript);
    }

}
