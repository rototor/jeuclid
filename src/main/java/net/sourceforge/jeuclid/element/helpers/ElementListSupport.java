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

/* $Id: ElementListSupport.java,v 1.1.2.3 2007/02/10 22:57:23 maxberger Exp $ */

package net.sourceforge.jeuclid.element.helpers;

import java.awt.Graphics2D;
import java.util.List;

import net.sourceforge.jeuclid.element.generic.AbstractMathElement;
import net.sourceforge.jeuclid.element.generic.DisplayableNode;

/**
 * Class to support Lists of MathElements.
 * <p>
 * This class can be used by all elements that have some kinf of a list of
 * children that they need to handle in a row-like manner.
 * 
 * @author Max Berger
 */
public final class ElementListSupport {

    private ElementListSupport() {
        // Utility class.
    }

    /**
     * Retrieve the maximum ascent height of the list.
     * 
     * @param g
     *            Graphics2D context to use.
     * @param elements
     *            List of elements.
     * @return the max ascent height.
     */
    public static int getAscentHeight(final Graphics2D g,
            final List<AbstractMathElement> elements) {
        int height = 0;
        for (final DisplayableNode element : elements) {
            height = Math.max(height, element.getAscentHeight(g));
        }
        return height;
    }

    /**
     * Retrieve the maximum descent height of the list.
     * 
     * @param g
     *            Graphics2D context to use.
     * @param elements
     *            List of elements.
     * @return the max descent height.
     */
    public static int getDescentHeight(final Graphics2D g,
            final List<AbstractMathElement> elements) {
        int height = 0;
        for (final DisplayableNode element : elements) {
            height = Math.max(height, element.getDescentHeight(g));
        }
        return height;
    }

    /**
     * Retrieve the total witdth of the list.
     * 
     * @param g
     *            Graphics2D context to use.
     * @param elements
     *            List of elements.
     * @return the total width.
     */
    public static int getWidth(final Graphics2D g,
            final List<AbstractMathElement> elements) {
        int width = 0;
        for (final DisplayableNode element : elements) {
            width += element.getWidth(g);
        }
        return width;
    }

    /**
     * Paint all elements in a row-like fashion.
     * 
     * @param g
     *            Graphics2D context to use.
     * @param elements
     *            List of elements.
     * @param posX
     *            x-origin to use for painting.
     * @param posY
     *            y-origin to use for painting.
     */
    public static void paint(final Graphics2D g, final int posX,
            final int posY, final List<AbstractMathElement> elements) {
        int pos = posX;
        for (final DisplayableNode element : elements) {
            element.paint(g, pos, posY);
            pos += element.getWidth(g);
        }
    }

}
