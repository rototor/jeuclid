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

package net.sourceforge.jeuclid.elements.support;

import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.mathml.MathMLNodeList;

/**
 * Implements a MathMLNodeList based on a java.util.List.
 * 
 * @version $Revision$
 */
public class MathMLNodeListImpl implements MathMLNodeList {

    private final List<Node> nodeList;

    /**
     * create a new MathMLNodeList based on a given java list of nodes.
     * 
     * @param list
     *            the list of nodes.
     */
    public MathMLNodeListImpl(final List<Node> list) {
        this.nodeList = list;
    }

    /** {@inheritDoc} */
    public int getLength() {
        return this.nodeList.size();
    }

    /** {@inheritDoc} */
    public Node item(final int arg0) {
        return this.nodeList.get(arg0);
    }

}
