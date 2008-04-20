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

package net.sourceforge.jeuclid.elements;

import net.sourceforge.jeuclid.LayoutContext;

/**
 * Generic interface for all MathNodes, including document Element.
 * 
 * @version $Revision$
 */
public interface JEuclidNode {

    /**
     * get the layout context for the given child.
     * 
     * @param childNum
     *            0-based number of the child to check.
     * @param context
     *            external context.
     * @return layout context to use.
     */
    LayoutContext getChildLayoutContext(final int childNum,
            final LayoutContext context);
}
