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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import net.sourceforge.jeuclid.LayoutContext;
import net.sourceforge.jeuclid.LayoutContext.Parameter;

/**
 * @author Max Berger
 * @version $Revision$
 */
public abstract class AbstractLayoutNode implements LayoutNode {

    private float posX;

    private float posY;

    private final LayoutContext layoutContext;

    /**
     * Default Constructor.
     * 
     * @param context
     *            LayoutContext for this node
     */
    public AbstractLayoutNode(final LayoutContext context) {
        this.layoutContext = context;
    }

    /** {@inheritDoc} */
    public float getPosX() {
        return this.posX;
    }

    /** {@inheritDoc} */
    public float getPosY() {
        return this.posY;
    }

    /** {@inheritDoc} */
    public void moveTo(final float x, final float y) {
        this.posX = x;
        this.posY = y;
    }

    /** {@inheritDoc} */
    public void paint(final float parentX, final float parentY,
            final Graphics2D g2d) {
        final float myX = parentX + this.posX;
        final float myY = parentY + this.posY;
        // TODO: Paint Background

        final boolean b = (Boolean) this.layoutContext
                .getParameter(Parameter.DEBUG);
        if (b) {
            g2d.setColor(Color.BLUE);
            g2d.draw(new Line2D.Float(myX, myY - this.getAscentHeight(), myX
                    + this.getWidth(), myY - this.getAscentHeight()));
            g2d.draw(new Line2D.Float(myX + this.getWidth(), myY
                    - this.getAscentHeight(), myX + this.getWidth(), myY
                    + this.getDescentHeight()));
            g2d.draw(new Line2D.Float(myX, myY + this.getDescentHeight(), myX
                    + this.getWidth(), myY + this.getDescentHeight()));
            g2d.draw(new Line2D.Float(myX, myY - this.getAscentHeight(), myX,
                    myY + this.getDescentHeight()));
            g2d.setColor(Color.RED);
            g2d.draw(new Line2D.Float(myX, myY, myX + this.getWidth(), myY));

        }
        // TODO: Paint Debug
        g2d.setColor((Color) this.layoutContext
                .getParameter(Parameter.MATHCOLOR));
        this.actuallyPaint(myX, myY, g2d);
    }

    /**
     * Actually paint this element.
     * 
     * @param absoluteX
     *            X position.
     * @param absoluteY
     *            Y position.
     * @param g2d
     *            Graphics context.
     */
    protected abstract void actuallyPaint(float absoluteX, float absoluteY,
            Graphics2D g2d);

    /**
     * Getter method for layoutContext.
     * 
     * @return the layoutContext
     */
    protected final LayoutContext getLayoutContext() {
        return this.layoutContext;
    }

}
