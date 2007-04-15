/*
 * Copyright 2007 - 2007 JEuclid, http://jeuclid.sf.net
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

package net.sourceforge.jeuclid.element.generic;

import net.sourceforge.jeuclid.MathBase;

import org.w3c.dom.mathml.MathMLElement;
import org.w3c.dom.mathml.MathMLUnderOverElement;

/**
 * Implementation and helper methods for munder, mover, and munderover.
 * 
 * @author Max Berger
 */
public abstract class AbstractUnderOverElement extends AbstractMathElement
        implements MathMLUnderOverElement {

    /** attribute for accent property. */
    public static final String ATTR_ACCENT = "accent";

    /** attribute for accentunder property. */
    public static final String ATTR_ACCENTUNDER = "accentunder";

    /**
     * default constructor.
     * 
     * @param base
     *            Mathbase to use.
     */
    public AbstractUnderOverElement(final MathBase base) {
        super(base);
    }

    /** {@inheritDoc} */
    public String getAccent() {
        return this.getMathAttribute(AbstractUnderOverElement.ATTR_ACCENT);
    }

    /**
     * returns the accent property as boolean.
     * 
     * @return accent
     */
    protected boolean getAccentAsBoolean() {
        return Boolean.parseBoolean(this.getAccent());
    }

    /** {@inheritDoc} */
    public String getAccentunder() {
        return this
                .getMathAttribute(AbstractUnderOverElement.ATTR_ACCENTUNDER);
    }

    /** {@inheritDoc} */
    @Override
    public int getScriptlevelForChild(final MathElement child) {
        if (child.isSameNode(this.getBase())) {
            return this.getAbsoluteScriptLevel();
        } else {
            // TODO: Should depend on type and accent
            return this.getAbsoluteScriptLevel() + 1;
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean isChildBlock(final MathElement child) {
        if (child.isSameNode(this.getBase())) {
            return super.isChildBlock(child);
        } else {
            return false;
        }
    }

    /**
     * returns the accentunder property as boolean.
     * 
     * @return accentunder
     */
    protected boolean getAccentunderAsBoolean() {
        return Boolean.parseBoolean(this.getAccentunder());
    }

    /** {@inheritDoc} */
    public MathElement getBase() {
        return this.getMathElement(0);
    }

    /** {@inheritDoc} */
    public void setAccent(final String accent) {
        this.setAttribute(AbstractUnderOverElement.ATTR_ACCENT, accent);
    }

    /** {@inheritDoc} */
    public void setAccentunder(final String accentunder) {
        this.setAttribute(AbstractUnderOverElement.ATTR_ACCENTUNDER,
                accentunder);
    }

    /** {@inheritDoc} */
    public void setBase(final MathMLElement base) {
        this.setMathElement(0, base);
    }

}
