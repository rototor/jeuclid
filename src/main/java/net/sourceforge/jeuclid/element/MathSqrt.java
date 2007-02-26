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

package net.sourceforge.jeuclid.element;

import java.util.List;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.element.generic.AbstractRootElement;
import net.sourceforge.jeuclid.element.generic.MathElement;
import net.sourceforge.jeuclid.element.helpers.ElementListSupport;

import org.w3c.dom.mathml.MathMLElement;
import org.w3c.dom.mathml.MathMLRadicalElement;

/**
 * This class presents a mathematical square root.
 * 
 * @author Unknown
 * @author Max Berger
 */
public class MathSqrt extends AbstractRootElement implements
        MathMLRadicalElement {

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "msqrt";

    /**
     * Creates a math element.
     * 
     * @param base
     *            The base for the math element tree.
     */
    public MathSqrt(final MathBase base) {
        super(base);
    }

    /** {@inheritDoc} */
    public String getTagName() {
        return MathSqrt.ELEMENT;
    }

    /** {@inheritDoc} */
    @Override
    protected List<MathElement> getContent() {
        return ElementListSupport.createListOfChildren(this);
    }

    /** {@inheritDoc} */
    @Override
    protected MathElement getActualIndex() {
        return new MathSpace(this.getMathBase());
    }

    /** {@inheritDoc} */
    public MathMLElement getIndex() {
        return null;
    }

    /** {@inheritDoc} */
    public MathMLElement getRadicand() {
        MathElement retVal;
        if (this.getMathElementCount() == 1) {
            retVal = this.getMathElement(0);
        } else {
            retVal = new MathRow(this.getMathBase());
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
