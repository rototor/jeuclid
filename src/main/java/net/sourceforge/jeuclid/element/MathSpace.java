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

/**
 * This class presents a mspace.
 * 
 */
public class MathSpace extends AbstractMathElement {

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "mspace";

    private String m_width = "0";

    private String m_height = "0";

    private String m_depth = "0";

    /**
     * Creates a math element.
     * 
     * @param base
     *            The base for the math element tree.
     */
    public MathSpace(MathBase base) {
        super(base);
    }

    /**
     * @return Space width
     */
    public String getSpaceWidth() {
        return m_width;
    }

    /**
     * @param width
     *            Space width
     */
    public void setSpaceWidth(String width) {
        m_width = width;
    }

    /**
     * @return Space height
     */
    public String getSpaceHeight() {
        return m_height;
    }

    /**
     * @param height
     *            Space height
     */
    public void setSpaceHeight(String height) {
        m_height = height;
    }

    /**
     * @return Space depth
     */
    public String getDepth() {
        return m_depth;
    }

    /**
     * @param depth
     *            Space depth
     */
    public void setDepth(String depth) {
        m_depth = depth;
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
    public void paint(Graphics2D g, int posX, int posY) {
        super.paint(g, posX, posY);
    }

    /** {@inheritDoc} */
    public int calculateWidth(Graphics2D g) {
        return AttributesHelper.getPixels(getSpaceWidth(), getFontMetrics(g));
    }

    /** {@inheritDoc} */
    public int calculateAscentHeight(Graphics2D g) {
        return AttributesHelper.getPixels(getSpaceHeight(), getFontMetrics(g));
    }

    /** {@inheritDoc} */
    public int calculateDescentHeight(Graphics2D g) {
        return AttributesHelper.getPixels(getDepth(), getFontMetrics(g));
    }

    /** {@inheritDoc} */
    public String getTagName() {
        return ELEMENT;
    }
}
