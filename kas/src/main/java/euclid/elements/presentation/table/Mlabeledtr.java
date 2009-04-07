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

/* $Id: Mlabeledtr.java 718 2008-04-28 18:46:38Z maxberger $ */

package euclid.elements.presentation.table;

import org.w3c.dom.Node;
import org.w3c.dom.mathml.MathMLElement;
import org.w3c.dom.mathml.MathMLLabeledRowElement;

/**
 * This class represents the mlabeledtr tag.
 * 
 * @todo add proper support for labels. They are currently silently ignored.
 * @version $Revision: 718 $
 */
public final class Mlabeledtr extends AbstractTableRow implements
        MathMLLabeledRowElement {
    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "mlabeledtr";

    private static final long serialVersionUID = 1L;

    /**
     * Creates a math element.
     */
    public Mlabeledtr() {
        super();
    }

    /** {@inheritDoc} */
    @Override
    protected Node newNode() {
        return new Mlabeledtr();
    }

    /** {@inheritDoc} */
    public MathMLElement getLabel() {
        return this.getMathElement(0);
    }

    /** {@inheritDoc} */
    public void setLabel(final MathMLElement label) {
        this.setMathElement(0, label);
    }

}
