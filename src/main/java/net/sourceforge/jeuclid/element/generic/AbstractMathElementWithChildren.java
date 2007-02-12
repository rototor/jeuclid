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

/* $Id: AbstractMathElementWithChildren.java,v 1.1.2.7 2007/02/10 22:57:23 maxberger Exp $ */

package net.sourceforge.jeuclid.element.generic;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.element.helpers.ElementListSupport;

/**
 * Represents a Math element that is painted and defined through its children.
 * 
 * @author Max Berger
 */
public abstract class AbstractMathElementWithChildren extends
        AbstractMathContainer {
    /**
     * Default constructor.
     * 
     * @param base
     *            MathBase to use.
     */
    public AbstractMathElementWithChildren(final MathBase base) {
        super(base);
    }

    private List<AbstractMathElement> getChildrenAsList() {
        final List<AbstractMathElement> children = new ArrayList<AbstractMathElement>(
                this.getMathElementCount());
        for (int i = 0; i < this.getMathElementCount(); i++) {
            children.add(this.getMathElement(i));
        }
        return children;
    }

    /**
     * Paint all children. To be called from within a paint() method.
     * 
     * @param g
     *            Graphics2D context
     * @param posX
     *            x-offset to start painting
     * @param posY
     *            y-offset to start painting
     */
    protected void paintChildren(final Graphics2D g, final int posX,
            final int posY) {
        super.paint(g, posX, posY);
        ElementListSupport.paint(g, posX, posY, this.getChildrenAsList());
    }

    /** {@inheritDoc} */
    public int calculateChildrenWidth(final Graphics2D g) {
        return ElementListSupport.getWidth(g, this.getChildrenAsList());
    }

    /** {@inheritDoc} */
    public int calculateChildrenAscentHeight(final Graphics2D g) {
        return ElementListSupport
                .getAscentHeight(g, this.getChildrenAsList());
    }

    /** {@inheritDoc} */
    public int calculateChildrenDescentHeight(final Graphics2D g) {
        return ElementListSupport.getDescentHeight(g, this
                .getChildrenAsList());
    }

}
