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

import org.w3c.dom.DOMException;
import org.w3c.dom.mathml.MathMLElement;
import org.w3c.dom.mathml.MathMLScriptElement;

/**
 * This class arranges an element lower to an other element.
 * 
 * @author Unknown
 * @author Max Berger
 * @version $Revision$
 */
public class Msup extends AbstractSubSuper implements MathMLScriptElement {

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "msup";

    /**
     * Creates a math element.
     */
    public Msup() {
        super();
    }

    // /**
    // * Paints this element.
    // *
    // * @param g
    // * The graphics context to use for painting.
    // * @param posX
    // * The first left position for painting.
    // * @param posY
    // * The position of the baseline.
    // */
    // @Override
    // public void paint(final Graphics2D g, final float posX, final float
    // posY) {
    // super.paint(g, posX, posY);
    // final JEuclidElement e1 = this.getMathElement(0);
    // final JEuclidElement e2 = this.getMathElement(1);
    //
    // final float posY2 = posY - this.getSuperBaseLineShift(g);
    //
    // e1.paint(g, posX, posY);
    // e2.paint(g, posX + e1.getWidth(g), posY2);
    // }
    //
    // /** {@inheritDoc} */
    // @Override
    // public float calculateWidth(final Graphics2D g) {
    // return this.getMathElement(0).getWidth(g)
    // + this.getMathElement(1).getWidth(g);
    // }
    //
    // /** {@inheritDoc} */
    // @Override
    // public float calculateAscentHeight(final Graphics2D g) {
    // return this.caclulateAscentHeightWithSuper(g);
    // }
    //
    // /** {@inheritDoc} */
    // @Override
    // public float calculateDescentHeight(final Graphics2D g) {
    // return this.getMathElement(0).getDescentHeight(g);
    // }

    /** {@inheritDoc} */
    public String getTagName() {
        return Msup.ELEMENT;
    }

    /** {@inheritDoc} */
    @Override
    public JEuclidElement getBase() {
        return this.getMathElement(0);
    }

    /** {@inheritDoc} */
    @Override
    public JEuclidElement getSubscript() {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public JEuclidElement getSuperscript() {
        return this.getMathElement(1);
    }

    /** {@inheritDoc} */
    public void setBase(final MathMLElement base) {
        this.setMathElement(0, base);
    }

    /** {@inheritDoc} */
    public void setSubscript(final MathMLElement subscript) {
        throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR,
                "msup does not have subscript");
    }

    /** {@inheritDoc} */
    public void setSuperscript(final MathMLElement superscript) {
        this.setMathElement(1, superscript);
    }

}
