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

package net.sourceforge.jeuclid.elements.presentation.enlivening;

import java.util.List;
import java.util.Vector;

import net.sourceforge.jeuclid.elements.AbstractElementWithDelegates;
import net.sourceforge.jeuclid.layout.LayoutableNode;

import org.w3c.dom.mathml.MathMLActionElement;

/**
 * Represents an maction element.
 * 
 * @todo This element does not actually implement any action.
 * @version $Revision$
 */
public class Maction extends AbstractElementWithDelegates implements
        MathMLActionElement {

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "maction";

    private static final String ATTR_ACTIONTYPE = "actiontype";

    private static final String ATTR_SELECTION = "selection";

    /**
     * Creates a math element.
     */
    public Maction() {
        super();
        this.setDefaultMathAttribute(Maction.ATTR_SELECTION, "1");
    }

    /** {@inheritDoc} */
    public String getTagName() {
        return Maction.ELEMENT;
    }

    /** {@inheritDoc} */
    public String getActiontype() {
        return this.getMathAttribute(Maction.ATTR_ACTIONTYPE);
    }

    /** {@inheritDoc} */
    public String getSelection() {
        return this.getMathAttribute(Maction.ATTR_SELECTION);
    }

    /** {@inheritDoc} */
    public void setActiontype(final String actiontype) {
        this.setAttribute(Maction.ATTR_ACTIONTYPE, actiontype);
    }

    /** {@inheritDoc} */
    public void setSelection(final String selection) {
        this.setAttribute(Maction.ATTR_SELECTION, selection);
    }

    /** {@inheritDoc} */
    @Override
    protected List<LayoutableNode> createDelegates() {
        LayoutableNode selectedElement;
        try {
            final int selected = Integer.parseInt(this.getSelection());
            selectedElement = this.getMathElement(selected - 1);
        } catch (final NumberFormatException nfe) {
            selectedElement = null;
        }
        final List<LayoutableNode> list = new Vector<LayoutableNode>(1);
        if (selectedElement != null) {
            list.add(selectedElement);
        }
        return list;
    }

}
