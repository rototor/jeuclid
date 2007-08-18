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

package net.sourceforge.jeuclid.layout;

import java.awt.Graphics2D;

/**
 * @author Max Berger
 * @version $Revision$
 */
public interface LayoutNode {

    /**
     * Reset cached values for size.
     */
    void resetCache();

    /**
     * Returns the current height of the upper part of this component from the
     * baseline.
     * 
     * @return Height of the upper part
     */
    float getAscentHeight();

    /**
     * Returns the current height of the lower part of this component from the
     * baseline.
     * 
     * @return Height of the lower part.
     */
    float getDescentHeight();

    /**
     * Returns the current width of this element.
     * 
     * @return Width of this element.
     */
    float getWidth();

    /**
     * Retrieve the X-position of the horizontal center of the content. In
     * most cases, this will be width / 2. This does not, however, take extra
     * borders into account. An element may have different border width on
     * left and right, in which case the center will be moved.
     * 
     * @return X-position of the center of the content
     */
    float getHorizontalCenterOffset();

    /**
     * Retrieve the X position of this element relative to its parent.
     * 
     * @return X position
     */
    float getPosX();

    /**
     * Retrieve the Y position of this element relative to its parent.
     * 
     * @return Y position
     */
    float getPosY();

    /**
     * Move this element to the given position relative to its parent.
     * 
     * @param x
     *            new X position
     * @param y
     *            new Y position
     */
    void moveTo(float x, float y);

    /**
     * Paint this element.
     * 
     * @param parentX
     *            absolute X position of parent element.
     * @param parentY
     *            absolute Y position of parent element.
     * @param g2d
     *            Graphics context. Must be compatible to the Graphics context
     *            used during layout, but not necessarily the same.
     */
    void paint(float parentX, float parentY, Graphics2D g2d);
}
