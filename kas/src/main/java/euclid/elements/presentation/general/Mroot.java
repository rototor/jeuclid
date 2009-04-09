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

package euclid.elements.presentation.general;

import java.util.ArrayList;
import java.util.List;

import euclid.LayoutContext;

import org.w3c.dom.Node;
import org.w3c.dom.mathml.MathMLElement;

import euclid.context.InlineLayoutContext;
import euclid.context.RelativeScriptlevelLayoutContext;
import euclid.layout.LayoutableNode;

/**
 * This class presents a mathematical root.
 * 
 * @version $Revision$
 */
public final class Mroot extends AbstractRoot {

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "mroot";

    private static final long serialVersionUID = 1L;

    /**
     * Creates a math element.
     */
    public Mroot() {
        super();
    }

    /** {@inheritDoc} */
    @Override
    protected Node newNode() {
        return new Mroot();
    }

    /** {@inheritDoc} */
    @Override
    protected List<LayoutableNode> getContent() {
        final List<LayoutableNode> mList = new ArrayList<LayoutableNode>(1);
        mList.add((LayoutableNode) this.getRadicand());
        return mList;
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
        final LayoutContext now = this.applyLocalAttributesToContext(context);
        if (childNum == 0) {
            return now;
        } else {
            // As specified in M2 3.3.3.2
            return new RelativeScriptlevelLayoutContext(
                    new InlineLayoutContext(now), 2);
        }
    }

}
