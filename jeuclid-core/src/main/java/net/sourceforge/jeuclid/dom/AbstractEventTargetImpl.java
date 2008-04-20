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
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.w3c.dom.Node;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.events.MutationEvent;

/**
 * @version $Revision$
 */
public abstract class AbstractEventTargetImpl extends AbstractPartialNodeImpl
        implements EventTarget {

    public static final String MUTATIONSEVENTS = "MutationEvents";

    private final Map<String, Set<EventListener>> mutationListeners = new TreeMap<String, Set<EventListener>>();

    public class MutationEventImpl extends AbstractEventImpl implements
            MutationEvent {

        public MutationEventImpl(final EventTarget element) {
            super(element);
        }

        public short getAttrChange() {
            return MutationEvent.MODIFICATION;
        }

        public String getAttrName() {
            throw new UnsupportedOperationException("getAttrName");
        }

        public String getNewValue() {
            throw new UnsupportedOperationException("getNewValue");
        }

        public String getPrevValue() {
            throw new UnsupportedOperationException("getPrevValue");
        }

        public Node getRelatedNode() {
            throw new UnsupportedOperationException("getRelatedNode");
        }

        public void initMutationEvent(final String typeArg,
                final boolean canBubbleArg, final boolean cancelableArg,
                final Node relatedNodeArg, final String prevValueArg,
                final String newValueArg, final String attrNameArg,
                final short attrChangeArg) {
            throw new UnsupportedOperationException("initMutationEvent");
        }

        public String getType() {
            return AbstractEventTargetImpl.MUTATIONSEVENTS;
        }

    }

    protected MutationEvent mutationEventFactory() {
        return new MutationEventImpl(this);
    }

    /** {@inheritDoc} */
    public boolean dispatchEvent(final Event evt) {

        if (evt instanceof AbstractEventImpl) {
            final AbstractEventImpl mutEvt = (AbstractEventImpl) evt;
            mutEvt.setCurrentTarget(this);
        }

        this.changeHook(this);

        final Set<EventListener> listeners = this.mutationListeners.get(evt
                .getType().toLowerCase(Locale.ENGLISH));

        if (listeners != null) {
            for (final EventListener listener : listeners) {
                listener.handleEvent(evt);
            }
        }

        final Node superNode = this.getParentNode();
        if (superNode instanceof EventTarget) {
            ((EventTarget) superNode).dispatchEvent(evt);
        }
        return true;
    }

    /** {@inheritDoc} */
    public void addEventListener(final String type,
            final EventListener listener, final boolean useCapture) {
        if (useCapture) {
            throw new UnsupportedOperationException(
                    "Capture is not supported!");
        }
        final String lowerType = type.toLowerCase(Locale.ENGLISH);
        Set<EventListener> listenerSet = this.mutationListeners
                .get(lowerType);
        if (listenerSet == null) {
            listenerSet = new HashSet<EventListener>();
            this.mutationListeners.put(lowerType, listenerSet);
        }
        listenerSet.add(listener);
    }

    /** {@inheritDoc} */
    public void removeEventListener(final String type,
            final EventListener listener, final boolean useCapture) {
        final String lowerType = type.toLowerCase(Locale.ENGLISH);
        final Set<EventListener> listenerSet = this.mutationListeners
                .get(lowerType);
        if (listenerSet != null) {
            listenerSet.remove(listener);
        }
    }

    protected void changeHook(final Node origin) {
        // Overwrite me!
    }

}
