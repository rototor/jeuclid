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

import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventTarget;

/**
 * @version $Revision$
 */
public abstract class AbstractEventImpl implements Event {

    final EventTarget target;

    EventTarget currentTarget;

    /**
     * Default Constructor.
     */
    public AbstractEventImpl(final EventTarget element) {
        this.target = element;
        this.currentTarget = element;
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
