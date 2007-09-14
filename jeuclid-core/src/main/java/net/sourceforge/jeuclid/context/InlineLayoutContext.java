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

package net.sourceforge.jeuclid.context;

import net.sourceforge.jeuclid.LayoutContext;

/**
 * @version $Revision$
 */
public class InlineLayoutContext implements LayoutContext {

    private final LayoutContext parentLayoutContext;

    private final boolean increaseIfAlreadyInline;

    /**
     * Default Constructor.
     * 
     * @param parent
     *            LayoutContext of parent.
     */
    public InlineLayoutContext(final LayoutContext parent) {
        this.parentLayoutContext = parent;
        this.increaseIfAlreadyInline = false;
    }

    /**
     * Behavior for mfrac.
     * 
     * @param parent
     *            LayoutContext of parent.
     * @param increase
     *            increase scriptlevel if already inline.
     */
    public InlineLayoutContext(final LayoutContext parent,
            final boolean increase) {
        this.parentLayoutContext = parent;
        this.increaseIfAlreadyInline = increase;
    }

    /** {@inheritDoc} */
    public Object getParameter(final Parameter which) {
        Object retVal;
        if (Parameter.DISPLAY.equals(which)) {
            retVal = Display.INLINE;
        } else if (this.increaseIfAlreadyInline
                && (Parameter.SCRIPTLEVEL.equals(which))
                && (Display.INLINE.equals(this.parentLayoutContext
                        .getParameter(Parameter.DISPLAY)))) {
            retVal = ((Integer) this.parentLayoutContext
                    .getParameter(Parameter.SCRIPTLEVEL)) + 1;
        } else {
            retVal = this.parentLayoutContext.getParameter(which);
        }
        return retVal;
    }
}
