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

package net.sourceforge.jeuclid.element.generic;

import java.awt.Graphics2D;
import java.util.List;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.element.helpers.ElementListSupport;

/**
 * Generic class for all mathobjects that can be represented using other Math
 * objects. These math objects use a delegates for the actual display and
 * calculations.
 * <p>
 * To use this class, overwrite {@link #createDelegates()} to create the
 * delegate objects.
 * 
 * @author Max Berger
 */
public abstract class AbstractElementWithDelegates extends
        AbstractMathContainer {

    private List<AbstractMathElement> delegates;

    /**
     * default constructor.
     * 
     * @param base
     *            the MathBase to use.
     */
    public AbstractElementWithDelegates(final MathBase base) {
        super(base);
    }

    /**
     * Overwrite this function in your implementation.
     * 
     * @return a MathObject representing the real contents.
     */
    abstract protected List<AbstractMathElement> createDelegates();

    private void prepareDelegates() {
        this.delegates = this.createDelegates();
        for (final AbstractMathElement element : this.delegates) {
            element.setFakeParent(this);
        }
        this.setChanged(false);
    }

    /** {@inheritDoc} */
    @Override
    public void eventElementComplete() {
        super.eventElementComplete();
        this.prepareDelegates();
    }

    /** {@inheritDoc} */
    @Override
    public int calculateAscentHeight(final Graphics2D g) {
        if (this.isChanged()) {
            this.prepareDelegates();
        }
        return ElementListSupport.getAscentHeight(g, this.delegates);
    }

    /** {@inheritDoc} */
    @Override
    public int calculateDescentHeight(final Graphics2D g) {
        if (this.isChanged()) {
            this.prepareDelegates();
        }
        return ElementListSupport.getDescentHeight(g, this.delegates);
    }

    /** {@inheritDoc} */
    @Override
    public int calculateWidth(final Graphics2D g) {
        if (this.isChanged()) {
            this.prepareDelegates();
        }
        return ElementListSupport.getWidth(g, this.delegates);
    }

    /** {@inheritDoc} */
    @Override
    public void paint(final Graphics2D g, final int posX, final int posY) {
        if (this.isChanged()) {
            this.prepareDelegates();
        }
        super.paint(g, posX, posY);
        ElementListSupport.paint(g, posX, posY, this.delegates);
    }

}
