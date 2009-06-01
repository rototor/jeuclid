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

package net.sourceforge.jeuclid.elements.presentation.table;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.mathml.MathMLNodeList;
import org.w3c.dom.mathml.MathMLTableCellElement;
import org.w3c.dom.mathml.MathMLTableRowElement;

/**
 * Abstract class for table rows with and without label.
 * 
 * @version $Revision$
 */
public abstract class AbstractTableRow extends AbstractTableElement implements
        MathMLTableRowElement {

    /**
     * Default constructor. Sets MathML Namespace.
     * 
     * @param qname
     *            Qualified name.
     * @param odoc
     *            Owner Document.
     */
    public AbstractTableRow(final String qname, final AbstractDocument odoc) {
        super(qname, odoc);
        this.setDefaultMathAttribute(Mtable.ATTR_GROUPALIGN, "");
    }

    /** {@inheritDoc} */
    public void deleteCell(final int index) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public MathMLNodeList getCells() {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public MathMLTableCellElement insertCell(
            final MathMLTableCellElement newCell, final int index) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public MathMLTableCellElement insertEmptyCell(final int index) {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public MathMLTableCellElement setCell(final MathMLTableCellElement newCell,
            final int index) {
        throw new UnsupportedOperationException();
    }

}
