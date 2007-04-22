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

/* $Id: MathPhantom.java 136 2007-04-19 13:58:28Z maxberger $ */

package net.sourceforge.jeuclid.elements.presentation.general;

import java.awt.Graphics2D;

import net.sourceforge.jeuclid.MathBase;

/**
 * This class represents a phantom of a math element. This is used as spacer.
 * 
 * @author Unknown
 * @author Max Berger
 * @version $Revision: 108 $
 */
public class Mphantom extends AbstractMathElementWithChildren {

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
    public Mphantom(final MathBase base) {
        super(base);
    }

    /** {@inheritDoc} */
    @Override
    public void paint(final Graphics2D g, final float posX, final float posY) {
        super.paint(g, posX, posY);
    }

    /** {@inheritDoc} */
    public String getTagName() {
        return Mphantom.ELEMENT;
    }

    /** {@inheritDoc} */
    @Override
    public float calculateAscentHeight(final Graphics2D g) {
        return super.calculateChildrenAscentHeight(g);
    }

    /** {@inheritDoc} */
    @Override
    public float calculateDescentHeight(final Graphics2D g) {
        return super.calculateChildrenDescentHeight(g);
    }

    /** {@inheritDoc} */
    @Override
    public float calculateWidth(final Graphics2D g) {
        return super.calculateChildrenWidth(g);
    }

}
