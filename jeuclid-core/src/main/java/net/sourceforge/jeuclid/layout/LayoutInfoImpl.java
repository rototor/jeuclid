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

import java.util.ArrayList;
import java.util.List;

/**
 * @version $Revision$
 */
public class LayoutInfoImpl implements LayoutInfo {

    private LayoutStage layoutStage;

    private float ascentHeightS1;

    private float ascentHeightS2;

    private float descentHeightS1;

    private float descentHeightS2;

    private float horizontalS1;

    private float horizontalS2;

    private float widthS1;

    private float widthS2;

    private float posXS1;

    private float posXS2;

    private float posYS1;

    private float posYS2;

    private float stretchAscent = -1.0f;

    private float stretchDescent = -1.0f;

    private float stretchWidth = -1.0f;

    private final List<GraphicsObject> graphicObjects;

    /**
     * Default Constructor.
     */
    public LayoutInfoImpl() {
        this.layoutStage = LayoutStage.NONE;
        this.graphicObjects = new ArrayList<GraphicsObject>();
    }

    /** {@inheritDoc} */
    public LayoutStage getLayoutStage() {
        return this.layoutStage;
    }

    /** {@inheritDoc} */
    public void setLayoutStage(final LayoutStage newStage) {
        this.layoutStage = newStage;
    }

    /** {@inheritDoc} */
    public float getAscentHeight(final LayoutStage stage) {
        if (LayoutStage.STAGE1.equals(stage)) {
            return this.ascentHeightS1;
        } else {
            return this.ascentHeightS2;
        }
    }

    /** {@inheritDoc} */
    public float getDescentHeight(final LayoutStage stage) {
        if (LayoutStage.STAGE1.equals(stage)) {
            return this.descentHeightS1;
        } else {
            return this.descentHeightS2;
        }
    }

    /** {@inheritDoc} */
    public float getHorizontalCenterOffset(final LayoutStage stage) {
        if (LayoutStage.STAGE1.equals(stage)) {
            return this.horizontalS1;
        } else {
            return this.horizontalS2;
        }
    }

    /** {@inheritDoc} */
    public float getPosX(final LayoutStage stage) {
        if (LayoutStage.STAGE1.equals(stage)) {
            return this.posXS1;
        } else {
            return this.posXS2;
        }
    }

    /** {@inheritDoc} */
    public float getPosY(final LayoutStage stage) {
        if (LayoutStage.STAGE1.equals(stage)) {
            return this.posYS1;
        } else {
            return this.posYS2;
        }
    }

    /** {@inheritDoc} */
    public float getWidth(final LayoutStage stage) {
        if (LayoutStage.STAGE1.equals(stage)) {
            return this.widthS1;
        } else {
            return this.widthS2;
        }
    }

    /** {@inheritDoc} */
    public void moveTo(final float x, final float y, final LayoutStage stage) {
        this.posXS2 = x;
        this.posYS2 = y;
        if (LayoutStage.STAGE1.equals(stage)) {
            this.posXS1 = x;
            this.posYS1 = y;
        }

    }

    /** {@inheritDoc} */
    public void setAscentHeight(final float ascentHeight,
            final LayoutStage stage) {
        this.ascentHeightS2 = ascentHeight;
        if (LayoutStage.STAGE1.equals(stage)) {
            this.ascentHeightS1 = ascentHeight;
        }
    }

    /** {@inheritDoc} */
    public void setDescentHeight(final float descentHeight,
            final LayoutStage stage) {
        this.descentHeightS2 = descentHeight;
        if (LayoutStage.STAGE1.equals(stage)) {
            this.descentHeightS1 = descentHeight;
        }
    }

    /** {@inheritDoc} */
    public void setHorizontalCenterOffset(final float newOffset,
            final LayoutStage stage) {
        this.horizontalS2 = newOffset;
        if (LayoutStage.STAGE1.equals(stage)) {
            this.horizontalS1 = newOffset;
        }
    }

    /** {@inheritDoc} */
    public void setWidth(final float width, final LayoutStage stage) {
        this.widthS2 = width;
        if (LayoutStage.STAGE1.equals(stage)) {
            this.widthS1 = width;
        }
    }

    /** {@inheritDoc} */
    public void setGraphicsObject(final GraphicsObject graphicsObject) {
        this.graphicObjects.clear();
        this.graphicObjects.add(graphicsObject);
    }

    /** {@inheritDoc} */
    public List<GraphicsObject> getGraphicObjects() {
        return this.graphicObjects;
    }

    /** {@inheritDoc} */
    public float getStretchWidth() {
        return this.stretchWidth;
    }

    /** {@inheritDoc} */
    public void setStretchWidth(final float newStretchWidth) {
        this.stretchWidth = newStretchWidth;
    }

    /** {@inheritDoc} */
    public float getStretchAscent() {
        if (this.stretchAscent < 0.0f) {
            return this.ascentHeightS1;
        } else {
            return this.stretchAscent;
        }
    }

    /** {@inheritDoc} */
    public float getStretchDescent() {
        if (this.stretchDescent < 0.0f) {
            return this.descentHeightS1;
        } else {
            return this.stretchDescent;
        }
    }

    /** {@inheritDoc} */
    public void setStretchAscent(final float newStretchAscent) {
        this.stretchAscent = newStretchAscent;

    }

    /** {@inheritDoc} */
    public void setStretchDescent(final float newStretchDescent) {
        this.stretchDescent = newStretchDescent;
    }

    /** {@inheritDoc} */
    public void shiftVertically(final float offsetY, final LayoutStage stage) {
        this.posYS2 += offsetY;
        if (LayoutStage.STAGE1.equals(stage)) {
            this.posYS1 += offsetY;
        }
    }
}
