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

/* $Id: MathString.java 52 2007-02-26 16:46:55Z maxberger $ */

package net.sourceforge.jeuclid.elements.presentation.token;

import net.sourceforge.jeuclid.MathBase;

import org.w3c.dom.mathml.MathMLStringLitElement;

/**
 * This class represents string in a equation.
 * 
 * @author Unknown
 * @author Max Berger
 * @version $Revision: 108 $
 */
public class Ms extends Mtext implements MathMLStringLitElement {

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "ms";

    /** Attribute for lquote. */
    public static final String ATTR_LQUOTE = "lquote";

    /** Attribute for rquote. */
    public static final String ATTR_RQUOTE = "rquote";

    private static final String VALUE_DOUBLEQUOTE = "\"";

    /**
     * Creates a math element.
     * 
     * @param base
     *            The base for the math element tree.
     */
    public Ms(final MathBase base) {
        super(base);
        this.setDefaultMathAttribute(Ms.ATTR_LQUOTE, Ms.VALUE_DOUBLEQUOTE);
        this.setDefaultMathAttribute(Ms.ATTR_RQUOTE, Ms.VALUE_DOUBLEQUOTE);
    }

    /**
     * @param lquote
     *            Left quote
     */
    public void setLquote(final String lquote) {
        this.setAttribute(Ms.ATTR_LQUOTE, lquote);
    }

    /**
     * @return Left quote
     */
    public String getLquote() {
        return this.getMathAttribute(Ms.ATTR_LQUOTE);
    }

    /**
     * @param rquote
     *            Right quota
     */
    public void setRquote(final String rquote) {
        this.setAttribute(Ms.ATTR_RQUOTE, rquote);
    }

    /**
     * @return Right quote
     */
    public String getRquote() {
        return this.getMathAttribute(Ms.ATTR_RQUOTE);
    }

    /**
     * Returns the text contentof this element.
     * 
     * @return Text content
     */
    @Override
    public String getText() {
        return this.getLquote() + super.getText() + this.getRquote();
    }

    /** {@inheritDoc} */
    @Override
    public String getTagName() {
        return Ms.ELEMENT;
    }
}
