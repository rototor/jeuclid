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

package net.sourceforge.jeuclid.element;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.element.generic.AbstractRowLikeElement;
import net.sourceforge.jeuclid.element.generic.MathElement;

import org.w3c.dom.mathml.MathMLMathElement;

/**
 * The root element for creating a MathElement tree.
 * 
 * @author Unknown
 * @author Max Berger
 */
public class MathMathElement extends AbstractRowLikeElement implements
        MathMLMathElement {
    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "math";

    private boolean m_debug = false;

    /**
     * Creates a math element.
     * 
     * @param base
     *            The base for the math element tree.
     */
    public MathMathElement(final MathBase base) {
        super(base);
    }

    /**
     * Set the type of equation.
     * 
     * @param display
     *            INLINE|BLOCK
     */
    public void setDisplay(final String display) {
        this.setAttribute("display", display);
    }

    /**
     * Returns the display.
     * 
     * @return Display display
     */
    public String getDisplay() {
        return this.getMathAttribute("display");
    }

    /**
     * Enables, or disables the debug display.
     * 
     * @param debug
     *            Debug display
     */
    public void setDebug(final boolean debug) {
        this.m_debug = debug;
    }

    /**
     * Indicates, if the debug display is enabled.
     * 
     * @return True, if the debug display is enabled
     */
    public boolean isDebug() {
        return this.m_debug;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isChildBlock(final MathElement child) {
        return "block".equalsIgnoreCase(this.getDisplay());
    }

    /** {@inheritDoc} */
    public String getMacros() {
        return this.getMathAttribute("macros");
    }

    /** {@inheritDoc} */
    public void setMacros(final String macros) {
        this.setAttribute("macros", macros);
    }

    /** {@inheritDoc} */
    public String getTagName() {
        return ELEMENT;
    }

}
