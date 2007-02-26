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

import java.awt.Graphics2D;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.element.generic.AbstractMathElement;
import net.sourceforge.jeuclid.element.helpers.AttributesHelper;

import org.w3c.dom.mathml.MathMLSpaceElement;

/**
 * This class presents a mspace.
 * 
 */
public class MathSpace extends AbstractMathElement implements
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

    private static final String VALUE_ZERO = "0";

    /**
     * Creates a math element.
     * 
     * @param base
     *            The base for the math element tree.
     */
    public MathSpace(final MathBase base) {
        super(base);
        this.setDefaultMathAttribute(MathSpace.ATTR_DEPTH,
                MathSpace.VALUE_ZERO);
        this.setDefaultMathAttribute(MathSpace.ATTR_HEIGHT,
                MathSpace.VALUE_ZERO);
        this.setDefaultMathAttribute(MathSpace.ATTR_WIDTH,
                MathSpace.VALUE_ZERO);
    }

    /**
     * @return Space width
     */
    public String getWidth() {
        return this.getMathAttribute(MathSpace.ATTR_WIDTH);
    }

    /**
     * @param width
     *            Space width
     */
    public void setWidth(final String width) {
        this.setAttribute(MathSpace.ATTR_WIDTH, width);
    }

    /**
     * @return Space height
     */
    public String getHeight() {
        return this.getMathAttribute(MathSpace.ATTR_HEIGHT);
    }

    /**
     * @param height
     *            Space height
     */
    public void setHeight(final String height) {
        this.setAttribute(MathSpace.ATTR_HEIGHT, height);
    }

    /**
     * @return Space depth
     */
    public String getDepth() {
        return this.getMathAttribute(MathSpace.ATTR_DEPTH);
    }

    /**
     * @param depth
     *            Space depth
     */
    public void setDepth(final String depth) {
        this.setAttribute(MathSpace.ATTR_DEPTH, depth);
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
    public void paint(final Graphics2D g, final int posX, final int posY) {
        super.paint(g, posX, posY);
    }

    /** {@inheritDoc} */
    @Override
    public int calculateWidth(final Graphics2D g) {
        return AttributesHelper.getPixels(this.getWidth(), this
                .getFontMetrics(g));
    }

    /** {@inheritDoc} */
    @Override
    public int calculateAscentHeight(final Graphics2D g) {
        return AttributesHelper.getPixels(this.getHeight(), this
                .getFontMetrics(g));
    }

    /** {@inheritDoc} */
    @Override
    public int calculateDescentHeight(final Graphics2D g) {
        return AttributesHelper.getPixels(this.getDepth(), this
                .getFontMetrics(g));
    }

    /** {@inheritDoc} */
    public String getTagName() {
        return MathSpace.ELEMENT;
    }
}
