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

package net.sourceforge.jeuclid.elements.presentation.token;

import java.awt.Graphics2D;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.elements.AbstractJEuclidElement;
import net.sourceforge.jeuclid.elements.support.attributes.AttributesHelper;

import org.w3c.dom.mathml.MathMLSpaceElement;

/**
 * This class presents a mspace.
 * 
 * @author Unknown
 * @author Max Berger
 * @version $Revision$
 */
public class Mspace extends AbstractJEuclidElement implements
        MathMLSpaceElement {

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "mspace";

    /** Attribute for width. */
    public static final String ATTR_WIDTH = "width";

    /** Attribute for height. */
    public static final String ATTR_HEIGHT = "height";

    /** Attribute for depth. */
    public static final String ATTR_DEPTH = "depth";

    /**
     * Creates a math element.
     * 
     * @param base
     *            The base for the math element tree.
     */
    public Mspace(final MathBase base) {
        super(base);
        this.setDefaultMathAttribute(Mspace.ATTR_DEPTH, MathBase.VALUE_ZERO);
        this.setDefaultMathAttribute(Mspace.ATTR_HEIGHT, MathBase.VALUE_ZERO);
        this.setDefaultMathAttribute(Mspace.ATTR_WIDTH, MathBase.VALUE_ZERO);
    }

    /**
     * @return Space width
     */
    public String getWidth() {
        return this.getMathAttribute(Mspace.ATTR_WIDTH);
    }

    /**
     * @param width
     *            Space width
     */
    public void setWidth(final String width) {
        this.setAttribute(Mspace.ATTR_WIDTH, width);
    }

    /**
     * @return Space height
     */
    public String getHeight() {
        return this.getMathAttribute(Mspace.ATTR_HEIGHT);
    }

    /**
     * @param height
     *            Space height
     */
    public void setHeight(final String height) {
        this.setAttribute(Mspace.ATTR_HEIGHT, height);
    }

    /**
     * @return Space depth
     */
    public String getDepth() {
        return this.getMathAttribute(Mspace.ATTR_DEPTH);
    }

    /**
     * @param depth
     *            Space depth
     */
    public void setDepth(final String depth) {
        this.setAttribute(Mspace.ATTR_DEPTH, depth);
    }

    /**
     * Paints this element.
     * 
     * @param g
     *            The graphics context to use for painting
     * @param posX
     *            The first left position for painting
     * @param posY
     *            The position of the baseline
     */
    @Override
    public void paint(final Graphics2D g, final float posX, final float posY) {
        super.paint(g, posX, posY);
    }

    /** {@inheritDoc} */
    @Override
    public float calculateWidth(final Graphics2D g) {
        return AttributesHelper.convertSizeToPt(this.getWidth(), this,
                AttributesHelper.PT);
    }

    /** {@inheritDoc} */
    @Override
    public float calculateAscentHeight(final Graphics2D g) {
        return AttributesHelper.convertSizeToPt(this.getHeight(), this,
                AttributesHelper.PT);
    }

    /** {@inheritDoc} */
    @Override
    public float calculateDescentHeight(final Graphics2D g) {
        return AttributesHelper.convertSizeToPt(this.getDepth(), this,
                AttributesHelper.PT);
    }

    /** {@inheritDoc} */
    public String getTagName() {
        return Mspace.ELEMENT;
    }
}
