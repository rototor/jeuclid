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

    private final Set<EventListener> mutationListeners = new HashSet<EventListener>();

    public class MutationEventImpl implements MutationEvent {

        final EventTarget target;

        EventTarget currentTarget;

        public MutationEventImpl(final EventTarget element) {
            this.target = element;
            this.currentTarget = element;
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

        public boolean getBubbles() {
            return true;
        }

        public boolean getCancelable() {
            return false;
        }

        public EventTarget getCurrentTarget() {
            return this.currentTarget;
        }

        public short getEventPhase() {
            return Event.BUBBLING_PHASE;
        }

        public EventTarget getTarget() {
            return this.target;
        }

        public long getTimeStamp() {
            throw new UnsupportedOperationException("getTimeStamp");
        }

        public String getType() {
            return AbstractEventTargetImpl.MUTATIONSEVENTS;
        }

        public void initEvent(final String eventTypeArg,
                final boolean canBubbleArg, final boolean cancelableArg) {
            throw new UnsupportedOperationException("initEvent");
        }

        public void preventDefault() {
            throw new UnsupportedOperationException("preventDefault");
        }

        public void stopPropagation() {
            throw new UnsupportedOperationException("stopPropagation");
        }

        public void setCurrentTarget(final EventTarget current) {
            this.currentTarget = current;
        }

    }

    protected MutationEvent mutationEventFactory() {
        return new MutationEventImpl(this);
    }

    /** {@inheritDoc} */
    public boolean dispatchEvent(final Event evt) {

        if (evt instanceof MutationEventImpl) {
            final MutationEventImpl mutEvt = (MutationEventImpl) evt;
            mutEvt.setCurrentTarget(this);
        }

        this.changeHook(this);

        for (final EventListener listener : this.mutationListeners) {
            listener.handleEvent(evt);
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
        if (!(AbstractEventTargetImpl.MUTATIONSEVENTS.equalsIgnoreCase(type))) {
            throw new UnsupportedOperationException(
                    "Unsupported Event Type: " + type);
        }
        if (useCapture) {
            throw new UnsupportedOperationException(
                    "Capture is not supported!");
        }
        this.mutationListeners.add(listener);
    }

    /** {@inheritDoc} */
    public void removeEventListener(final String type,
            final EventListener listener, final boolean useCapture) {
        this.mutationListeners.remove(listener);
    }

    protected void changeHook(final Node origin) {
        // Overwrite me!
    }

}
