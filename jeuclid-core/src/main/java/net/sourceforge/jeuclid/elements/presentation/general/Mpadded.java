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

package net.sourceforge.jeuclid.elements.presentation.general;

import net.sourceforge.jeuclid.elements.presentation.AbstractContainer;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.Node;
import org.w3c.dom.mathml.MathMLPaddedElement;

/**
 * This class implements the mpadded element.
 * <p>
 * TODO: none of the attributes are actually implemented yet.
 * 
 * @version $Revision$
 */
public final class Mpadded extends AbstractContainer implements
        MathMLPaddedElement {

    /** constant for depth attribute. */
    public static final String ATTR_DEPTH = "depth";

    /** constant for height attribute. */
    public static final String ATTR_HEIGHT = "height";

    /** constant for lspace attribute. */
    public static final String ATTR_LSPACE = "lspace";

    /** constant for width attribute. */
    public static final String ATTR_WIDTH = "width";

    /**
     * The MathML element name for this class.
     */
    public static final String ELEMENT = "mpadded";

    private static final long serialVersionUID = 1L;

    /**
     * Default constructor. Sets MathML Namespace.
     * 
     * @param qname
     *            Qualified name.
     * @param odoc
     *            Owner Document.
     */
    public Mpadded(final String qname, final AbstractDocument odoc) {
        super(qname, odoc);
    }

    /** {@inheritDoc} */
    @Override
    protected Node newNode() {
        return new Mpadded(this.nodeName, this.ownerDocument);
    }

    /** {@inheritDoc} */
    public String getDepth() {
        return this.getMathAttribute(Mpadded.ATTR_DEPTH);
    }

    /** {@inheritDoc} */
    public String getHeight() {
        return this.getMathAttribute(Mpadded.ATTR_HEIGHT);
    }

    /** {@inheritDoc} */
    public String getLspace() {
        return this.getMathAttribute(Mpadded.ATTR_LSPACE);
    }

    /** {@inheritDoc} */
    public String getWidth() {
        return this.getMathAttribute(Mpadded.ATTR_WIDTH);
    }

    /** {@inheritDoc} */
    public void setDepth(final String depth) {
        this.setAttribute(Mpadded.ATTR_DEPTH, depth);
    }

    /** {@inheritDoc} */
    public void setHeight(final String height) {
        this.setAttribute(Mpadded.ATTR_HEIGHT, height);
    }

    /** {@inheritDoc} */
    public void setLspace(final String lspace) {
        this.setAttribute(Mpadded.ATTR_LSPACE, lspace);
    }

    /** {@inheritDoc} */
    public void setWidth(final String width) {
        this.setAttribute(Mpadded.ATTR_WIDTH, width);
    }

}
