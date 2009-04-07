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

/* $Id: Mphantom.java 783 2008-06-07 14:12:27Z maxberger $ */

package euclid.elements.presentation.general;

import java.util.Collections;
import java.util.List;


import org.w3c.dom.Node;

import euclid.elements.presentation.AbstractContainer;
import euclid.layout.LayoutableNode;

/**
 * This class represents a phantom of a math element. This is used as spacer.
 * 
 * @version $Revision: 783 $
 */
public final class Mphantom extends AbstractContainer {

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "mphantom";

    private static final long serialVersionUID = 1L;

    /**
     * Creates a math element.
     */
    public Mphantom() {
        super();
    }

    /** {@inheritDoc} */
    @Override
    protected Node newNode() {
        return new Mphantom();
    }

    /** {@inheritDoc} */
    @Override
    public List<LayoutableNode> getChildrenToDraw() {
        return Collections.emptyList();
    }
}
