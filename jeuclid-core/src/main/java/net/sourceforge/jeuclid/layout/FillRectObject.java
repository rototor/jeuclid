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
import java.awt.geom.Rectangle2D;

/**
 * @version $Revision$
 */
public class FillRectObject implements GraphicsObject {

    private final Color c;

    private final float a;

    private final float d;

    private final float w;

    /**
     * Default Constructor.
     * 
     * @param ascent
     *            Ascent.
     * @param descent
     *            Descent.
     * @param width
     *            Width.
     * @param color
     *            Color.
     */
    public FillRectObject(final Color color, final float ascent,
            final float descent, final float width) {
        this.c = color;
        this.a = ascent;
        this.d = descent;
        this.w = width;
    }

    /** {@inheritDoc} */
    public void paint(final float x, final float y, final Graphics2D g) {
        g.setColor(this.c);
        g.fill(new Rectangle2D.Float(x, y - this.a, this.w, this.a + this.d));
    }
}
