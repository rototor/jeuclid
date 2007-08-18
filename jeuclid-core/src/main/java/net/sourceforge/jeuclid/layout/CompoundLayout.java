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
import java.util.List;
import java.util.Vector;

import net.sourceforge.jeuclid.LayoutContext;

/**
 * @author Max Berger
 * @version $Revision$
 */
public class CompoundLayout extends AbstractLayoutNode {

    private static final float MORE_THAN_UNSET = -1.0f;

    private static final float UNSET = -2.0f;

    private final List<LayoutNode> children = new Vector<LayoutNode>();

    private float borderLeft;

    private float borderRight;

    private float borderTop;

    private float borderBottom;

    private float width;

    private float ascentHeight;

    private float descentHeight;

    /**
     * Default Constructor.
     * 
     * @param context
     *            LayoutContext for this node
     */
    public CompoundLayout(final LayoutContext context) {
        super(context);
        this.resetCache();
    }

    /** {@inheritDoc} */
    public void resetCache() {
        this.width = CompoundLayout.UNSET;
        this.ascentHeight = CompoundLayout.UNSET;
        this.descentHeight = CompoundLayout.UNSET;
    }

    /** {@inheritDoc} */
    public float getAscentHeight() {
        if (this.ascentHeight < CompoundLayout.MORE_THAN_UNSET) {
            this.ascentHeight = 0.0f;
            for (final LayoutNode child : this.children) {
                this.ascentHeight = Math.max(this.ascentHeight, -child
                        .getPosY()
                        + child.getAscentHeight());
            }
        }
        return this.ascentHeight + this.borderTop;
    }

    /** {@inheritDoc} */
    public float getDescentHeight() {
        if (this.descentHeight < CompoundLayout.MORE_THAN_UNSET) {
            this.descentHeight = 0.0f;
            for (final LayoutNode child : this.children) {
                this.descentHeight = Math.max(this.descentHeight, child
                        .getPosY()
                        + child.getDescentHeight());
            }
        }
        return this.descentHeight + this.borderBottom;
    }

    /** {@inheritDoc} */
    public float getWidth() {
        this.calculateWidth();
        return this.width + this.borderLeft + this.borderRight;
    }

    private void calculateWidth() {
        if (this.width < CompoundLayout.MORE_THAN_UNSET) {
            this.width = 0.0f;
            for (final LayoutNode child : this.children) {
                this.width = Math.max(this.width, child.getPosX()
                        + child.getWidth());
            }
        }
    }

    /** {@inheritDoc} */
    public float getHorizontalCenterOffset() {
        this.calculateWidth();
        return (this.getWidth() / 2.0f) + this.borderLeft;
    }

    /** {@inheritDoc} */
    @Override
    public void actuallyPaint(final float x, final float y,
            final Graphics2D g2d) {
        final float realX = x + this.borderLeft;
        final float realY = y + this.borderTop;
        for (final LayoutNode child : this.children) {
            child.paint(realX, realY, g2d);
        }
    }

    /**
     * Retrieve the list of children. If modified later than construction you
     * must call {@link #resetCache()} after modification.
     * 
     * @return List of children.
     */
    public List<LayoutNode> getListOfChildren() {
        return this.children;
    }

    /**
     * Setter method for borderLeft.
     * 
     * @param newBorderLeft
     *            the borderLeft to set
     */
    public final void setBorderLeft(final float newBorderLeft) {
        this.borderLeft = newBorderLeft;
    }

    /**
     * Setter method for borderRight.
     * 
     * @param newBorderRight
     *            the borderRight to set
     */
    public final void setBorderRight(final float newBorderRight) {
        this.borderRight = newBorderRight;
    }

    /**
     * Setter method for borderTop.
     * 
     * @param newBorderTop
     *            the borderTop to set
     */
    public final void setBorderTop(final float newBorderTop) {
        this.borderTop = newBorderTop;
    }

    /**
     * Setter method for borderBottom.
     * 
     * @param newBorderBottom
     *            the borderBottom to set
     */
    public final void setBorderBottom(final float newBorderBottom) {
        this.borderBottom = newBorderBottom;
    }

}
