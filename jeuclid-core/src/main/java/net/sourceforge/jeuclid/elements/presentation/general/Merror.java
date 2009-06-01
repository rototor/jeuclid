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

package net.sourceforge.jeuclid.elements.presentation.general;

import java.awt.Color;

import net.sourceforge.jeuclid.LayoutContext;
import net.sourceforge.jeuclid.context.StyleAttributeLayoutContext;
import net.sourceforge.jeuclid.elements.presentation.AbstractContainer;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.Node;

/**
 * Represents an merror element.
 * 
 * @version $Revision$
 */
public final class Merror extends AbstractContainer {

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "merror";

    private static final long serialVersionUID = 1L;

    /**
     * Default constructor. Sets MathML Namespace.
     * 
     * @param qname
     *            Qualified name.
     * @param odoc
     *            Owner Document.
     */
    public Merror(final String qname, final AbstractDocument odoc) {
        super(qname, odoc);
    }

    /** {@inheritDoc} */
    @Override
    protected Node newNode() {
        return new Merror(this.nodeName, this.ownerDocument);
    }

    /** {@inheritDoc} */
    @Override
    public LayoutContext applyLocalAttributesToContext(
            final LayoutContext context) {
        return super
                .applyLocalAttributesToContext(new StyleAttributeLayoutContext(
                        context, null, Color.RED));
    }
}
