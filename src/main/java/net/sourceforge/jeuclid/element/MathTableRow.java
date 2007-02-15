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

    /**  */
    public static final int ALIGN_TOP = 0;

    /**  */
    public static final int ALIGN_BOTTOM = 1;

    /**  */
    public static final int ALIGN_CENTER = 2;

    /**  */
    public static final int ALIGN_BASELINE = 3;

    /**  */
    public static final int ALIGN_AXIS = 4;

    /**  */
    public static final int ALIGN_LEFT = 5;

    /**  */
    public static final int ALIGN_RIGHT = 6;

    /**  */
    public static final int ALIGN_DECIMALPOINT = 7;

    private int m_rowalign = MathTableRow.ALIGN_CENTER;

    private int m_columnalign = MathTableRow.ALIGN_CENTER;

    private int m_groupalign = MathTableRow.ALIGN_CENTER;

    /**
     * Array with values of groupalign property in this row.
     */
    private int[] groupsalignvalues = null;

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
    public int getRowalign() {
        return this.m_rowalign;
    }

    /**
     * Sets row alignment.
     * 
     * @param rowalign
     *            Value of row alignment.
     */
    public void setRowalign(final int rowalign) {
        this.m_rowalign = rowalign;
    }

    /**
     * Gets alignment for group in column. (not implemented yet).
     * 
     * @return Alignment for group in column.
     */
    public int getColumnalign() {
        return this.m_columnalign;
    }

    /**
     * Sets alignment for group in column. (not implemented yet).
     * 
     * @param columnalign
     *            Alignment for group in column.
     */
    public void setColumnalign(final int columnalign) {
        this.m_columnalign = columnalign;
    }

    /**
     * Gets alignment of the group for the row. (not implemented yet).
     * 
     * @return Alifnment of the row.
     */
    public int getGroupalign() {
        return this.m_groupalign;
    }

    /**
     * Sets alignment of the group for the row. (not implemented yet).
     * 
     * @param groupalign
     *            Alignment.
     */
    public void setGroupalign(final int groupalign) {
        this.m_groupalign = groupalign;
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
    public int[] getGroupAlign() {
        return this.groupsalignvalues;
    }

    /**
     * Creates array with alignments for all align groups in this table.
     * 
     * @param groupalign
     *            String with table attribute "groupalign".
     */
    public void setGroupAlign(final String groupalign) {
        this.groupsalignvalues = MathTable.createGroupAlignValues(groupalign);
    }

    /** {@inheritDoc} */
    public String getTagName() {
        return MathTableRow.ELEMENT;
    }
}
