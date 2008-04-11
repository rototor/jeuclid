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

/**
 * Interface for nodes which are capable of tracking changes.
 * 
 * @version $Revision$
 */
public interface ChangeTrackingInterface {
    /**
     * Called when the element has changed.
     * 
     * @param propagate
     *            if set to true, change is also fired on parent and
     *            registered listeners.
     */
    void fireChanged(boolean propagate);

    /**
     * Adds a change listener to this element.
     * 
     * @param listener
     *            the element to be notified in case of changes (if propagate
     *            is set).
     */
    void addListener(ChangeTrackingListener listener);

    /**
     * fires a change on this element and all its children, but no listeners
     * and parents.
     */
    void fireChangeForSubTree();
}
