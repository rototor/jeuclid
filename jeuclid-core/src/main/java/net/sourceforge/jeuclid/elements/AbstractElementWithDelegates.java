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
import net.sourceforge.jeuclid.layout.LayoutableNode;

import org.apache.batik.dom.AbstractDocument;

/**
 * Generic class for all mathobjects that can be represented using other Math
 * objects. These math objects use a delegates for the actual display and
 * calculations.
 * <p>
 * To use this class, overwrite {@link #createDelegates()} to create the
 * delegate objects.
 * 
 * @version $Revision$
 */
public abstract class AbstractElementWithDelegates extends AbstractContainer {

    // TODO: Re-Enable resetting delegates on changeHook!
    private List<LayoutableNode> delegates;

    /**
     * Default constructor. Sets MathML Namespace.
     * 
     * @param qname
     *            Qualified name.
     * @param odoc
     *            Owner Document.
     */
    public AbstractElementWithDelegates(final String qname,
            final AbstractDocument odoc) {
        super(qname, odoc);
    }

    /**
     * Overwrite this function in your implementation.
     * 
     * @return a MathObject representing the real contents.
     */
    protected abstract List<LayoutableNode> createDelegates();

    private void prepareDelegates() {
        if (this.delegates == null) {
            this.delegates = this.createDelegates();
            for (final LayoutableNode element : this.delegates) {
                ((JEuclidElement) element).setFakeParent(this);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public List<LayoutableNode> getChildrenToLayout() {
        this.prepareDelegates();
        return this.delegates;
    }

    /** {@inheritDoc} */
    @Override
    public List<LayoutableNode> getChildrenToDraw() {
        this.prepareDelegates();
        return this.delegates;
    }

}
