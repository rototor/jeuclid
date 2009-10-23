/*
 * Copyright 2007 - 2008 JEuclid, http://jeuclid.sf.net
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

package cViewer;

import java.awt.Component;
import java.awt.Component.BaselineResizeBehavior;

import javax.swing.JComponent;
import javax.swing.SwingConstants;

public class MathComponentUI16 extends MathComponentUI {
    /**
     * Default Constructor.
     */
    public MathComponentUI16() {
        super();
    }

    /** {@inheritDoc} */
    @Override
    public BaselineResizeBehavior getBaselineResizeBehavior(final JComponent c) {
        final BaselineResizeBehavior retVal;
        switch (((JMathComponent) c).getVerticalAlignment()) {
        case SwingConstants.TOP:
            retVal = Component.BaselineResizeBehavior.CONSTANT_ASCENT;
            break;
        case SwingConstants.BOTTOM:
            retVal = Component.BaselineResizeBehavior.CONSTANT_DESCENT;
            break;
        case SwingConstants.CENTER:
            retVal = Component.BaselineResizeBehavior.CENTER_OFFSET;
            break;
        default:
            retVal = Component.BaselineResizeBehavior.OTHER;
        }
        return retVal;
    }
}
