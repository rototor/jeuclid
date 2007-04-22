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

/* $Id: MathAlignMark.java 136 2007-04-19 13:58:28Z maxberger $ */

package net.sourceforge.jeuclid.elements.presentation.table;

import java.awt.Graphics2D;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.elements.AbstractInvisibleJEuclidElement;

import org.w3c.dom.mathml.MathMLAlignMarkElement;

/**
 * This class represents the malignmark tag.
 * 
 * @author PG
 * @author Max Berger
 * @version $Revision: 108 $
 */
public class Malignmark extends AbstractInvisibleJEuclidElement implements
        MathMLAlignMarkElement {

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "malignmark";

    /** The edge attribute. */
    public static final String ATTR_EDGE = "edge";

    private final int width = 0;

    /**
     * Creates a math element.
     * 
     * @param base
     *            The base for the math element tree.
     */
    public Malignmark(final MathBase base) {
        super(base);
    }

    /** {@inheritDoc} */
    @Override
    public float calculateWidth(final Graphics2D g) {
        return this.width;
    }

    /** {@inheritDoc} */
    public String getTagName() {
        return Malignmark.ELEMENT;
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
