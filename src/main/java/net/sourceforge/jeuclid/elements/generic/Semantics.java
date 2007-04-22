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

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.elements.presentation.general.AbstractRowLike;

import org.w3c.dom.mathml.MathMLElement;
import org.w3c.dom.mathml.MathMLSemanticsElement;

/**
 * This class represents a semantics element.
 * 
 * @author Max Berger
 * @version $Revision$
 */
public class Semantics extends AbstractRowLike implements
        MathMLSemanticsElement {

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "semantics";

    /**
     * Creates a math element.
     * 
     * @param base
     *            The base for the math element tree.
     */
    public Semantics(final MathBase base) {
        super(base);
    }

    /** {@inheritDoc} */
    public String getTagName() {
        return Semantics.ELEMENT;
    }

    /** {@inheritDoc} */
    public void deleteAnnotation(final int index) {
        this.removeAnnotation(index);
    }

    /** {@inheritDoc} */
    public MathMLElement getAnnotation(final int index) {
        // Index is 1-based!
        return (MathMLElement) this.getChildNodes().item(index);
    }

    /** {@inheritDoc} */
    public MathMLElement getBody() {
        return (MathMLElement) this.getFirstChild();
    }

    /** {@inheritDoc} */
    public int getNAnnotations() {
        return Math.max(0, this.getChildNodes().getLength() - 1);
    }

    /** {@inheritDoc} */
    public MathMLElement insertAnnotation(final MathMLElement newAnnotation,
            final int index) {
        if (index == 0) {
            if (this.getNAnnotations() == 0) {
                this.setAnnotation(newAnnotation, 1);
            } else {
                this.addMathElement(newAnnotation);
            }
        } else {
            final MathMLElement oldChild = this.getAnnotation(index);
            if (oldChild != null) {
                this.insertBefore(newAnnotation, oldChild);
            } else {
                this.setAnnotation(newAnnotation, index);
            }
        }
        return newAnnotation;
    }

    /** {@inheritDoc} */
    public MathMLElement removeAnnotation(final int index) {
        final MathMLElement oldChild = this.getAnnotation(index);
        return (MathMLElement) this.removeChild(oldChild);
    }

    /** {@inheritDoc} */
    public MathMLElement setAnnotation(final MathMLElement newAnnotation,
            final int index) {
        // Index is 1-based!
        this.setMathElement(index, newAnnotation);
        return newAnnotation;
    }

    /** {@inheritDoc} */
    public void setBody(final MathMLElement body) {
        this.setMathElement(0, body);
    }

}
