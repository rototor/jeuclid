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

package net.sourceforge.jeuclid.elements.generic;

import net.sourceforge.jeuclid.LayoutContext;
import net.sourceforge.jeuclid.context.Display;
import net.sourceforge.jeuclid.elements.presentation.general.AbstractRowLike;

import org.w3c.dom.mathml.MathMLMathElement;

/**
 * The root element for creating a MathElement tree.
 * 
 * @version $Revision$
 */
public class MathImpl extends AbstractRowLike implements MathMLMathElement {

    /** attribute for display. */
    public static final String ATTR_DISPLAY = "display";

    /** attribute for macros. */
    public static final String ATTR_MACROS = "macros";

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "math";

    /**
     * Creates a math element.
     */
    public MathImpl() {
        super();
    }

    /**
     * Set the type of equation.
     * 
     * @param display
     *            INLINE|BLOCK
     */
    public void setDisplay(final String display) {
        this.setAttribute(MathImpl.ATTR_DISPLAY, display);
    }

    /**
     * Returns the display.
     * 
     * @return Display display
     */
    public String getDisplay() {
        return this.getMathAttribute(MathImpl.ATTR_DISPLAY);
    }

    /** {@inheritDoc} */
    @Override
    public LayoutContext getChildLayoutContext(final int childNum,
            final LayoutContext context) {
        return new LayoutContext() {

            public Object getParameter(final Parameter which) {
                final Object retVal;
                if (Parameter.DISPLAY.equals(which)) {
                    if ("block".equalsIgnoreCase(MathImpl.this.getDisplay())) {
                        retVal = Display.BLOCK;
                    } else {
                        retVal = Display.INLINE;
                    }
                } else {
                    retVal = MathImpl.this.applyLocalAttributesToContext(
                            context).getParameter(which);
                }
                return retVal;
            }
        };
    }

    /** {@inheritDoc} */
    public String getMacros() {
        return this.getMathAttribute(MathImpl.ATTR_MACROS);
    }

    /** {@inheritDoc} */
    public void setMacros(final String macros) {
        this.setAttribute(MathImpl.ATTR_MACROS, macros);
    }

    /** {@inheritDoc} */
    public String getTagName() {
        return MathImpl.ELEMENT;
    }

}
