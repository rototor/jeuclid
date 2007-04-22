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

/* $Id: MathOver.java 132 2007-04-18 07:55:39Z maxberger $ */

package net.sourceforge.jeuclid.elements.presentation.script;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.elements.JEuclidElement;

import org.w3c.dom.DOMException;
import org.w3c.dom.mathml.MathMLElement;

/**
 * This class arranges a element over an other element.
 * 
 * @author Unknown
 * @author Max Berger
 * @version $Revision: 108 $
 */
public class Mover extends AbstractUnderOver {

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "mover";

    /**
     * Creates a math element.
     * 
     * @param base
     *            The base for the math element tree.
     */
    public Mover(final MathBase base) {
        super(base);
    }

    /** {@inheritDoc} */
    public String getTagName() {
        return Mover.ELEMENT;
    }

    /** {@inheritDoc} */
    public JEuclidElement getOverscript() {
        return this.getMathElement(1);
    }

    /** {@inheritDoc} */
    public JEuclidElement getUnderscript() {
        return null;
    }

    /** {@inheritDoc} */
    public void setOverscript(final MathMLElement overscript) {
        this.setMathElement(1, overscript);
    }

    /** {@inheritDoc} */
    public void setUnderscript(final MathMLElement underscript) {
        throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR,
                "mover does not have underscript");
    }

}
