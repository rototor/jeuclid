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

import net.sourceforge.jeuclid.elements.presentation.general.AbstractRowLike;

import org.w3c.dom.Node;
import org.w3c.dom.mathml.MathMLTableCellElement;

/**
 * This class presents a cell of a table.
 * 
 * @version $Revision$
 */
public final class Mtd extends AbstractRowLike implements
        MathMLTableCellElement {

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "mtd";

    /** attribute for rowspan. */
    private static final String ATTR_ROWSPAN = "rowspan";

    /** attribute for columnspan. */
    private static final String ATTR_COLUMNSPAN = "columnspan";

    private static final String VALUE_ONE = "1";

    private static final long serialVersionUID = 1L;

    /**
     * Creates a math element.
     */
    public Mtd() {
        super();
        this.setDefaultMathAttribute(Mtd.ATTR_ROWSPAN, Mtd.VALUE_ONE);
        this.setDefaultMathAttribute(Mtd.ATTR_COLUMNSPAN, Mtd.VALUE_ONE);
    }

    /** {@inheritDoc} */
    @Override
    protected Node newNode() {
        return new Mtd();
    }

    /**
     * @return Rowspan
     */
    public String getRowspan() {
        return this.getMathAttribute(Mtd.ATTR_ROWSPAN);
    }

    /**
     * @param rowspan
     *            Rowspan
     */
    public void setRowspan(final String rowspan) {
        this.setAttribute(Mtd.ATTR_ROWSPAN, rowspan);
    }

    /**
     * @return Columnspan
     */
    public String getColumnspan() {
        return this.getMathAttribute(Mtd.ATTR_COLUMNSPAN);
    }

    /**
     * @param columnspan
     *            Columnspan
     */
    public void setColumnspan(final String columnspan) {
        this.setAttribute(Mtd.ATTR_COLUMNSPAN, columnspan);
    }

    /**
     * @return Row align
     */
    public String getRowalign() {
        return this.getMathAttribute(Mtable.ATTR_ROWALIGN);
    }

    /**
     * Sets property of the rowalign attribute.
     * 
     * @param rowalign
     *            new value.
     */
    public void setRowalign(final String rowalign) {
        this.setAttribute(Mtable.ATTR_ROWALIGN, rowalign);
    }

    /**
     * @return Column align
     */
    public String getColumnalign() {
        return this.getMathAttribute(Mtable.ATTR_COLUMNALIGN);
    }

    /**
     * Sets property of the columnalign attribute.
     * 
     * @param columnalign
     *            Value
     */
    public void setColumnalign(final String columnalign) {
        this.setAttribute(Mtable.ATTR_COLUMNALIGN, columnalign);
    }

    /**
     * @return Group align
     */
    public String getGroupalign() {
        return this.getMathAttribute(Mtable.ATTR_GROUPALIGN);
    }

    /**
     * Sets property of the groupalign attribute.
     * 
     * @param groupalign
     *            Groupalign
     */
    public void setGroupalign(final String groupalign) {
        this.setAttribute(Mtable.ATTR_GROUPALIGN, groupalign);
    }

    /** {@inheritDoc} */
    public String getCellindex() {
        return Integer.toString(this.getParent().getIndexOfMathElement(this));
    }

    /** {@inheritDoc} */
    public boolean getHasaligngroups() {
        return this.getGroupalign() != null;
    }

}
