/*
 * Copyright 2007 - 2007 JEuclid, http://jeuclid.sf.net
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

import java.awt.Graphics2D;

import net.sourceforge.jeuclid.MathBase;

/**
 * Common base class for all elements that present themself as a row of
 * children, like the mrow element.
 * 
 * @author Max Berger
 * @version $Revision$
 */
public abstract class AbstractRowLike extends AbstractMathElementWithChildren {

    /**
     * Default constructor.
     * 
     * @param base
     *            MathBase to use.
     */
    public AbstractRowLike(final MathBase base) {
        super(base);
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

    /** {@inheritDoc} */
    @Override
    public void paint(final Graphics2D g, final float posX, final float posY) {
        super.paint(g, posX, posY);
        this.paintChildren(g, posX, posY);
    }

}
