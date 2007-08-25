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
import java.awt.font.TextLayout;

/**
 * @author Max Berger
 * @version $Revision$
 */
public class TextObject implements GraphicsObject {

    private final TextLayout layout;

    private final Color color;

    /**
     * Default Constructor.
     * 
     * @param textLayout
     *            Text Layout.
     * @param textColor
     *            text color;
     */
    public TextObject(final TextLayout textLayout, final Color textColor) {
        this.layout = textLayout;
        this.color = textColor;
    }

    /** {@inheritDoc} */
    public void paint(final float x, final float y, final Graphics2D g) {
        g.setColor(this.color);
        this.layout.draw(g, x, y);
    }

}
