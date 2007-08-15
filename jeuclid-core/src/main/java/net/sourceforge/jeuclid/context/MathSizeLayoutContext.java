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
import net.sourceforge.jeuclid.elements.support.attributes.AttributesHelper;

/**
 * @author Max Berger
 * @version $Revision$
 */
public class MathSizeLayoutContext implements LayoutContext {

    private final LayoutContext parentLayoutContext;

    private final String sizeString;

    /**
     * Default Constructor.
     * 
     * @param parent
     *            LayoutContext of parent.
     * @param msize
     *            msize String to apply to parent Context
     */
    public MathSizeLayoutContext(final LayoutContext parent,
            final String msize) {
        this.parentLayoutContext = parent;
        this.sizeString = msize;
    }

    /** {@inheritDoc} */
    public Object getParameter(final Parameter which) {
        if (Parameter.MATHSIZE.equals(which)) {
            return AttributesHelper.convertSizeToPt(this.sizeString,
                    this.parentLayoutContext, AttributesHelper.PT);
        }
        return this.parentLayoutContext.getParameter(which);
    }
}
