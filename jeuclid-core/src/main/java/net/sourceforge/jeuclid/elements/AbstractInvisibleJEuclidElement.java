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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.jeuclid.layout.LayoutableNode;

import org.apache.batik.dom.AbstractDocument;

/**
 * Represents a MathElement with no content.
 * 
 * @version $Revision$
 */
public abstract class AbstractInvisibleJEuclidElement extends
        AbstractJEuclidElement {

    /**
     * Default constructor. Sets MathML Namespace.
     * 
     * @param qname
     *            Qualified name.
     * @param odoc
     *            Owner Document.
     */
    public AbstractInvisibleJEuclidElement(final String qname,
            final AbstractDocument odoc) {
        super(qname, odoc);
    }

    /** {@inheritDoc} */
    @Override
    public List<LayoutableNode> getChildrenToLayout() {
        return new ArrayList<LayoutableNode>(0);
    }

    /** {@inheritDoc} */
    @Override
    public List<LayoutableNode> getChildrenToDraw() {
        return new ArrayList<LayoutableNode>(0);
    }

}
