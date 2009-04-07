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

/* $Id: LayoutInfo.java 518 2007-09-14 08:29:58Z maxberger $ */

package euclid.layout;

import java.util.List;

/**
 * @version $Revision: 518 $
 */
public interface LayoutInfo {

    /**
     * The LayoutStage this element represents.
     * 
     * @return current layout stage.
     */
    LayoutStage getLayoutStage();

    /**
     * @param newStage
     *            new Layout Stage.
     */
    void setLayoutStage(LayoutStage newStage);

    /**
     * Returns the current height of the upper part of this component from the
     * baseline.
     * 
     * @return Height of the upper part
     * @param stage
     *            layoutStage to get this information for (either STAGE1 or
     *            STAGE2)
     */
    float getAscentHeight(LayoutStage stage);

    /**
     * Returns the current height of the lower part of this component from the
     * baseline.
     * 
     * @return Height of the lower part.
     * @param stage
     *            layoutStage to get this information for (either STAGE1 or
     *            STAGE2)
     */
    float getDescentHeight(LayoutStage stage);

    /**
     * Returns the current width of this element.
     * 
     * @return Width of this element.
     * @param stage
     *            layoutStage to get this information for (either STAGE1 or
     *            STAGE2)
     */
    float getWidth(LayoutStage stage);

    /**
     * Retrieve the X-position of the horizontal center of the content. In
     * most cases, this will be width / 2. This does not, however, take extra
     * borders into account. An element may have different border width on
     * left and right, in which case the center will be moved.
     * 
     * @return X-position of the center of the content
     * @param stage
     *            layoutStage to get this information for (either STAGE1 or
     *            STAGE2)
     */
    float getHorizontalCenterOffset(LayoutStage stage);

    /**
     * @param newOffset
     *            new horizontal offset.
     * @param stage
     *            layoutStage to get this information for (either STAGE1 or
     *            STAGE2)
     */
    void setHorizontalCenterOffset(float newOffset, LayoutStage stage);

    /**
     * Retrieve the X position of this element relative to its parent.
     * 
     * @return X position
     * @param stage
     *            layoutStage to get this information for (either STAGE1 or
     *            STAGE2)
     */
    float getPosX(LayoutStage stage);

    /**
     * Retrieve the Y position of this element relative to its parent.
     * 
     * @return Y position
     * @param stage
     *            layoutStage to get this information for (either STAGE1 or
     *            STAGE2)
     */
    float getPosY(LayoutStage stage);

    /**
     * Move this element to the given position relative to its parent.
     * 
     * @param x
     *            new X position
     * @param y
     *            new Y position
     * @param stage
     *            layoutStage to get this information for (either STAGE1 or
     *            STAGE2)
     */
    void moveTo(float x, float y, LayoutStage stage);

    /**
     * Shift vertically by given offset.
     * 
     * @param offsetY
     *            offset to shift.
     * @param stage
     *            Stage to manipulate.
     */
    void shiftVertically(float offsetY, LayoutStage stage);

    /**
     * @param ascentHeight
     *            new ascentHeight.
     * @param stage
     *            layoutStage to get this information for (either STAGE1 or
     *            STAGE2)
     */
    void setAscentHeight(float ascentHeight, LayoutStage stage);

    /**
     * @param descentHeight
     *            new descentHeight.
     * @param stage
     *            layoutStage to get this information for (either STAGE1 or
     *            STAGE2)
     */
    void setDescentHeight(float descentHeight, LayoutStage stage);

    /**
     * @param width
     *            new width.
     * @param stage
     *            layoutStage to get this information for (either STAGE1 or
     *            STAGE2)
     */
    void setWidth(float width, LayoutStage stage);

    /**
     * Set the stretch width for children, or < 0 if children should be
     * horizontally unstretched.
     * 
     * @param stretchWidth
     *            new stretch width
     */
    void setStretchWidth(float stretchWidth);

    /**
     * Retrieve the stretch width if set, or STAGE1.width if unset.
     * 
     * @return stretch width.
     */
    float getStretchWidth();

    /**
     * Set the stretch descent for children. Defaults to STAGE1.descent
     * 
     * @param stretchDescent
     *            new stretch descent
     */
    void setStretchDescent(float stretchDescent);

    /**
     * Retrieve the stretch descent if set, or STAGE1.descent if unset.
     * 
     * @return stretch descent.
     */
    float getStretchDescent();

    /**
     * Set the stretch ascent for children. Defaults to STAGE1.ascent
     * 
     * @param stretchAscent
     *            new stretch ascent
     */
    void setStretchAscent(float stretchAscent);

    /**
     * Retrieve the stretch ascent if set, or STAGE1.ascent if unset.
     * 
     * @return stretch ascent.
     */
    float getStretchAscent();

    /**
     * @param graphicsObject
     *            the GraphicsObject to set.
     */
    void setGraphicsObject(GraphicsObject graphicsObject);

    /**
     * @return Graphic objects associated with this node.
     */
    List<GraphicsObject> getGraphicObjects();
}
