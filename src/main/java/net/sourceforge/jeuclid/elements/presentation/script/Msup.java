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

/* $Id: MathSup.java 136 2007-04-19 13:58:28Z maxberger $ */

package net.sourceforge.jeuclid.elements.presentation.script;

import java.awt.Graphics2D;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.elements.JEuclidElement;

import org.w3c.dom.DOMException;
import org.w3c.dom.mathml.MathMLElement;
import org.w3c.dom.mathml.MathMLScriptElement;

/**
 * This class arrange an element lower to an other element.
 * 
 * @author Unknown
 * @author Max Berger
 * @version $Revision: 108 $
 */
public class Msup extends AbstractSubSuper implements MathMLScriptElement {

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "msup";

    /**
     * Creates a math element.
     * 
     * @param base
     *            The base for the math element tree.
     */
    public Msup(final MathBase base) {
        super(base);
    }

    /**
     * Paints this element.
     * 
     * @param g
     *            The graphics context to use for painting.
     * @param posX
     *            The first left position for painting.
     * @param posY
     *            The position of the baseline.
     */
    @Override
    public void paint(final Graphics2D g, final float posX, final float posY) {
        super.paint(g, posX, posY);
        final JEuclidElement e1 = this.getMathElement(0);
        final JEuclidElement e2 = this.getMathElement(1);

        final float posY2 = posY - this.getSuperBaseLineShift(g);
        // final int posY1 = posY + e1DescentHeight + e2.getAscentHeight(g)
        // - middleshift;
        //
        // if (posY2 + e2.getHeight(g) > posY1) {
        // // TODO: This belongs in SubSup, but not in Sup!
        // posY2 = posY1 - e2.getHeight(g);
        // // if main symbol is too small, sup- and subsymblos have not to be
        // // laid one on onother.
        // }
        e1.paint(g, posX, posY);
        e2.paint(g, posX + e1.getWidth(g), posY2);
    }

    /** {@inheritDoc} */
    @Override
    public float calculateWidth(final Graphics2D g) {
        return this.getMathElement(0).getWidth(g)
                + this.getMathElement(1).getWidth(g);
    }

    /** {@inheritDoc} */
    @Override
    public float calculateAscentHeight(final Graphics2D g) {
        return this.caclulateAscentHeightWithSuper(g);
    }

    /** {@inheritDoc} */
    @Override
    public float calculateDescentHeight(final Graphics2D g) {
        return this.getMathElement(0).getDescentHeight(g);
    }

    /** {@inheritDoc} */
    @Override
    public int getScriptlevelForChild(final JEuclidElement child) {
        if (child.isSameNode(this.getFirstChild())) {
            return this.getAbsoluteScriptLevel();
        } else {
            return this.getAbsoluteScriptLevel() + 1;
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean isChildBlock(final JEuclidElement child) {
        if (child.isSameNode(this.getFirstChild())) {
            return super.isChildBlock(child);
        } else {
            return false;
        }
    }

    /** {@inheritDoc} */
    public String getTagName() {
        return Msup.ELEMENT;
    }

    /** {@inheritDoc} */
    public JEuclidElement getBase() {
        return this.getMathElement(0);
    }

    /** {@inheritDoc} */
    public JEuclidElement getSubscript() {
        return null;
    }

    /** {@inheritDoc} */
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
