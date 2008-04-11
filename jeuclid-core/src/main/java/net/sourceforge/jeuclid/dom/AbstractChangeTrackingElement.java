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

package net.sourceforge.jeuclid.dom;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.jeuclid.elements.support.ElementListSupport;

import org.w3c.dom.Node;

/**
 * generic implementation of Element that tries to track if a change has
 * happened.
 * 
 * @version $Revision$
 */
public abstract class AbstractChangeTrackingElement extends
        AbstractPartialElementImpl implements ChangeTrackingInterface,
        ChangeTrackingListener {

    private final Set<ChangeTrackingListener> listeners = new HashSet<ChangeTrackingListener>();

    /** {@inheritDoc} */
    @Override
    public final Node appendChild(final Node newChild) {
        final Node retVal = super.appendChild(newChild);
        this.fireChanged(true);
        return retVal;
    }

    /** {@inheritDoc} */
    @Override
    public final void setAttribute(final String name, final String value) {
        super.setAttribute(name, value);
        this.fireChanged(true);
    }

    /** {@inheritDoc} */
    @Override
    public final void setTextContent(final String newTextContent) {
        super.setTextContent(newTextContent);
        this.fireChanged(true);
    }

    /** {@inheritDoc} */
    @Override
    public final Node replaceChild(final Node newChild, final Node oldChild) {
        final Node retVal = super.replaceChild(newChild, oldChild);
        this.fireChanged(true);
        return retVal;
    }

    public void changeHook(final Node origin) {
        // Overwrite me!
    }

    /** {@inheritDoc} */
    public void fireChanged(final boolean propagate) {
        this.changeHook(this);
        if (propagate) {
            final Node superNode = this.getParentNode();
            if (superNode instanceof ChangeTrackingInterface) {
                ((ChangeTrackingInterface) superNode).fireChanged(propagate);
            }
            for (final ChangeTrackingListener listener : this.listeners) {
                listener.changeHook(this);
            }
        }
    }

    /** {@inheritDoc} */
    public void fireChangeForSubTree() {
        this.fireChanged(false);
        ElementListSupport.fireChangeForSubTree(ElementListSupport
                .createListOfChildren(this));
    }

    /** {@inheritDoc} */
    public void addListener(final ChangeTrackingListener listener) {
        this.listeners.add(listener);
    }

}
