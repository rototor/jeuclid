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

/* $Id: JEuclidView.java 795 2008-06-21 10:53:35Z maxberger $ */

package net.sourceforge.jeuclid.test.testsuite;

import java.io.Serializable;

/**
 * Storable Render Information, to compare layout.
 * 
 * @version $Revision: 000 $
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
     * @param elementName
     *            Name of the element
     * @param ascent
     *            Ascent height
     * @param descent
     *            Descent height
     * @param width
     *            Width
     * @param posx
     *            X-Position
     * @param posy
     *            Y-Position.
     */
    public RenderInfo(final String elementName, final float ascent,
            final float descent, final float width, final float posx,
            final float posy) {
        super();
        if (elementName == null) {
            this.elementName = "";
        } else {
            this.elementName = elementName;
        }
        this.ascent = ascent;
        this.descent = descent;
        this.width = width;
        this.posx = posx;
        this.posy = posy;
    }

    /**
     * Checks if this render information is similar enough (within
     * {@link #FUZZYNESS} to another RenderInfo object.
     * 
     * @param other
     *            RenderInfo to compare to.
     * @return true if both RenderInfos are similar.
     */
    public boolean isSimilar(final RenderInfo other) {
        boolean isSim = true;
        isSim &= (this.elementName.equals(other.elementName));
        isSim &= (this.isClose(this.ascent, other.ascent));
        isSim &= (this.isClose(this.descent, other.descent));
        isSim &= (this.isClose(this.width, other.width));
        isSim &= (this.isClose(this.posx, other.posx));
        isSim &= (this.isClose(this.posy, other.posy));
        return isSim;
    }

    private boolean isClose(final float should, final float is) {
        final float maxdelta = Math.max(Math.abs(should
                * RenderInfo.FUZZYNESS), 0.1f);
        return Math.abs(should - is) <= (maxdelta);
    }

    /**
     * @return the Element Name.
     */
    public String getElementName() {
        return this.elementName;
    }

}
