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

import java.util.List;

import net.sourceforge.jeuclid.LayoutContext;
import net.sourceforge.jeuclid.elements.JEuclidNode;

import org.w3c.dom.Node;

/**
 * @version $Revision$
 */
public interface LayoutableNode extends Node, JEuclidNode {

    /**
     * @return List of children to Layout. Normally, all children.
     */
    List<LayoutableNode> getChildrenToLayout();

    /**
     * @return List of children to Draw. Normally, all children.
     */
    List<LayoutableNode> getChildrenToDraw();

    /**
     * context insensitive layout.
     * 
     * @param view
     *            LayoutView to use.
     * @param info
     *            LayoutInfo to manipulate.
     * @param childMinStage
     *            minimum stage of children. Either STAGE1 or STAGE2.
     * @param context
     *            LayoutContext for this element.
     */
    void layoutStage1(LayoutView view, LayoutInfo info,
            LayoutStage childMinStage, LayoutContext context);

    /**
     * context sensitive layout.
     * 
     * @param view
     *            LayoutView to use.
     * @param info
     *            LayoutInfo to manipulate.
     * @param context
     *            LayoutContext for this element.
     */
    void layoutStage2(LayoutView view, LayoutInfo info, LayoutContext context);

}
