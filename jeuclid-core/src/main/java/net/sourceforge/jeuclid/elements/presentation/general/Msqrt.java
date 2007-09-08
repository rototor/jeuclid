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

import org.w3c.dom.mathml.MathMLElement;
import org.w3c.dom.mathml.MathMLRadicalElement;

/**
 * This class presents a mathematical square root.
 * 
 * @author Unknown
 * @author Max Berger
 * @version $Revision$
 */
public class Msqrt extends AbstractRoot implements MathMLRadicalElement {

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "msqrt";

    /**
     * Creates a math element.
     */
    public Msqrt() {
        super();
    }

    /** {@inheritDoc} */
    public String getTagName() {
        return Msqrt.ELEMENT;
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
            retVal = new Mrow();
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
