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

import java.awt.Graphics2D;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.element.generic.AbstractMathElementWithChildren;

/**
 * This class represents a phantom of a math element. This is used as spacer.
 * 
 */
public class MathPhantom extends AbstractMathElementWithChildren {

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "mphantom";

    /**
     * Creates a math element.
     * 
     * @param base
     *            The base for the math element tree.
     */
    public MathPhantom(final MathBase base) {
        super(base);
    }

    /** {@inheritDoc} */
    @Override
    public void paint(final Graphics2D g, final int posX, final int posY) {
        super.paint(g, posX, posY);
    }

    /** {@inheritDoc} */
    public String getTagName() {
        return MathPhantom.ELEMENT;
    }

    /** {@inheritDoc} */
    @Override
    public int calculateAscentHeight(final Graphics2D g) {
        return super.calculateChildrenAscentHeight(g);
    }

    /** {@inheritDoc} */
    @Override
    public int calculateDescentHeight(final Graphics2D g) {
        return super.calculateChildrenDescentHeight(g);
    }

    /** {@inheritDoc} */
    @Override
    public int calculateWidth(final Graphics2D g) {
        return super.calculateChildrenWidth(g);
    }

}
