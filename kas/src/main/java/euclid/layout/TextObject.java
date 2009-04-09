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

package euclid.layout;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;

/**
 * @version $Revision$
 */
public class TextObject implements GraphicsObject {

    private final TextLayout layout;
    private final Color color;
    private final float xoffset;
    private final float yoffset;
    private final AffineTransform trans;

    /**
     * Default Constructor.
     * 
     * @param textLayout
     *            Text Layout.
     * @param xo
     *            X-Offset
     * @param textColor
     *            text color.
     */
    public TextObject(final TextLayout textLayout, final float xo, final Color textColor) {
        assert textLayout != null;
        layout = textLayout;
        color = textColor;
        xoffset = xo;
        yoffset = 0.0f;
        trans = null;
    }

    /**
     * Constructor for more complex texts (operators).
     * 
     * @param textLayout
     *            Text Layout.
     * @param textColor
     *            text color.
     * @param xo
     *            X-Offset for drawing.
     * @param yo
     *            Y-Offset for drawing.
     * @param transform
     *            Transformation to apply before drawing.
     */
    public TextObject(final TextLayout textLayout, final float xo,
            final float yo, final AffineTransform transform,
            final Color textColor) {
    	layout = textLayout;
        color = textColor;
        xoffset = xo;
        yoffset = yo;
        trans = transform;
    }

    /** {@inheritDoc} */
    public void paint(final float x, final float y, final Graphics2D g) {
        g.setColor(color);
        final AffineTransform oldTrans = g.getTransform();
        g.translate(x + xoffset, y + yoffset);
        if (trans != null) {
            g.transform(trans);
        }
        layout.draw(g, 0, 0);
        g.setTransform(oldTrans);
    }
}
