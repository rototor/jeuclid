/*
 * Copyright 2007 - 2007 JEuclid, http://jeuclid.sf.net
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

package net.sourceforge.jeuclid.elements.support.attributes;

import net.sourceforge.jeuclid.layout.LayoutInfo;
import net.sourceforge.jeuclid.layout.LayoutStage;

/**
 * @version $Revision$
 */
public enum HAlign {
    /** Left align. */
    LEFT,
    /** Center align. */
    CENTER,
    /** Right align. */
    RIGHT;
    /** constant for center align. */
    public static final String ALIGN_CENTER = "center";

    /** constant for right align. */
    public static final String ALIGN_RIGHT = "right";

    /**
     * Parse an Alignment String.
     * 
     * @param alignString
     *            String to parse.
     * @param defaultt
     *            default value.
     * @return a HAlign value.
     */
    public static HAlign parseString(final String alignString,
            final HAlign defaultt) {
        final HAlign retVal;
        if (HAlign.ALIGN_CENTER.equalsIgnoreCase(alignString)) {
            retVal = CENTER;
        } else if ("left".equalsIgnoreCase(alignString)) {
            retVal = LEFT;
        } else if (HAlign.ALIGN_RIGHT.equalsIgnoreCase(alignString)) {
            retVal = RIGHT;
        } else {
            retVal = defaultt;
        }
        return retVal;
    }

    /**
     * Retrieve H-Align offset.
     * 
     * @param stage
     *            current Layout Stage
     * @param info
     *            Info object to examine
     * @param width
     *            Total width
     * @return Alignment offset to add to left.
     */
    public float getHAlignOffset(final LayoutStage stage,
            final LayoutInfo info, final float width) {
        final float offset;
        switch (this) {
        case LEFT:
            offset = 0.0f;
            break;
        case RIGHT:
            offset = width - info.getWidth(stage);
            break;
        case CENTER:
            offset = width / 2.0f - info.getHorizontalCenterOffset(stage);
            break;
        default:
            assert false;
            offset = 0.0f;
        }
        return offset;
    }

}
