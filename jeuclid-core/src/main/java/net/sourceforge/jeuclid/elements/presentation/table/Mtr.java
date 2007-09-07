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

import net.sourceforge.jeuclid.elements.AbstractJEuclidElement;

import org.w3c.dom.mathml.MathMLNodeList;
import org.w3c.dom.mathml.MathMLTableCellElement;
import org.w3c.dom.mathml.MathMLTableRowElement;

/**
 * This class presents a row in MathTable.
 * 
 * @author Unknown
 * @author Max Berger
 * @version $Revision$
 */
public class Mtr extends AbstractJEuclidElement implements
        MathMLTableRowElement {

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "mtr";

    /**
     * Creates a math element.
     */
    public Mtr() {
        super();
        this.setDefaultMathAttribute(Mtable.ATTR_GROUPALIGN, "");
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
     * @return Alifnment of the row.
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

    // /**
    // * Paints this element.
    // *
    // * @param g
    // * The graphics context to use for painting.
    // * @param posX
    // * The first left position for painting.
    // * @param posY
    // * The position of the baseline.
    // */
    // @Override
    // public void paint(final Graphics2D g, final float posX, final float
    // posY) {
    // super.paint(g, posX, posY);
    //
    // final float columnwidth = this.getMaxColumnWidth(g);
    // float pos = posX;
    //
    // for (int i = 0; i < this.getMathElementCount(); i++) {
    // this.getMathElement(i).paint(g, pos, posY);
    // pos += columnwidth;
    // }
    // }
    //
    // /**
    // * Returns the maximal width of a column for all columns in this row.
    // *
    // * @return Max column width.
    // * @param g
    // * Graphics2D context to use.
    // */
    // protected float getMaxColumnWidth(final Graphics2D g) {
    // float width = 0;
    //
    // for (int i = 0; i < this.getMathElementCount(); i++) {
    // width = Math.max(width, this.getMathElement(i).getWidth(g));
    // }
    // return width;
    // }
    //
    // /**
    // * Return the current width of this element.
    // *
    // * @return Width of this element
    // * @param g
    // * Graphics2D context to use.
    // */
    // @Override
    // public float calculateWidth(final Graphics2D g) {
    // return this.getMaxColumnWidth(g) * this.getMathElementCount();
    // }
    //
    // /**
    // * Returns the current height of the upper part of this component from
    // the
    // * baseline.
    // *
    // * @return Height of the upper part.
    // * @param g
    // * Graphics2D context to use.
    // */
    // @Override
    // public float calculateAscentHeight(final Graphics2D g) {
    // float height = 0;
    //
    // for (int i = 0; i < this.getMathElementCount(); i++) {
    // height = Math.max(height, this.getMathElement(i).getAscentHeight(
    // g));
    // }
    // return height;
    // }
    //
    // /**
    // * Returns the current height of the lower part of this component from
    // the
    // * baseline.
    // *
    // * @return Height of the lower part.
    // * @param g
    // * Graphics2D context to use.
    // */
    // @Override
    // public float calculateDescentHeight(final Graphics2D g) {
    // float height = 0;
    //
    // for (int i = 0; i < this.getMathElementCount(); i++) {
    // height = Math.max(height, this.getMathElement(i)
    // .getDescentHeight(g));
    // }
    // return height;
    // }

    /** {@inheritDoc} */
    public String getTagName() {
        return Mtr.ELEMENT;
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
    public MathMLTableCellElement setCell(
            final MathMLTableCellElement newCell, final int index) {
        throw new UnsupportedOperationException();
    }
}
