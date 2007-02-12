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

/* $Id: AbstractChangeTrackingElement.java,v 1.1.2.1 2007/02/05 21:54:03 maxberger Exp $ */

package net.sourceforge.jeuclid.dom;

import org.w3c.dom.Node;

/**
 * generic implementation of Element that tries to track if a change has
 * happened.
 * 
 * @author Max Berger
 */
public abstract class AbstractChangeTrackingElement extends
        AbstractPartialElementImpl {

    private boolean changed = true;

    /** {@inheritDoc} */
    @Override
    public final Node appendChild(final Node newChild) {
        this.setChanged(true);
        return super.appendChild(newChild);
    }

    /** {@inheritDoc} */
    @Override
    public final void setAttribute(final String name, final String value) {
        this.setChanged(true);
        super.setAttribute(name, value);
    }

    /** {@inheritDoc} */
    @Override
    public final void setTextContent(final String newTextContent) {
        this.setChanged(true);
        super.setTextContent(newTextContent);
    }

    /**
     * @return the hasChanged
     */
    protected final boolean isChanged() {
        return this.changed;
    }

    /**
     * @param hasChanged
     *            the hasChanged to set
     */
    protected void setChanged(final boolean hasChanged) {
        this.changed = hasChanged;
    }
}
