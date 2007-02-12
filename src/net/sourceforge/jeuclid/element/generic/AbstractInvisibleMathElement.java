/*
 * Copyright 2002 - 2006 JEuclid, http://jeuclid.sf.net
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

package net.sourceforge.jeuclid.element.generic;

import java.awt.Graphics;

import net.sourceforge.jeuclid.MathBase;

/**
 * Represents a MathElement with no content.
 * 
 * @author Max Berger
 */
public class AbstractInvisibleMathElement extends AbstractMathElement {
    /**
     * Default Constructor.
     * 
     * @param base
     *            MathBase to use.
     */
    public AbstractInvisibleMathElement(MathBase base) {
        super(base);
    }

    /** {@inheritDoc} */
    public void paint(Graphics g, int posX, int posY) {
        super.paint(g, posX, posY);
    }

    /** {@inheritDoc} */
    public int calculateAscentHeight(Graphics g) {
        return 0;
    }

    /** {@inheritDoc} */
    public int calculateDescentHeight(Graphics g) {
        return 0;
    }

    /** {@inheritDoc} */
    public int calculateWidth(Graphics g) {
        return 0;
    }

}
