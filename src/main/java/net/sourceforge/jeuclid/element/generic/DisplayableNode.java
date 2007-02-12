/*
 * Copyright 2002 - 2006 JEuclid, http://jeuclid.sf.net
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

/* $Id: DisplayableNode.java,v 1.1.2.2 2007/02/10 22:57:22 maxberger Exp $ */

package net.sourceforge.jeuclid.element.generic;

import java.awt.Graphics2D;

/**
 * Any node that can be displayed on screen. This is the start of a generic
 * interface that should support all types of Nodes.
 * 
 * @author Max Berger
 */
public interface DisplayableNode {

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
    void paint(Graphics2D g, int posX, int posY);

    /**
     * Returns the current height of the upper part of this component from the
     * baseline.
     * 
     * @return Height of the upper part
     * @param g
     *            Graphics2D context to use.
     */
    int getAscentHeight(Graphics2D g);

    /**
     * Returns the current height of the lower part of this component from the
     * baseline.
     * 
     * @return Height of the lower part.
     * @param g
     *            Graphics2D context to use.
     */
    int getDescentHeight(Graphics2D g);

    /**
     * Return the current height of this element.
     * 
     * @return Height of this element
     * @param g
     *            Graphics2D context to use.
     */
    int getHeight(Graphics2D g);

    /**
     * Returns the current width of this element.
     * 
     * @return Width of this element.
     * @param g
     *            Graphics2D context to use.
     */
    int getWidth(Graphics2D g);

    /**
     * Returns the last X position this node was painted on. May return -1 if
     * the node was not painted recently.
     * 
     * @return the x position.
     */
    int getPaintedPosX();

    /**
     * Returns the last Y position this node was painted on. May return -1 if
     * the node was not painted recently.
     * 
     * @return the y position.
     */
    int getPaintedPosY();

}
