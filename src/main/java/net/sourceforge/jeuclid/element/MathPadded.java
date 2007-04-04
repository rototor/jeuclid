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

package net.sourceforge.jeuclid.element;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.element.generic.AbstractRowLikeElement;

import org.w3c.dom.mathml.MathMLPaddedElement;

/**
 * This class implemented the mpadded element.
 * 
 * @author Max Berger
 * @todo none of the attributes are actually implemented yet.
 */
public class MathPadded extends AbstractRowLikeElement implements
        MathMLPaddedElement {

    /** constant for depth attribute. */
    public static final String ATTR_DEPTH = "depth";

    /** constant for height attribute. */
    public static final String ATTR_HEIGHT = "height";

    /** constant for lspace attribute. */
    public static final String ATTR_LSPACE = "lspace";

    /** constant for width attribute. */
    public static final String ATTR_WIDTH = "width";

    /**
     * The MathML element name for this class.
     */
    public static final String ELEMENT = "mpadded";

    /**
     * Default constructor.
     * 
     * @param base
     *            Mathbase to use.
     */
    public MathPadded(final MathBase base) {
        super(base);
    }

    /** {@inheritDoc} */
    public String getDepth() {
        return this.getMathAttribute(MathPadded.ATTR_DEPTH);
    }

    /** {@inheritDoc} */
    public String getHeight() {
        return this.getMathAttribute(MathPadded.ATTR_HEIGHT);
    }

    /** {@inheritDoc} */
    public String getLspace() {
        return this.getMathAttribute(MathPadded.ATTR_LSPACE);
    }

    /** {@inheritDoc} */
    public String getWidth() {
        return this.getMathAttribute(MathPadded.ATTR_WIDTH);
    }

    /** {@inheritDoc} */
    public void setDepth(final String depth) {
        this.setAttribute(MathPadded.ATTR_DEPTH, depth);
    }

    /** {@inheritDoc} */
    public void setHeight(final String height) {
        this.setAttribute(MathPadded.ATTR_HEIGHT, height);
    }

    /** {@inheritDoc} */
    public void setLspace(final String lspace) {
        this.setAttribute(MathPadded.ATTR_LSPACE, lspace);
    }

    /** {@inheritDoc} */
    public void setWidth(final String width) {
        this.setAttribute(MathPadded.ATTR_WIDTH, width);
    }

    /** {@inheritDoc} */
    public String getTagName() {
        return MathPadded.ELEMENT;
    }

}
