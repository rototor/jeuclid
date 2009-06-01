/*
 * Copyright 2008 - 2009 JEuclid, http://jeuclid.sf.net
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

package net.sourceforge.jeuclid.test.testsuite;

import java.io.Serializable;

/**
 * Storable Render Information, to compare layout.
 * 
 * @version $Revision$
 */
public class RenderInfo implements Serializable {
    private static final float FUZZYNESS = 0.1f;

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private final String elementName;

    private final float ascent;

    private final float descent;

    private final float width;

    private final float posx;

    private final float posy;

    /**
     * Default constructor.
     * 
     * @param element
     *            Name of the element
     * @param asc
     *            Ascent height
     * @param desc
     *            Descent height
     * @param wid
     *            Width
     * @param x
     *            X-Position
     * @param y
     *            Y-Position.
     */
    public RenderInfo(final String element, final float asc, final float desc,
            final float wid, final float x, final float y) {
        super();
        if (element == null) {
            this.elementName = "";
        } else {
            this.elementName = element;
        }
        this.ascent = asc;
        this.descent = desc;
        this.width = wid;
        this.posx = x;
        this.posy = y;
    }

    /**
     * Checks if this render information is similar enough (within
     * {@link #FUZZYNESS} to another RenderInfo object.
     * 
     * @param other
     *            RenderInfo to compare to.
     * @return true if both RenderInfos are similar.
     */
    public String checkSimilar(final RenderInfo other) {
        final StringBuilder b = new StringBuilder();
        if (!this.elementName.equals(other.elementName)) {
            b.append(" name was: " + other.elementName);
        }
        if (!this.isClose(this.ascent, other.ascent)) {
            b.append(" ascent " + this.ascent + " vs. " + other.ascent);
        }
        if (!this.isClose(this.descent, other.descent)) {
            b.append(" descent " + this.descent + " vs. " + other.descent);
        }
        if (!this.isClose(this.width, other.width)) {
            b.append(" width " + this.width + " vs. " + other.width);
        }
        if (!this.isClose(this.posx, other.posx)) {
            b.append(" posx " + this.posx + " vs. " + other.posx);
        }
        if (!this.isClose(this.posy, other.posy)) {
            b.append(" posy " + this.posy + " vs. " + other.posy);
        }
        return b.toString();
    }

    private boolean isClose(final float should, final float is) {
        final float maxdelta = Math.max(
                Math.abs(should * RenderInfo.FUZZYNESS), 0.1f);
        return Math.abs(should - is) <= maxdelta;
    }

    /**
     * @return the Element Name.
     */
    public String getElementName() {
        return this.elementName;
    }

}
