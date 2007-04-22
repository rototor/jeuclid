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

/* $Id: MathRoot.java 139 2007-04-21 16:12:43Z maxberger $ */

package net.sourceforge.jeuclid.elements.presentation.general;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.elements.JEuclidElement;

import org.w3c.dom.mathml.MathMLElement;
import org.w3c.dom.mathml.MathMLRadicalElement;

/**
 * This class presents a mathematical root.
 * 
 * @author Unknown
 * @author Max Berger
 * @version $Revision: 108 $
 */
public class Mroot extends AbstractRoot implements MathMLRadicalElement {

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "mroot";

    /**
     * Creates a math element.
     * 
     * @param base
     *            The base for the math element tree.
     */
    public Mroot(final MathBase base) {
        super(base);
    }

    /** {@inheritDoc} */
    public String getTagName() {
        return Mroot.ELEMENT;
    }

    /** {@inheritDoc} */
    @Override
    protected List<JEuclidElement> getContent() {
        final List<JEuclidElement> mList = new ArrayList<JEuclidElement>(1);
        mList.add(this.getMathElement(0));
        return mList;
    }

    /** {@inheritDoc} */
    @Override
    protected JEuclidElement getActualIndex() {
        return this.getMathElement(1);
    }

    /** {@inheritDoc} */
    public MathMLElement getIndex() {
        return this.getMathElement(1);
    }

    /** {@inheritDoc} */
    public MathMLElement getRadicand() {
        return this.getMathElement(0);
    }

    /** {@inheritDoc} */
    public void setIndex(final MathMLElement index) {
        this.setMathElement(1, index);
    }

    /** {@inheritDoc} */
    public void setRadicand(final MathMLElement radicand) {
        this.setMathElement(0, radicand);
    }

    /** {@inheritDoc} */
    @Override
    public int getScriptlevelForChild(final JEuclidElement child) {
        if (child.isSameNode(this.getIndex())) {
            return this.getAbsoluteScriptLevel() + 2;
        } else {
            return this.getAbsoluteScriptLevel();
        }
    }

}
