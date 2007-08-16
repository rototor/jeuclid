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
 * @author Max Berger
 * @version $Revision$
 */
public class StyleAttributeLayoutContext implements LayoutContext {

    private final LayoutContext parentLayoutContext;

    private final String sizeString;

    private final Color foregroundColor;

    private final Color backgroundColor;

    /**
     * Default Constructor.
     * 
     * @param parent
     *            LayoutContext of parent.
     * @param msize
     *            msize String to apply to parent context.
     * @param foreground
     *            Foreground color for new context.
     * @param background
     *            Background color for new context.
     */
    public StyleAttributeLayoutContext(final LayoutContext parent,
            final String msize, final Color foreground, final Color background) {
        this.parentLayoutContext = parent;
        this.sizeString = msize;
        this.foregroundColor = foreground;
        this.backgroundColor = background;
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
        } else if (Parameter.MATHBACKGROUND.equals(which)
                && this.backgroundColor != null) {
            // This means, that a "null" (transparent) background color will
            // never override a non-null background color from its parent.
            //
            // In reality, this is not important, as an element having a
            // transparent background color will still inherit the background
            // color from its parent.
            retVal = this.backgroundColor;
        } else {
            retVal = this.parentLayoutContext.getParameter(which);
        }
        return retVal;
    }
}
