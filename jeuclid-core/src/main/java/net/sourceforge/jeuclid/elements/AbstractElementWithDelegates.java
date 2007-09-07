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

package net.sourceforge.jeuclid.elements;

import java.util.List;

import net.sourceforge.jeuclid.elements.presentation.AbstractContainer;

/**
 * Generic class for all mathobjects that can be represented using other Math
 * objects. These math objects use a delegates for the actual display and
 * calculations.
 * <p>
 * To use this class, overwrite {@link #createDelegates()} to create the
 * delegate objects.
 * 
 * @author Max Berger
 * @version $Revision$
 */
public abstract class AbstractElementWithDelegates extends AbstractContainer {

    private List<JEuclidElement> delegates;

    /**
     * default constructor.
     */
    public AbstractElementWithDelegates() {
        super();
    }

    /**
     * Overwrite this function in your implementation.
     * 
     * @return a MathObject representing the real contents.
     */
    protected abstract List<JEuclidElement> createDelegates();

    private void prepareDelegates() {
        this.delegates = this.createDelegates();
        for (final JEuclidElement element : this.delegates) {
            element.setFakeParent(this);
        }
    }

    // TODO
    // /** {@inheritDoc} */
    // @Override
    // public float calculateAscentHeight(final Graphics2D g) {
    // return ElementListSupport.getAscentHeight(g, this.delegates);
    // }
    //
    // /** {@inheritDoc} */
    // @Override
    // public float calculateDescentHeight(final Graphics2D g) {
    // return ElementListSupport.getDescentHeight(g, this.delegates);
    // }
    //
    // /** {@inheritDoc} */
    // @Override
    // public float calculateWidth(final Graphics2D g) {
    // return ElementListSupport.getWidth(g, this.delegates);
    // }
    //
    // /** {@inheritDoc} */
    // @Override
    // public void paint(final Graphics2D g, final float posX, final float
    // posY) {
    // super.paint(g, posX, posY);
    // ElementListSupport.paint(g, posX, posY, this.delegates);
    // }

    /** {@inheritDoc} */
    @Override
    protected void changeHook() {
        super.changeHook();
        this.prepareDelegates();
    }
}
