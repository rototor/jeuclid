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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.jeuclid.LayoutContext;
import net.sourceforge.jeuclid.context.RelativeScriptlevelLayoutContext;
import net.sourceforge.jeuclid.elements.JEuclidElement;

import org.w3c.dom.mathml.MathMLElement;
import org.w3c.dom.mathml.MathMLRadicalElement;

/**
 * This class presents a mathematical root.
 * 
 * @author Unknown
 * @author Max Berger
 * @version $Revision$
 */
public class Mroot extends AbstractRoot implements MathMLRadicalElement {

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "mroot";

    /**
     * Creates a math element.
     */
    public Mroot() {
        super(AbstractRoot.STANDARD_ROOT_CHAR);
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
    public LayoutContext getChildLayoutContext(final int childNum,
            final LayoutContext context) {
        if (childNum == 0) {
            return new RelativeScriptlevelLayoutContext(context, 2);
        } else {
            return context;
        }
    }

}
