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
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;

/**
 * @version $Revision$
 */
public class LineObject implements GraphicsObject {

    private final float x1;

    private final float y1;

    private final float x2;

    private final float y2;

    private final float width;

    private final Color col;

    private final boolean dash;

    /**
     * Default Constructor.
     * 
     * @param color
     *            Color of the line.
     * @param lineWidth
     *            StrokeWidth of the line.
     * @param offsetY
     *            Y Offset from baseline.
     * @param offsetX
     *            X Offset from left.
     * @param offsetY2
     *            Y2 Offset from baseline.
     * @param offsetX2
     *            X2 Offset from left.
     */
    public LineObject(final float offsetX, final float offsetY,
            final float offsetX2, final float offsetY2, final float lineWidth,
            final Color color) {
        this.x1 = offsetX;
        this.y1 = offsetY;
        this.x2 = offsetX2;
        this.y2 = offsetY2;
        this.width = lineWidth;
        this.col = color;
        this.dash = false;
    }

    /**
     * Default Constructor.
     * 
     * @param color
     *            Color of the line.
     * @param lineWidth
     *            StrokeWidth of the line.
     * @param offsetY
     *            Y Offset from baseline.
     * @param offsetX
     *            X Offset from left.
     * @param offsetY2
     *            Y2 Offset from baseline.
     * @param offsetX2
     *            X2 Offset from left.
     * @param dashed
     *            if true line is dashed instead of solid.
     */
    public LineObject(final float offsetX, final float offsetY,
            final float offsetX2, final float offsetY2, final float lineWidth,
            final Color color, final boolean dashed) {
        this.x1 = offsetX;
        this.y1 = offsetY;
        this.x2 = offsetX2;
        this.y2 = offsetY2;
        this.width = lineWidth;
        this.col = color;
        this.dash = dashed;
    }

    /** {@inheritDoc} */
    public void paint(final float x, final float y, final Graphics2D g) {
        g.setColor(this.col);
        final Stroke oldStroke = g.getStroke();
        if (this.dash) {
            final float dashWidth = 3.0f * this.width;
            g.setStroke(new BasicStroke(this.width, BasicStroke.CAP_SQUARE,
                    BasicStroke.JOIN_BEVEL, this.width, new float[] {
                            dashWidth, dashWidth, }, 0));
        } else {
            g.setStroke(new BasicStroke(this.width));
        }
        g.draw(new Line2D.Float(x + this.x1, y + this.y1, x + this.x2, y
                + this.y2));
        g.setStroke(oldStroke);

    }
}
