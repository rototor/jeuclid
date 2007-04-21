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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.element.generic.AbstractRootElement;
import net.sourceforge.jeuclid.element.generic.MathElement;

import org.w3c.dom.mathml.MathMLElement;
import org.w3c.dom.mathml.MathMLRadicalElement;

/**
 * This class presents a mathematical root.
 * 
 * @author Unknown
 * @author Max Berger
 */
public class MathRoot extends AbstractRootElement implements
        MathMLRadicalElement {

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
    public MathRoot(final MathBase base) {
        super(base);
    }

    /** {@inheritDoc} */
    public String getTagName() {
        return MathRoot.ELEMENT;
    }

    /** {@inheritDoc} */
    @Override
    protected List<MathElement> getContent() {
        final List<MathElement> mList = new ArrayList<MathElement>(1);
        mList.add(this.getMathElement(0));
        return mList;
    }

    /** {@inheritDoc} */
    @Override
    protected MathElement getActualIndex() {
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
    public int getScriptlevelForChild(final MathElement child) {
        if (child.isSameNode(this.getIndex())) {
            return this.getAbsoluteScriptLevel() + 2;
        } else {
            return this.getAbsoluteScriptLevel();
        }
    }

}
