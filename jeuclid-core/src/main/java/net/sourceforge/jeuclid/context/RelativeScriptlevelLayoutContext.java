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
public class RelativeScriptlevelLayoutContext implements LayoutContext {

    private final LayoutContext parentLayoutContext;

    private final int sizeOffset;

    /**
     * Default Constructor.
     * 
     * @param parent
     *            LayoutContext of parent.
     * @param offset
     *            offset to parent. In most cases this will be 1.
     * 
     */
    public RelativeScriptlevelLayoutContext(final LayoutContext parent,
            final int offset) {
        this.parentLayoutContext = parent;
        this.sizeOffset = offset;
    }

    /** {@inheritDoc} */
    public Object getParameter(final Parameter which) {
        if (Parameter.SCRIPTLEVEL.equals(which)) {
            return ((Integer) this.parentLayoutContext
                    .getParameter(Parameter.SCRIPTLEVEL))
                    + this.sizeOffset;
        }
        return this.parentLayoutContext.getParameter(which);
    }
}
