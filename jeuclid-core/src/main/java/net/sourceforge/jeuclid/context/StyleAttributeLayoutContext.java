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

import java.awt.Color;

import net.sourceforge.jeuclid.LayoutContext;
import net.sourceforge.jeuclid.elements.support.attributes.AttributesHelper;

/**
 * @version $Revision$
 */
public class StyleAttributeLayoutContext implements LayoutContext {

    private final LayoutContext parentLayoutContext;

    private final String sizeString;

    private final Color foregroundColor;

    /**
     * Default Constructor.
     * 
     * @param parent
     *            LayoutContext of parent.
     * @param msize
     *            msize String to apply to parent context.
     * @param foreground
     *            Foreground color for new context.
     */
    public StyleAttributeLayoutContext(final LayoutContext parent,
            final String msize, final Color foreground) {
        this.parentLayoutContext = parent;
        this.sizeString = msize;
        this.foregroundColor = foreground;
    }

    /** {@inheritDoc} */
    public Object getParameter(final Parameter which) {
        final Object retVal;
        if (Parameter.MATHSIZE.equals(which) && (this.sizeString != null)) {
            retVal = AttributesHelper.convertSizeToPt(this.sizeString,
                    this.parentLayoutContext, AttributesHelper.PT);
        } else if (Parameter.MATHCOLOR.equals(which)
                && this.foregroundColor != null) {
            retVal = this.foregroundColor;
        } else {
            retVal = this.parentLayoutContext.getParameter(which);
        }
        return retVal;
    }
}
