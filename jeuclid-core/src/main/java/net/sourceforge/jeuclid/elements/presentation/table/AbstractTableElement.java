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

import net.sourceforge.jeuclid.elements.presentation.AbstractContainer;

import org.apache.batik.dom.AbstractDocument;

/**
 * Common functionality for all Table elements. This class contains support for
 * setting and getting alignment attributes.
 * 
 * @version $Revision$
 */
public abstract class AbstractTableElement extends AbstractContainer {
    /**
     * Default constructor. Sets MathML Namespace.
     * 
     * @param qname
     *            Qualified name.
     * @param odoc
     *            Owner Document.
     */
    public AbstractTableElement(final String qname, final AbstractDocument odoc) {
        super(qname, odoc);
    }

    /**
     * Gets row alignment.
     * 
     * @return Alignment of the row.
     */
    public String getRowalign() {
        return this.getMathAttribute(Mtable.ATTR_ROWALIGN);
    }

    /**
     * Sets row alignment.
     * 
     * @param rowalign
     *            Value of row alignment.
     */
    public void setRowalign(final String rowalign) {
        this.setAttribute(Mtable.ATTR_ROWALIGN, rowalign);
    }

    /**
     * Gets alignment for group in column. (not implemented yet).
     * 
     * @return Alignment for group in column.
     */
    public String getColumnalign() {
        return this.getMathAttribute(Mtable.ATTR_COLUMNALIGN);
    }

    /**
     * Sets alignment for group in column. (not implemented yet).
     * 
     * @param columnalign
     *            Alignment for group in column.
     */
    public void setColumnalign(final String columnalign) {
        this.setAttribute(Mtable.ATTR_COLUMNALIGN, columnalign);
    }

    /**
     * Gets alignment of the group for the row. (not implemented yet).
     * 
     * @return Alignment of the row.
     */
    public String getGroupalign() {
        return this.getMathAttribute(Mtable.ATTR_GROUPALIGN);
    }

    /**
     * Sets alignment of the group for the row. (not implemented yet).
     * 
     * @param groupalign
     *            Alignment.
     */
    public void setGroupalign(final String groupalign) {
        this.setAttribute(Mtable.ATTR_GROUPALIGN, groupalign);
    }

}
