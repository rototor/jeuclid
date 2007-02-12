/*
 * Copyright 2002 - 2006 JEuclid, http://jeuclid.sf.net
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

import java.awt.Graphics;

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

    private int m_rowalign = ALIGN_CENTER;

    private int m_columnalign = ALIGN_CENTER;

    private int m_groupalign = ALIGN_CENTER;

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
    public MathTableRow(MathBase base) {
        super(base);
    }

    /**
     * Gets row alignment.
     * 
     * @return Alignment of the row.
     */
    public int getRowalign() {
        return m_rowalign;
    }

    /**
     * Sets row alignment.
     * 
     * @param rowalign
     *            Value of row alignment.
     */
    public void setRowalign(int rowalign) {
        m_rowalign = rowalign;
    }

    /**
     * Gets alignment for group in column. (not implemented yet).
     * 
     * @return Alignment for group in column.
     */
    public int getColumnalign() {
        return m_columnalign;
    }

    /**
     * Sets alignment for group in column. (not implemented yet).
     * 
     * @param columnalign
     *            Alignment for group in column.
     */
    public void setColumnalign(int columnalign) {
        m_columnalign = columnalign;
    }

    /**
     * Gets alignment of the group for the row. (not implemented yet).
     * 
     * @return Alifnment of the row.
     */
    public int getGroupalign() {
        return m_groupalign;
    }

    /**
     * Sets alignment of the group for the row. (not implemented yet).
     * 
     * @param groupalign
     *            Alignment.
     */
    public void setGroupalign(int groupalign) {
        m_groupalign = groupalign;
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
    public void paint(Graphics g, int posX, int posY) {
        super.paint(g, posX, posY);

        int columnwidth = getMaxColumnWidth(g);
        int pos = posX;

        for (int i = 0; i < getMathElementCount(); i++) {
            getMathElement(i).paint(g, pos, posY);
            pos += columnwidth;
        }
    }

    /**
     * Returns the maximal width of a column for all columns in this row.
     * 
     * @return Max column width.
     * @param g
     *            Graphics context to use.
     */
    protected int getMaxColumnWidth(Graphics g) {
        int width = 0;

        for (int i = 0; i < getMathElementCount(); i++) {
            width = Math.max(width, getMathElement(i).getWidth(g));
        }
        return width;
    }

    /**
     * Return the current width of this element.
     * 
     * @return Width of this element
     * @param g
     *            Graphics context to use.
     */
    public int calculateWidth(Graphics g) {
        return getMaxColumnWidth(g) * getMathElementCount();
    }

    /**
     * Returns the current height of the upper part of this component from the
     * baseline.
     * 
     * @return Height of the upper part.
     * @param g
     *            Graphics context to use.
     */
    public int calculateAscentHeight(Graphics g) {
        int height = 0;

        for (int i = 0; i < getMathElementCount(); i++) {
            height = Math.max(height, getMathElement(i).getAscentHeight(g));
        }
        return height;
    }

    /**
     * Returns the current height of the lower part of this component from the
     * baseline.
     * 
     * @return Height of the lower part.
     * @param g
     *            Graphics context to use.
     */
    public int calculateDescentHeight(Graphics g) {
        int height = 0;

        for (int i = 0; i < getMathElementCount(); i++) {
            height = Math.max(height, getMathElement(i).getDescentHeight(g));
        }
        return height;
    }

    /**
     * Gets alignments of the align group.
     * 
     * @return Array with group alignments values.
     */
    public int[] getGroupAlign() {
        return groupsalignvalues;
    }

    /**
     * Creates array with alignments for all align groups in this table.
     * 
     * @param groupalign
     *            String with table attribute "groupalign".
     */
    public void setGroupAlign(String groupalign) {
        groupsalignvalues = MathTable.createGroupAlignValues(groupalign);
    }
}
