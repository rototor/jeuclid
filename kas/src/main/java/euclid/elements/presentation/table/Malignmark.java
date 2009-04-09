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

package euclid.elements.presentation.table;


import org.w3c.dom.Node;
import org.w3c.dom.mathml.MathMLAlignMarkElement;

import euclid.elements.AbstractInvisibleJEuclidElement;

/**
 * This class represents the malignmark tag.
 * 
 * @version $Revision$
 */
public final class Malignmark extends AbstractInvisibleJEuclidElement
        implements MathMLAlignMarkElement {

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "malignmark";

    /** The edge attribute. */
    public static final String ATTR_EDGE = "edge";

    private static final long serialVersionUID = 1L;

    /**
     * Creates a math element.
     */
    public Malignmark() {
        super();
    }

    /** {@inheritDoc} */
    @Override
    protected Node newNode() {
        return new Malignmark();
    }

    /** {@inheritDoc} */
    public String getEdge() {
        return this.getMathAttribute(Malignmark.ATTR_EDGE);
    }

    /** {@inheritDoc} */
    public void setEdge(final String edge) {
        this.setAttribute(Malignmark.ATTR_EDGE, edge);
    }

}
