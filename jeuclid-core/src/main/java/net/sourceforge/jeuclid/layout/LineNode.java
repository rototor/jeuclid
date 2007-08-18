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

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;

import net.sourceforge.jeuclid.LayoutContext;

/**
 * @author Max Berger
 * @version $Revision$
 */
public class LineNode extends AbstractBorderlessNode {

    private final float xo;

    private final float yo;

    private final float linewidth;

    /**
     * Default Constructor.
     * 
     * @param context
     *            Layout Context.
     * @param lineThickness
     *            Line thickness.
     * @param height
     *            y-offset from origin. (must be positive)
     * @param width
     *            x-offset from origin. (must be positive)
     */
    public LineNode(final float width, final float height,
            final float lineThickness, final LayoutContext context) {
        super(context);
        this.xo = width;
        this.yo = height;
        this.linewidth = lineThickness;

    }

    /** {@inheritDoc} */
    @Override
    protected void actuallyPaint(final float absoluteX,
            final float absoluteY, final Graphics2D g) {

        final Stroke oldStroke = g.getStroke();
        g.setStroke(new BasicStroke(this.linewidth));
        g.draw(new Line2D.Float(absoluteX, absoluteY, absoluteX + this.xo,
                absoluteY + this.yo));
        g.setStroke(oldStroke);
    }

    /** {@inheritDoc} */
    public float getAscentHeight() {
        return this.linewidth / 2.0f;
    }

    /** {@inheritDoc} */
    public float getDescentHeight() {
        return this.yo + this.linewidth / 2.0f;
    }

    /** {@inheritDoc} */
    public float getWidth() {
        return this.xo + this.linewidth / 2.0f;
    }

    /** {@inheritDoc} */
    public void resetCache() {
        // nothing to do.
    }
}
