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

package net.sourceforge.jeuclid.elements.content.semantic;

import net.sourceforge.jeuclid.elements.AbstractInvisibleJEuclidElement;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.Node;
import org.w3c.dom.mathml.MathMLAnnotationElement;

/**
 * This class represents a annotation element.
 * 
 * @version $Revision$
 */
public final class Annotation extends AbstractInvisibleJEuclidElement implements
        MathMLAnnotationElement {

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "annotation";

    /** The encoding attribute. */
    public static final String ATTR_ENCODING = "encoding";

    private static final long serialVersionUID = 1L;

    /**
     * Default constructor. Sets MathML Namespace.
     * 
     * @param qname
     *            Qualified name.
     * @param odoc
     *            Owner Document.
     */
    public Annotation(final String qname, final AbstractDocument odoc) {
        super(qname, odoc);
    }

    /** {@inheritDoc} */
    @Override
    protected Node newNode() {
        return new Annotation(this.nodeName, this.ownerDocument);
    }

    /** {@inheritDoc} */
    public String getBody() {
        return this.getText();
    }

    /** {@inheritDoc} */
    public String getEncoding() {
        return this.getMathAttribute(Annotation.ATTR_ENCODING);
    }

    /** {@inheritDoc} */
    public void setBody(final String body) {
        this.setTextContent(body);
    }

    /** {@inheritDoc} */
    public void setEncoding(final String encoding) {
        this.setAttribute(Annotation.ATTR_ENCODING, encoding);
    }

}
