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

/**
 * This class represents string in a equation.
 * 
 */
public class MathString extends MathText {

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "ms";

    private String m_lquote = "\"";

    private String m_rquote = "\"";

    /**
     * Creates a math element.
     * 
     * @param base
     *            The base for the math element tree.
     */
    public MathString(MathBase base) {
        super(base);
    }

    /**
     * @param lquote
     *            Left quote
     */
    public void setLQuote(String lquote) {
        if (lquote != null) {
            m_lquote = lquote;
        }
    }

    /**
     * @return Left quote
     */
    public String getLQuote() {
        return m_lquote;
    }

    /**
     * @param rquote
     *            Right quota
     */
    public void setRQuote(String rquote) {
        if (rquote != null) {
            m_rquote = rquote;
        }
    }

    /**
     * @return Right quote
     */
    public String getRQuote() {
        return m_rquote;
    }

    /**
     * Returns the text contentof this element.
     * 
     * @return Text content
     */
    public String getText() {
        return m_lquote + super.getText() + m_rquote;
    }

    /** {@inheritDoc} */
    public String getTagName() {
        return ELEMENT;
    }
}
