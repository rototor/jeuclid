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

import java.awt.Graphics2D;

/**
 * @version $Revision$
 */
public interface LayoutView {

    /**
     * Retrieve the Info object for a given child.
     * 
     * @param child
     *            the node
     * @return an LayoutInfo object.
     */
    LayoutInfo getInfo(LayoutableNode child);

    /**
     * @return the Layout Graphics Context.
     */
    Graphics2D getGraphics();

}
