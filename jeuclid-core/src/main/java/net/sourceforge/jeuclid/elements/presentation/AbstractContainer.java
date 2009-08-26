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

/* $Id$ */

package net.sourceforge.jeuclid.elements.presentation;

import net.sourceforge.jeuclid.elements.AbstractJEuclidElement;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.mathml.MathMLDeclareElement;
import org.w3c.dom.mathml.MathMLElement;
import org.w3c.dom.mathml.MathMLNodeList;
import org.w3c.dom.mathml.MathMLPresentationContainer;

/**
 * Abstract implementation for all classes that provide support for the DOM
 * MathMLContainer interface.
 * 
 * @version $Revision$
 */
public abstract class AbstractContainer extends AbstractJEuclidElement
        implements MathMLPresentationContainer {

    /**
     * Default constructor. Sets MathML Namespace.
     * 
     * @param qname
     *            Qualified name.
     * @param odoc
     *            Owner Document.
     */
    public AbstractContainer(final String qname, final AbstractDocument odoc) {
        super(qname, odoc);
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
    public MathMLElement getArgument(final int index) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public MathMLElement setArgument(final MathMLElement newArgument,
            final int index) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public MathMLElement insertArgument(final MathMLElement newArgument,
            final int index) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public void deleteArgument(final int index) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public MathMLElement removeArgument(final int index) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public MathMLDeclareElement getDeclaration(final int index) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public MathMLDeclareElement setDeclaration(
            final MathMLDeclareElement newDeclaration, final int index) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public MathMLDeclareElement insertDeclaration(
            final MathMLDeclareElement newDeclaration, final int index) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public MathMLDeclareElement removeDeclaration(final int index) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public void deleteDeclaration(final int index) {
        throw new UnsupportedOperationException();
    }
}
