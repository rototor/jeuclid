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

package net.sourceforge.jeuclid.element;

import java.awt.Graphics2D;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.element.generic.AbstractMathElement;

/**
 * This class presents a row in MathTable.
 * 
 */

public class MathTableRow extends AbstractMathElement {

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "mtr";

    /** Attribute for rowalign. */
    public static final String ATTR_ROWALIGN = "rowalign";

    /** Attribute for columnalign. */
    public static final String ATTR_COLUMNALIGN = "columnalign";

    /** Attribute for groupalign. */
    public static final String ATTR_GROUPALIGN = "groupalign";

    /**
     * Creates a math element.
     * 
     * @param base
     *            The base for the math element tree.
     */
    public MathTableRow(final MathBase base) {
        super(base);
    }

    /**
     * Gets row alignment.
     * 
     * @return Alignment of the row.
     */
    public String getRowalign() {
        return this.getMathAttribute(MathTableRow.ATTR_ROWALIGN);
    }

    /**
     * Sets row alignment.
     * 
     * @param rowalign
     *            Value of row alignment.
     */
    public void setRowalign(final String rowalign) {
        this.setAttribute(MathTableRow.ATTR_ROWALIGN, rowalign);
    }

    /**
     * Gets alignment for group in column. (not implemented yet).
     * 
     * @return Alignment for group in column.
     */
    public String getColumnalign() {
        return this.getMathAttribute(MathTableRow.ATTR_COLUMNALIGN);
    }

    /**
     * Sets alignment for group in column. (not implemented yet).
     * 
     * @param columnalign
     *            Alignment for group in column.
     */
    public void setColumnalign(final String columnalign) {
        this.setAttribute(MathTableRow.ATTR_COLUMNALIGN, columnalign);
    }

    /**
     * Gets alignment of the group for the row. (not implemented yet).
     * 
     * @return Alifnment of the row.
     */
    public String getGroupalign() {
        return this.getMathAttribute(ATTR_GROUPALIGN);
    }

    /**
     * Sets alignment of the group for the row. (not implemented yet).
     * 
     * @param groupalign
     *            Alignment.
     */
    public void setGroupalign(final String groupalign) {
        this.setAttribute(ATTR_GROUPALIGN, groupalign);
    }

    /**
     * Paints this element.
     * 
     * @param g
     *            The graphics context to use for painting.
     * @param posX
     *            The first left position for painting.
     * @param posY
     *            The position of the baseline.
     */
    @Override
    public void paint(final Graphics2D g, final int posX, final int posY) {
        super.paint(g, posX, posY);

        final int columnwidth = this.getMaxColumnWidth(g);
        int pos = posX;

        for (int i = 0; i < this.getMathElementCount(); i++) {
            this.getMathElement(i).paint(g, pos, posY);
            pos += columnwidth;
        }
    }

    /**
     * Returns the maximal width of a column for all columns in this row.
     * 
     * @return Max column width.
     * @param g
     *            Graphics2D context to use.
     */
    protected int getMaxColumnWidth(final Graphics2D g) {
        int width = 0;

        for (int i = 0; i < this.getMathElementCount(); i++) {
            width = Math.max(width, this.getMathElement(i).getWidth(g));
        }
        return width;
    }

    /**
     * Return the current width of this element.
     * 
     * @return Width of this element
     * @param g
     *            Graphics2D context to use.
     */
    @Override
    public int calculateWidth(final Graphics2D g) {
        return this.getMaxColumnWidth(g) * this.getMathElementCount();
    }

    /**
     * Returns the current height of the upper part of this component from the
     * baseline.
     * 
     * @return Height of the upper part.
     * @param g
     *            Graphics2D context to use.
     */
    @Override
    public int calculateAscentHeight(final Graphics2D g) {
        int height = 0;

        for (int i = 0; i < this.getMathElementCount(); i++) {
            height = Math.max(height, this.getMathElement(i).getAscentHeight(
                    g));
        }
        return height;
    }

    /**
     * Returns the current height of the lower part of this component from the
     * baseline.
     * 
     * @return Height of the lower part.
     * @param g
     *            Graphics2D context to use.
     */
    @Override
    public int calculateDescentHeight(final Graphics2D g) {
        int height = 0;

        for (int i = 0; i < this.getMathElementCount(); i++) {
            height = Math.max(height, this.getMathElement(i)
                    .getDescentHeight(g));
        }
        return height;
    }

    /**
     * Gets alignments of the align group.
     * 
     * @return Array with group alignments values.
     */
    public int[] getGroupAlignArray() {
        return MathTable.createGroupAlignValues(this.getGroupalign());
    }

    /** {@inheritDoc} */
    public String getTagName() {
        return MathTableRow.ELEMENT;
    }
}
