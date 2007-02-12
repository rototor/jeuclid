/*
 * Copyright 2002 - 2006 JEuclid, http://jeuclid.sf.net
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

/* $Id: AbstractMathContainer.java,v 1.1.2.1 2006/09/14 19:56:40 maxberger Exp $ */

package net.sourceforge.jeuclid.element.generic;

import net.sourceforge.jeuclid.MathBase;

import org.w3c.dom.DOMException;
import org.w3c.dom.mathml.MathMLContainer;
import org.w3c.dom.mathml.MathMLDeclareElement;
import org.w3c.dom.mathml.MathMLElement;
import org.w3c.dom.mathml.MathMLNodeList;

public abstract class AbstractMathContainer extends AbstractMathElement
        implements MathMLContainer {

    public AbstractMathContainer(MathBase base) {
        super(base);
    }

    /** {@inheritDoc} */
    public int getNArguments() {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public MathMLNodeList getArguments() {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public MathMLNodeList getDeclarations() {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public MathMLElement getArgument(int index) throws DOMException {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public MathMLElement setArgument(MathMLElement newArgument, int index)
            throws DOMException {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public MathMLElement insertArgument(MathMLElement newArgument, int index)
            throws DOMException {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public void deleteArgument(int index) throws DOMException {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public MathMLElement removeArgument(int index) throws DOMException {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public MathMLDeclareElement getDeclaration(int index) throws DOMException {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public MathMLDeclareElement setDeclaration(
            MathMLDeclareElement newDeclaration, int index) throws DOMException {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public MathMLDeclareElement insertDeclaration(
            MathMLDeclareElement newDeclaration, int index) throws DOMException {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public MathMLDeclareElement removeDeclaration(int index)
            throws DOMException {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public void deleteDeclaration(int index) throws DOMException {
        throw new UnsupportedOperationException();
    }
}
