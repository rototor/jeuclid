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

package net.sourceforge.jeuclid.element.helpers;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.jeuclid.element.generic.DisplayableNode;
import net.sourceforge.jeuclid.element.generic.MathElement;

import org.w3c.dom.Node;

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
     * Creates a list of children for the given MathElement.
     * 
     * @param parent
     *            the parent element.
     * @return list of Children.
     */
    public static List<MathElement> createListOfChildren(final Node parent) {
        final org.w3c.dom.NodeList childList = parent.getChildNodes();
        final int len = childList.getLength();
        final List<MathElement> children = new ArrayList<MathElement>(len);
        for (int i = 0; i < len; i++) {
            children.add((MathElement) childList.item(i));
        }
        return children;

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
            final List<MathElement> elements) {
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
            final List<MathElement> elements) {
        int height = 0;
        for (final DisplayableNode element : elements) {
            height = Math.max(height, element.getDescentHeight(g));
        }
        return height;
    }

    /**
     * Retrieve the total height of the list.
     * 
     * @param g
     *            Graphics2D context to use.
     * @param elements
     *            List of elements.
     * @return thetotal height.
     */
    public static int getHeight(final Graphics2D g,
            final List<MathElement> elements) {
        return ElementListSupport.getAscentHeight(g, elements)
                + ElementListSupport.getDescentHeight(g, elements);
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
            final List<MathElement> elements) {
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
            final int posY, final List<MathElement> elements) {
        int pos = posX;
        for (final DisplayableNode element : elements) {
            element.paint(g, pos, posY);
            pos += element.getWidth(g);
        }
    }

}
