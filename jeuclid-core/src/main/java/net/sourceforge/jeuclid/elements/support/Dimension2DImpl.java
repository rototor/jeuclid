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

package net.sourceforge.jeuclid.elements.support;

import java.awt.geom.Dimension2D;

/**
 * @version $Revision$
 */
public class Dimension2DImpl extends Dimension2D {
    private float width;

    private float height;

    /**
     * Default Constructor.
     * 
     * @param w
     *            new width.
     * @param h
     *            new height.
     */
    public Dimension2DImpl(final float w, final float h) {
        this.width = w;
        this.height = h;
    }

    /** {@inheritDoc} */
    @Override
    public double getHeight() {
        return this.height;
    }

    /** {@inheritDoc} */
    @Override
    public double getWidth() {
        return this.width;

    }

    /** {@inheritDoc} */
    @Override
    public void setSize(final double w, final double h) {
        this.width = (float) w;
        this.height = (float) h;
    }
}
