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

package net.sourceforge.jeuclid.elements.support;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.jeuclid.LayoutContext;
import net.sourceforge.jeuclid.dom.ChangeTrackingInterface;
import net.sourceforge.jeuclid.elements.DisplayableNode;
import net.sourceforge.jeuclid.elements.JEuclidElement;
import net.sourceforge.jeuclid.elements.JEuclidNode;
import net.sourceforge.jeuclid.layout.LayoutNode;

import org.w3c.dom.Node;

/**
 * Class to support Lists of MathElements.
 * <p>
 * This class can be used by all elements that have some kinf of a list of
 * children that they need to handle in a row-like manner.
 * 
 * @author Max Berger
 * @version $Revision$
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
    public static List<JEuclidElement> createListOfChildren(final Node parent) {
        final org.w3c.dom.NodeList childList = parent.getChildNodes();
        final int len = childList.getLength();
        final List<JEuclidElement> children = new ArrayList<JEuclidElement>(
                len);
        for (int i = 0; i < len; i++) {
            final Node child = childList.item(i);
            if (child instanceof JEuclidElement) {
                children.add((JEuclidElement) child);
            }
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
    public static float getAscentHeight(final Graphics2D g,
            final List<JEuclidElement> elements) {
        float height = 0;
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
    public static float getDescentHeight(final Graphics2D g,
            final List<JEuclidElement> elements) {
        float height = 0;
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
    public static float getHeight(final Graphics2D g,
            final List<JEuclidElement> elements) {
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
    public static float getWidth(final Graphics2D g,
            final List<JEuclidElement> elements) {
        float width = 0;
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
    public static void paint(final Graphics2D g, final float posX,
            final float posY, final List<JEuclidElement> elements) {
        float pos = posX;
        for (final DisplayableNode element : elements) {
            element.paint(g, pos, posY);
            pos += element.getWidth(g);
        }
    }

    /**
     * Calls {@link ChangeTrackingInterface#fireChangeForSubTree()} on all the
     * given elements.
     * 
     * @param elements
     *            a list of elements to fire the change on.
     */
    public static void fireChangeForSubTree(
            final List<JEuclidElement> elements) {
        for (final ChangeTrackingInterface element : elements) {
            element.fireChangeForSubTree();
        }

    }

    /**
     * Layout a list of children.
     * 
     * @param g
     *            Graphics context.
     * @param listOfDataChildren
     *            List of data children.
     * @param listOfLayoutChildren
     *            List of layout children children. It is not cleared
     *            automatically.
     * @param context
     *            layout context
     */
    public static void layoutPhase1(final Graphics2D g,
            final List<JEuclidElement> listOfDataChildren,
            final List<LayoutNode> listOfLayoutChildren,
            final LayoutContext context) {
        for (final JEuclidNode element : listOfDataChildren) {
            listOfLayoutChildren.add(element.layout(g));
        }
    }

    /**
     * Layout a list of children.
     * 
     * @param g
     *            Graphics context.
     * @param children
     *            List of children.
     * @param context
     *            layout context
     */
    public static void layoutPhase2(final Graphics2D g,
            final List<LayoutNode> children, final LayoutContext context) {
        // TODO: Phase 2 Layout
    }

    /**
     * Layout a list of children.
     * 
     * @param g
     *            Graphics context.
     * @param children
     *            List of children.
     * @param context
     *            layout context
     */
    public static void positionChildrenSequentially(final Graphics2D g,
            final List<LayoutNode> children, final LayoutContext context) {
        float posX = 0.0f;
        for (final LayoutNode node : children) {
            node.moveTo(posX, 0);
            posX += node.getWidth();
        }
    }

}
