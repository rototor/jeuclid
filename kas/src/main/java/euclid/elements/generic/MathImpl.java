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

/* $Id: MathImpl.java 806 2008-06-24 20:21:55Z maxberger $ */

package euclid.elements.generic;

import euclid.Constants;
import euclid.LayoutContext;

import org.w3c.dom.Node;
import org.w3c.dom.mathml.MathMLMathElement;

import euclid.context.Display;
import euclid.context.Parameter;
import euclid.elements.presentation.AbstractContainer;

/**
 * The root element for creating a MathElement tree.
 * 
 * @version $Revision: 806 $
 */
public final class MathImpl extends AbstractContainer implements
        MathMLMathElement {

    private final class ChildContext implements LayoutContext {
        private final LayoutContext context;

        private ChildContext(final LayoutContext myContext) {
            this.context = myContext;
        }

        public Object getParameter(final Parameter which) {
            Object retVal;
            if (Parameter.DISPLAY.equals(which)) {
                if (MathImpl.DISPLAY_BLOCK.equals(MathImpl.this.getDisplay())) {
                    retVal = Display.BLOCK;
                } else {
                    retVal = Display.INLINE;
                }
            } else {
                retVal = MathImpl.this.applyLocalAttributesToContext(
                        this.context).getParameter(which);
            }

            final String s = MathImpl.this.getAttributeNS(
                    Constants.NS_CONTEXT, which.toString());
            if ((s != null) && (s.length() > 0)) {
                retVal = which.fromString(s);
            }

            return retVal;
        }
    }

    /** attribute for display. */
    public static final String ATTR_DISPLAY = "display";

    /** attribute for macros. */
    public static final String ATTR_MACROS = "macros";

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "math";

    private static final long serialVersionUID = 1L;

    /** attribute for mode. */
    private static final String ATTR_MODE = "mode";

    private static final String DISPLAY_INLINE = "inline";

    private static final String DISPLAY_BLOCK = "block";

    // Happens to be display as well.
    private static final String DEPRECATED_BLOCK_VALUE_FOR_MODE = MathImpl.ATTR_DISPLAY;

    /**
     * Creates a math element.
     */
    public MathImpl() {
        super();
    }

    /** {@inheritDoc} */
    @Override
    protected Node newNode() {
        return new MathImpl();
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
        final String retVal;
        final String attrDisplay = this
                .getMathAttribute(MathImpl.ATTR_DISPLAY);
        if (attrDisplay == null) {
            if (MathImpl.DEPRECATED_BLOCK_VALUE_FOR_MODE
                    .equalsIgnoreCase(this
                            .getMathAttribute(MathImpl.ATTR_MODE))) {
                retVal = MathImpl.DISPLAY_BLOCK;
            } else {
                retVal = MathImpl.DISPLAY_INLINE;
            }
        } else {
            if (MathImpl.DISPLAY_BLOCK.equalsIgnoreCase(attrDisplay)) {
                retVal = MathImpl.DISPLAY_BLOCK;
            } else {
                retVal = MathImpl.DISPLAY_INLINE;
            }
        }
        return retVal;
    }

    /** {@inheritDoc} */
    @Override
    public LayoutContext getChildLayoutContext(final int childNum,
            final LayoutContext context) {
        return new ChildContext(context);
    }

    /** {@inheritDoc} */
    public String getMacros() {
        return this.getMathAttribute(MathImpl.ATTR_MACROS);
    }

    /** {@inheritDoc} */
    public void setMacros(final String macros) {
        this.setAttribute(MathImpl.ATTR_MACROS, macros);
    }

}
