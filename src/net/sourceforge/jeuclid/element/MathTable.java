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

/* $Id: MathTable.java,v 1.11.2.4 2006/11/04 04:28:28 maxberger Exp $ */

package net.sourceforge.jeuclid.element;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.element.generic.AbstractMathElement;
import net.sourceforge.jeuclid.element.helpers.AttributesHelper;

/**
 * This class presents a table.
 * 
 * @author Unknown
 * @author Max Berger
 */
public class MathTable extends AbstractMathElement {

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "mtable";

    /**
     * Align constant: top align.
     */
    public static final int ALIGN_TOP = 0;

    /**
     * Align constant: bottom align.
     */
    public static final int ALIGN_BOTTOM = 1;

    /**
     * Default column spacing.
     */
    public static final String DEFAULT_COLUMNSPACING = "0.8em";

    /**
     * Default row spacing.
     */
    public static final String DEFAULT_ROWSPACING = "1.0ex";

    /**
     * Default frame spacing.
     */
    public static final String DEFAULT_FRAMESPACING = "0.4em 0.5ex";

    /**
     * Align constant: center align.
     */
    public static final int ALIGN_CENTER = 2;

    /**
     * Align constant: baseline align.
     */
    public static final int ALIGN_BASELINE = 3;

    /**
     * Align constant: axis align.
     */
    public static final int ALIGN_AXIS = 4;

    /**
     * Align constant: left align.
     */
    public static final int ALIGN_LEFT = 5;

    /**
     * Align constant: right align.
     */
    public static final int ALIGN_RIGHT = 6;

    /**
     * Align constant: mark align.
     */
    public static final int ALIGN_MARK = 11;

    /**
     * Align constant: decimal point align.
     */
    public static final int ALIGN_DECIMALPOINT = 7;

    /**
     * Alignment variable.
     */
    private int m_align = ALIGN_CENTER;

    /**
     * Alignment of the row variable.
     */
    private int m_rowalign = ALIGN_CENTER;

    /**
     * Alignment of the column variable.
     */
    private int m_columnalign = ALIGN_CENTER;

    /**
     * Alignment of the group variable.
     */
    private int m_groupalign = ALIGN_CENTER;

    /**
     * Alignment scope variable.
     */
    private boolean m_alignmentscope = false;

    /**
     * Constant width auto.
     */
    public static final int WIDTH_AUTO = -1;

    /**
     * Constant width fit.
     */
    public static final int WIDTH_FIT = -2;

    /**
     * Column width variable.
     */
    private int m_columnwidth = WIDTH_AUTO;

    /**
     * Width variable.
     */
    private int m_width = WIDTH_AUTO;

    /**
     * Array with row spacing values..
     */
    private ArrayList m_rowspacing = new ArrayList();

    /**
     * Array with column spacing values..
     */
    private ArrayList m_columnspacing = new ArrayList();

    /**
     * Lines constant: no lines.
     */
    public static final int LINE_NONE = 0;

    /**
     * Lines constant: solid lines.
     */
    public static final int LINE_SOLID = 1;

    /**
     * Lines constant: dashed lines.
     */
    public static final int LINE_DASHED = 2;

    /**
     * Array with row lines values.
     */
    private int[] m_rowlines = new int[] { LINE_NONE };

    /**
     * Array with column lines values.
     */
    private int[] m_columnlines = new int[] { LINE_NONE };

    /**
     * Frame line value.
     */
    private int m_frame = LINE_NONE;

    /**
     * Frame spacing value.
     */
    private String m_framespacing = "";

    /**
     * Horizontal frame spacing value.
     */
    private int framespacingh = -1;

    /**
     * Vertical frame spacing value.
     */
    private int framespacingv = -1;

    /**
     * Equal rows flag.
     */
    private boolean m_equalrows = false;

    /**
     * Equal columns flag.
     */
    private boolean m_equalcolumns = false;

    /**  */
    public static final int SIDE_LEFT = 0;

    /**  */
    public static final int SIDE_RIGHT = 1;

    /**  */
    public static final int SIDE_LEFTOVERLAP = 2;

    /**  */
    public static final int SIDE_RIGHTOVERLAP = 3;

    private int m_side = SIDE_LEFT;

    private int m_minlabelspacing = 0;

    private int[] groupsalignvalues = null;

    /**
     * Creates a math element.
     * 
     * @param base
     *            The base for the math element tree.
     */
    public MathTable(MathBase base) {
        super(base);
    }

    /** {@inheritDoc} */
    protected boolean isChildBlock(AbstractMathElement child) {
        return false;
    }

    /**
     * @return Table align
     */
    public int getAlign() {
        return m_align;
    }

    /**
     * @param align
     *            Align of the table
     */
    public void setAlign(String align) {
        if (align.equals("axis")) {
            m_align = ALIGN_AXIS;
        } else if (align.equals("bottom")) {
            m_align = ALIGN_BOTTOM;
        } else if (align.equals("center")) {
            m_align = ALIGN_CENTER;
        } else if (align.equals("baseline")) {
            m_align = ALIGN_BASELINE;
        } else if (align.equals("top")) {
            m_align = ALIGN_TOP;
        }
    }

    /**
     * @return Row Align
     */
    public int getRowAlign() {
        return m_rowalign;
    }

    /**
     * @param rowalign
     *            Row Align
     */
    public void setRowAlign(int rowalign) {
        if ((rowalign == ALIGN_TOP) || (rowalign == ALIGN_BOTTOM)
                || (rowalign == ALIGN_CENTER) || (rowalign == ALIGN_BASELINE)
                || (rowalign == ALIGN_AXIS)) {
            m_rowalign = rowalign;
        }
    }

    /**
     * @return Column align
     */
    public int getColumnalign() {
        return m_columnalign;
    }

    /**
     * @param columnalign
     *            Column align
     */
    public void setColumnalign(int columnalign) {
        if ((columnalign == ALIGN_LEFT) || (columnalign == ALIGN_RIGHT)
                || (columnalign == ALIGN_CENTER)) {
            m_columnalign = columnalign;
        }
    }

    /**
     * @param groupalign
     *            Group align
     */
    public void setGroupalign(int groupalign) {
        if ((groupalign == ALIGN_LEFT) || (groupalign == ALIGN_RIGHT)
                || (groupalign == ALIGN_CENTER)
                || (groupalign == ALIGN_DECIMALPOINT)) {
            m_groupalign = groupalign;
        }
    }

    /**
     * 
     * @return Group Align
     */
    public int getGroupalign() {
        return m_groupalign;
    }

    /**
     * @return Alignment scope
     */
    public boolean getAlignmentscope() {
        return m_alignmentscope;
    }

    /**
     * @param alignmentscope
     *            Alignment scope
     */
    public void setAlignmentscope(boolean alignmentscope) {
        m_alignmentscope = alignmentscope;
    }

    /**
     * @return Column width
     */
    public int getColumnwidth() {
        return m_columnwidth;
    }

    /**
     * @param columnwidth
     *            Column width
     */
    public void setColumnwidth(int columnwidth) {
        if ((columnwidth >= 0) || (columnwidth == WIDTH_AUTO)
                || (columnwidth == WIDTH_FIT)) {
            m_columnwidth = columnwidth;
        }
    }

    /**
     * @param width
     *            Width of the table
     */
    public void setWidth(int width) {
        if ((width >= 0) || (width == WIDTH_AUTO)) {
            m_width = width;
        }
    }

    /**
     * 
     * @param row
     *            Row number
     * @return Row spacing after [row] row
     * @param g
     *            Graphics context to use.
     */
    public int getRowspacing(Graphics g, int row) {
        if (row < m_rowspacing.size()) {
            return AttributesHelper.getPixels((String) m_rowspacing.get(row),
                    getFontMetrics(g));
        } else {
            return AttributesHelper.getPixels(DEFAULT_ROWSPACING,
                    getFontMetrics(g));
        }
    }

    /**
     * @param rowspacing
     *            Row spacing
     */
    public void setRowspacing(String rowspacing) {
        int pos;
        while (!rowspacing.equals("")) {
            pos = rowspacing.indexOf(' ');
            if (pos < 0) {
                pos = rowspacing.length();
            }
            m_rowspacing.add(rowspacing.substring(0, pos));
            if (pos == rowspacing.length()) {
                rowspacing = "";
            } else {
                rowspacing = rowspacing.substring(pos + 1);
            }
        }
    }

    /**
     * 
     * @param col
     *            Column number
     * @return Column spacing
     * @param g
     *            Graphics context to use.
     */
    public int getColumnspacing(Graphics g, int col) {
        if (col < m_columnspacing.size()) {
            return AttributesHelper.getPixels(
                    (String) m_columnspacing.get(col), getFontMetrics(g));
        } else {
            return AttributesHelper.getPixels(DEFAULT_COLUMNSPACING,
                    getFontMetrics(g));
        }
    }

    /**
     * @param columnspacing
     *            Column spacing
     */
    public void setColumnspacing(String columnspacing) {
        int pos;
        while (!columnspacing.equals("")) {
            pos = columnspacing.indexOf(' ');
            if (pos < 0) {
                pos = columnspacing.length() - 1;
            }
            m_columnspacing.add(columnspacing.substring(0, pos));
            columnspacing = columnspacing.substring(pos + 1);
        }
    }

    /**
     * @return Table rows
     */
    public int[] getRowlines() {
        return m_rowlines;
    }

    /**
     * @param rowlines
     *            Table rows
     */
    public void setRowlines(int[] rowlines) {
        if (rowlines.length > 0) {
            m_rowlines = rowlines;
        }
    }

    /**
     * @return Table columns
     */
    public int[] getColumnlines() {
        return m_columnlines;
    }

    /**
     * @param columnlines
     *            Table columns
     */
    public void setColumnlines(int[] columnlines) {
        if (columnlines.length > 0) {
            m_columnlines = columnlines;
        }
    }

    /**
     * @return Frame of the table
     */
    public int getFrame() {
        return m_frame;
    }

    /**
     * @param frame
     *            Table frame
     */
    public void setFrame(int frame) {
        m_frame = frame;
    }

    /**
     * 
     * @return Horisonatl frame spacing
     * @param g
     *            Graphics context to use.
     */
    protected int getFramespacingh(Graphics g) {
        if (m_frame == LINE_NONE) {
            return 0;
        }
        if (m_framespacing == "") {
            setFramespacing(DEFAULT_FRAMESPACING);
        }
        if (framespacingh == -1) {
            framespacingh = AttributesHelper.getPixels(m_framespacing
                    .substring(0, m_framespacing.indexOf(' ')),
                    getFontMetrics(g));
            framespacingv = AttributesHelper.getPixels(m_framespacing
                    .substring(m_framespacing.indexOf(' ') + 1),
                    getFontMetrics(g));
        }
        return framespacingh;
    }

    /**
     * 
     * @return Vertical frame spacing
     * @param g
     *            Graphics context to use.
     */
    protected int getFramespacingv(Graphics g) {
        if (m_frame == LINE_NONE) {
            return 0;
        }
        if (m_framespacing == "") {
            setFramespacing(DEFAULT_FRAMESPACING);
        }
        if (framespacingh == -1) {
            framespacingh = AttributesHelper.getPixels(m_framespacing
                    .substring(0, m_framespacing.indexOf(' ')),
                    getFontMetrics(g));
            framespacingv = AttributesHelper.getPixels(m_framespacing
                    .substring(m_framespacing.indexOf(' ') + 1),
                    getFontMetrics(g));
        }
        return framespacingv;
    }

    /**
     * @param framespacing
     *            Frame spacing
     */
    public void setFramespacing(String framespacing) {
        m_framespacing = framespacing;
    }

    /**
     * @return True if equal rows mode
     */
    public boolean getEqualrows() {
        return m_equalrows;
    }

    /**
     * @param equalrows
     *            Equal row mode
     */
    public void setEqualrows(boolean equalrows) {
        m_equalrows = equalrows;
    }

    /**
     * @return True if equal columns mode
     */
    public boolean getEqualcolumns() {
        return m_equalcolumns;
    }

    /**
     * @param equalcolumns
     *            Equal columns mode
     */
    public void setEqualcolumns(boolean equalcolumns) {
        m_equalcolumns = equalcolumns;
    }

    /**
     * @return Side
     */
    public int getSide() {
        return m_side;
    }

    /**
     * @param side
     *            Side
     */
    public void setSide(int side) {
        if ((side == SIDE_LEFT) || (side == SIDE_RIGHT)
                || (side == SIDE_LEFTOVERLAP) || (side == SIDE_RIGHTOVERLAP)) {
            m_side = side;
        }
    }

    /**
     * Gets value of minlabelspacing property.
     * 
     * @return Length.
     */
    public int getMinlabelspacing() {
        return m_minlabelspacing;
    }

    /**
     * Sets value of minlabelspacing property.
     * 
     * @param minlabelspacing
     *            Length.
     */
    public void setMinlabelspacing(int minlabelspacing) {
        m_minlabelspacing = minlabelspacing;
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
        posX = posX + getFramespacingh(g);
        posY = posY + getFramespacingv(g);

        int i, j;
        int[] maxrowascentheight = new int[getMathElementCount()];
        int[] maxrowdescentheight = new int[getMathElementCount()];

        for (i = 0; i < getMathElementCount(); i++) {
            maxrowascentheight[i] = getMaxRowAscentHeight(g, i);
            maxrowdescentheight[i] = getMaxRowDescentHeight(g, i);
        }

        int maxcolumns = getMaxColumnCount();
        boolean isAlignGroupsExist = getMaxGroupAlignCount() == 0 ? false
                : true;
        int[] maxcolumnwidth = new int[maxcolumns];

        for (i = 0; i < maxcolumns; i++) {
            maxcolumnwidth[i] = getMaxColumnWidth(g, i);
        }

        int x1 = posX;
        int x = x1;

        posY = posY - getAscentHeight(g);
        for (i = 0; i < getMathElementCount(); i++) {
            AbstractMathElement row = getMathElement(i);
            posY += maxrowascentheight[i];

            x = x1;
            for (j = 0; (j < maxcolumns) && (j < row.getMathElementCount()); j++) {
                if (isAlignGroupsExist
                        && getAlignGroups((MathTableData) row.getMathElement(j)).length > 0) {
                    // left alignment
                    row.getMathElement(j).paint(g, x, posY);
                } else {
                    // PG this code makes table to draw content of the cell in
                    // the middle of the cell
                    int xx = x + maxcolumnwidth[j] / 2
                            - row.getMathElement(j).getWidth(g) / 2;
                    row.getMathElement(j).paint(g, xx, posY);
                }
                x += maxcolumnwidth[j] + getColumnspacing(g, j);
            }

            posY += maxrowdescentheight[i];
            posY += getRowspacing(g, i);
        }
    }

    /**
     * Returns the maximal ascent height of a row in this table.
     * 
     * @param row
     *            Row.
     * @return Maximal ascent height.
     */
    private int getMaxRowAscentHeight(Graphics g, int row) {
        if (row >= getMathElementCount()) {
            return 0;
        }
        AbstractMathElement child = getMathElement(row);
        int height = 0;

        for (int i = 0; i < child.getMathElementCount(); i++) {
            height = Math.max(height, child.getMathElement(i)
                    .getAscentHeight(g));
        }
        return height;
    }

    /**
     * Returns the maximal descent height of a row in this table.
     * 
     * @param row
     *            Row.
     * @return Maximal descent height.
     */
    private int getMaxRowDescentHeight(Graphics g, int row) {
        if (row >= getMathElementCount()) {
            return 0;
        }

        AbstractMathElement child = getMathElement(row);
        int height = 0;

        for (int i = 0; i < child.getMathElementCount(); i++) {
            height = Math.max(height, child.getMathElement(i).getDescentHeight(
                    g));
        }
        return height;
    }

    /**
     * Returns the maximal width of a column in this table.
     * 
     * @param column
     *            Column.
     * @return Maximal width.
     */
    private int getMaxColumnWidth(Graphics g, int column) {
        int width = 0;

        for (int i = 0; i < getMathElementCount(); i++) {
            AbstractMathElement child = getMathElement(i); // row

            if (column < child.getMathElementCount()) {
                width = Math.max(width, child.getMathElement(column)
                        .getWidth(g));
            }
        }
        return width;
    }

    /**
     * Returns the maximal count of columns.
     * 
     * @return Maximal count of columns.
     */
    private int getMaxColumnCount() {
        int count = 0;

        for (int i = 0; i < getMathElementCount(); i++) {
            AbstractMathElement child = getMathElement(i);
            count = Math.max(count, child.getMathElementCount());
        }
        return count;
    }

    /** {@inheritDoc} */
    public int calculateWidth(Graphics g) {
        calculateAlignmentGroups(g);
        int width = 0;
        int maxcolumns = getMaxColumnCount();

        for (int i = 0; i < maxcolumns; i++) {
            width = width + getMaxColumnWidth(g, i);
            if (i + 1 < maxcolumns) {
                width = width + getColumnspacing(g, i);
            }
        }
        width = width + getFramespacingh(g) * 2;
        return width;
    }

    /** {@inheritDoc} */
    public int calculateHeight(Graphics g) {
        int height = 0;
        int mec = getMathElementCount();
        for (int i = 0; i < mec; i++) {
            height = height + getMaxRowAscentHeight(g, i)
                    + getMaxRowDescentHeight(g, i);
            if (i + 1 < mec) {
                height = height + getRowspacing(g, i);
            }
        }
        height = height + getFramespacingv(g) * 2;
        return height;
    }

    /** {@inheritDoc} */
    public int calculateAscentHeight(Graphics g) {
        if (this.getAlign() == MathTable.ALIGN_BOTTOM) {
            return getHeight(g);
        }
        if (this.getAlign() == MathTable.ALIGN_TOP) {
            return this.getRowCount() > 0 ? getMaxRowAscentHeight(g, 0) : 0;
        }
        if (this.getAlign() == MathTable.ALIGN_AXIS) {
            return getHeight(g) / 2;
        }
        // baseline or center
        return getHeight(g) / 2 + getMiddleShift(g);
    }

    /** {@inheritDoc} */
    public int calculateDescentHeight(Graphics g) {
        if (this.getAlign() == MathTable.ALIGN_BOTTOM) {
            return 0;
        }
        if (this.getAlign() == MathTable.ALIGN_TOP) {
            return getHeight(g)
                    - (this.getRowCount() > 0 ? getMaxRowAscentHeight(g, 0) : 0);
        }
        if (this.getAlign() == MathTable.ALIGN_AXIS) {
            return getHeight(g) / 2;
        }
        int b = getMiddleShift(g);
        int c = getHeight(g) / 2;
        return c - b;
    }

    /*
     * ----------------- NEW FUNCTIONS, CONCERNED <MALIGNGROUP> ELEMENT
     * -------------------
     */

    /**
     * Set "groupalign" attribute.
     * 
     * @param groupalign
     *            Contains string with "groupalign" attribute.
     */
    public void setGroupAlign(String groupalign) {
        groupsalignvalues = createGroupAlignValues(groupalign);
    }

    /**
     * Creates array with alignments according string in parameter.
     * 
     * @param groupalign
     *            String with value of attribute "groupalign"
     * @return Array with alignment constants
     */
    public static int[] createGroupAlignValues(String groupalign) {
        int[] result = null;
        ArrayList alignArray = new ArrayList();
        String alignString = groupalign.trim();
        alignString = alignString.substring(1, alignString.length() - 1) + " ";

        // parsing string and filling out ArrayList object
        int spaceIndex = alignString.indexOf(" ");
        while (spaceIndex > 0) {
            String valueString = alignString.substring(0, spaceIndex)
                    .toLowerCase();
            int valueInt = ALIGN_LEFT;

            if (valueString.equals("center")) { // 2
                valueInt = ALIGN_CENTER;
            } else {
                if (valueString.equals("left")) { // 5
                    valueInt = ALIGN_LEFT;
                } else {
                    if (valueString.equals("right")) { // 6
                        valueInt = ALIGN_RIGHT;
                    } else {
                        if (valueString.equals("decimalpoint")) { // 7
                            valueInt = ALIGN_DECIMALPOINT;
                        }
                    }
                }
            }

            alignArray.add(new Integer(valueInt));
            alignString = alignString.substring(spaceIndex + 1);
            spaceIndex = alignString.indexOf(" ");
        }

        // copy to internal array
        result = new int[alignArray.size()];
        for (int i = 0; i < alignArray.size(); i++) {
            result[i] = ((Integer) alignArray.get(i)).intValue();
        }

        return result;
    }

    /**
     * Get array with group alignment constants.
     * 
     * @return Array with group alignment constants.
     */
    public int[] getGroupAlign() {
        return groupsalignvalues;
    }

    /**
     * Retrieves groupalign values from mtd element. If requested cell doesn't
     * contain groupalign attribute, the current row (and after it current
     * table) will be requested.
     * 
     * @param cell
     *            Cell to get groupalign values.
     * @return Array with groupalign values.
     */
    public int[] getGroupAlign(MathTableData cell) {
        int[] result = null;

        if (cell == null) {
            return new int[0];
        }

        if (cell.getGroupAlign() == null || cell.getGroupAlign().length == 0) {
            result = ((MathTableRow) cell.getParent()).getGroupAlign();
        } else {
            result = cell.getGroupAlign();
        }

        if (result == null) {
            result = getGroupAlign();
        }

        if (result == null) {
            result = new int[0];
        }

        return result;
    }

    /**
     * Get max count of align groups in a cell.
     * 
     * @return Max number of groups.
     */
    private int getMaxGroupAlignCount() {
        int result = 0;
        int rowsCount = getRowCount();
        int columnsCount = getMaxColumnCount();
        int tmpLength = 0;

        for (int column = 0; column < columnsCount; column++) {
            for (int row = 0; row < rowsCount; row++) {
                MathTableData cell = null;
                try {
                    cell = getCell(row, column);
                } catch (Exception e) {
                    return 0;
                }

                if (cell != null) {
                    tmpLength = cell.getAlignGroups().size();
                    if (result < tmpLength) {
                        result = tmpLength;
                    }
                }
            }
        }

        return result;
    }

    /**
     * Gets count of rows in table.
     * 
     * @return
     */
    private int getRowCount() {
        return getMathElementCount();
    }

    /**
     * Finds cell in the table by row and column indexes.
     * 
     * @param row
     *            Row index.
     * @param column
     *            Column index.
     * @return Cell object.
     * @throws Exception
     *             Throw exception, if table doesn't contain <mtr> and <mtd>
     *             tags.
     */
    private MathTableData getCell(int row, int column) throws Exception {
        int rowsCount = getRowCount();
        int columnsCount = getMaxColumnCount();
        MathTableData cell = null;

        if (row > rowsCount - 1 || column > columnsCount - 1) {
            return cell;
        }

        AbstractMathElement theRow = getMathElement(row); // row
        if (column < theRow.getMathElementCount()) {
            if (theRow.getMathElement(column) instanceof MathTableData) {
                cell = (MathTableData) theRow.getMathElement(column);
            } else {
                throw new Exception(
                        "This table doesn't contain <mtr> and <mtd> tags.");
            }
        }

        return cell;
    }

    /**
     * Determines all maligngroup elements in the cell.
     * 
     * @param cell
     *            Cell object.
     * @return Array of maligngroup elements.
     */
    private MathAlignGroup[] getAlignGroups(MathTableData cell) {
        MathAlignGroup[] result;

        ArrayList list = cell.getAlignGroups();

        if (list.size() == 0) {
            result = new MathAlignGroup[0];
        } else {
            // copy to result array
            result = new MathAlignGroup[list.size()];
            for (int i = 0; i < list.size(); i++) {
                result[i] = ((MathAlignGroup) list.get(i));
            }
        }

        return result;
    }

    /**
     * Constant for calculating of align elements widths.
     */
    private static int WHOLE_WIDTH = 0;

    /**
     * Constant for calculating of align elements widths.
     */
    private static int LEFT_WIDTH = 1;

    /**
     * Constant for calculating of align elements widths.
     */
    private static int RIGHT_WIDTH = 2;

    /**
     * Method calculates widths of alignment elements. Must be called after the
     * calculating of all MathElements widths.
     * 
     * @param g
     *            Graphics context to use.
     */
    protected void calculateAlignmentGroups(Graphics g) {
        int rowsCount = getRowCount();
        int columnsCount = getMaxColumnCount();
        int MAX_WIDTH_IN_COLUMN = rowsCount;
        int maxGroupAlignCount = getMaxGroupAlignCount();
        if (maxGroupAlignCount == 0) {
            return;
        }
        // structure to hold calculated temp values, for each element from align
        // group
        // [0] whole width value
        // [1] left width value (till malignmark or decimal point)
        // [2] right width value
        // rowsCount+1 - max width of element (or its part)
        int[][][][] alignwidths = new int[columnsCount][3][rowsCount + 1][maxGroupAlignCount];
        // need to know, either this column of aligngroup uses malignmarks
        boolean[][] usesMarks = new boolean[columnsCount][getMaxGroupAlignCount()];
        // array of aligngropus of the cell
        MathAlignGroup[] aligngroups; // align values of aligngroups in the
        // cell
        int[] groupalignvalues; // elements of the aligngroup
        ArrayList elements;
        for (int col = 0; col < columnsCount; col++) { // walking through the
            // column "column"
            for (int row = 0; row < rowsCount; row++) {
                MathTableData cell = null;
                try {
                    cell = getCell(row, col);
                } catch (Exception e) {
                    cell = null;
                }
                if (cell != null) { // all align components of the cell
                    aligngroups = getAlignGroups(cell);
                    groupalignvalues = getGroupAlign(cell);
                    if (groupalignvalues.length == 0) {
                        // values of alignment didn't mentioned, have to use
                        // default
                        groupalignvalues = new int[aligngroups.length];
                        for (int i = 0; i < aligngroups.length; i++) {
                            groupalignvalues[i] = ALIGN_LEFT;
                        }
                    }
                    if (aligngroups.length == 0
                            || aligngroups.length < groupalignvalues.length) {
                        continue; // there is no aligngroups in the cell or
                        // wrong count
                    }
                    for (int alignIndex = 0; alignIndex < groupalignvalues.length; alignIndex++) {
                        // widths of align group
                        elements = MathAlignGroup
                                .getElementsOfAlignGroup(aligngroups[alignIndex]);
                        alignwidths[col][WHOLE_WIDTH][row][alignIndex] = MathAlignGroup
                                .getElementsWholeWidth(g, elements);
                        // max whole width of group
                        if (alignwidths[col][WHOLE_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex] < alignwidths[col][WHOLE_WIDTH][row][alignIndex]) {
                            alignwidths[col][WHOLE_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex] = alignwidths[col][WHOLE_WIDTH][row][alignIndex];
                        }
                        // there is alignmark in the group
                        if (aligngroups[alignIndex].getMark() != null) {
                            usesMarks[col][alignIndex] = true;
                            int leftPart = getWidthTillMark(g, elements
                                    .iterator());
                            // left width = width till alignmark
                            alignwidths[col][LEFT_WIDTH][row][alignIndex] = leftPart;
                            // right width = whole width - left width
                            alignwidths[col][RIGHT_WIDTH][row][alignIndex] = alignwidths[col][WHOLE_WIDTH][row][alignIndex]
                                    - leftPart;
                            // max left width
                            if (alignwidths[col][LEFT_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex] < leftPart) {
                                alignwidths[col][LEFT_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex] = leftPart;
                            }
                            // max right width
                            if (alignwidths[col][RIGHT_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex] < alignwidths[col][RIGHT_WIDTH][row][alignIndex]) {
                                alignwidths[col][RIGHT_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex] = alignwidths[col][RIGHT_WIDTH][row][alignIndex];
                            }
                        } else {
                            // there is no alignmark, right width is equal to
                            // the whole width
                            alignwidths[col][LEFT_WIDTH][row][alignIndex] = 0;
                            alignwidths[col][RIGHT_WIDTH][row][alignIndex] = alignwidths[col][WHOLE_WIDTH][row][alignIndex];
                            // but! may be, we have alignment decimalpoint...
                            if (groupalignvalues[alignIndex] == ALIGN_DECIMALPOINT) {
                                // width of the left (till decimal point) value
                                // of MathNumber
                                int tillPoint = getWidthTillPoint(g, elements
                                        .iterator());
                                int pointWidth = getPointWidth(g, elements
                                        .iterator());
                                // left width
                                alignwidths[col][LEFT_WIDTH][row][alignIndex] = tillPoint;
                                // determine max left width till decimal point
                                if (alignwidths[col][LEFT_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex] < tillPoint) {
                                    alignwidths[col][LEFT_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex] = tillPoint;
                                }
                                // calculate right part
                                alignwidths[col][RIGHT_WIDTH][row][alignIndex] = alignwidths[col][WHOLE_WIDTH][row][alignIndex]
                                        - tillPoint - pointWidth;
                                // calculate right max
                                if (alignwidths[col][RIGHT_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex] < alignwidths[col][RIGHT_WIDTH][row][alignIndex]) {
                                    alignwidths[col][RIGHT_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex] = alignwidths[col][RIGHT_WIDTH][row][alignIndex];
                                }
                            }
                        } // no align mark in this align group
                    } // cycle: align groups in the cell
                } // if cell != null
            } // cycle: rows
        } // cycle: columns

        // calculating shifts of MathAlignGroup elements
        int alignOfTheGroup = ALIGN_LEFT;
        int currentWidth = 0;
        int maxWidth = 0;
        int leftWidth = 0;
        int leftMaxWidth = 0;
        int rightWidth = 0;
        int rightMaxWidth = 0;
        MathAlignGroup group = null;
        MathAlignGroup nextGroup = null;
        for (int col = 0; col < columnsCount; col++) {
            for (int row = 0; row < rowsCount; row++) {
                MathTableData cell = null;
                try {
                    cell = getCell(row, col);
                } catch (Exception e) {
                    cell = null;
                }

                if (cell != null) {
                    aligngroups = getAlignGroups(cell);
                    groupalignvalues = getGroupAlign(cell);
                    if (groupalignvalues.length == 0) {
                        // values of alignment didn't mentioned, have to use
                        // default
                        groupalignvalues = new int[aligngroups.length];
                        for (int i = 0; i < aligngroups.length; i++) {
                            groupalignvalues[i] = ALIGN_LEFT;
                        }
                    }
                    if (aligngroups.length == 0
                            || aligngroups.length < groupalignvalues.length) {
                        continue;
                    }
                    for (int alignIndex = 0; alignIndex < groupalignvalues.length; alignIndex++) {
                        // retrieving previously calculated information about
                        // aligngroups widths
                        alignOfTheGroup = groupalignvalues[alignIndex];
                        currentWidth = alignwidths[col][WHOLE_WIDTH][row][alignIndex];
                        maxWidth = alignwidths[col][WHOLE_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex];
                        leftWidth = alignwidths[col][LEFT_WIDTH][row][alignIndex];
                        leftMaxWidth = alignwidths[col][LEFT_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex];
                        rightWidth = alignwidths[col][RIGHT_WIDTH][row][alignIndex];
                        rightMaxWidth = alignwidths[col][RIGHT_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex];
                        group = (MathAlignGroup) aligngroups[alignIndex];
                        if (usesMarks[col][alignIndex]) {
                            alignOfTheGroup = ALIGN_MARK;
                        }
                        if (alignIndex < groupalignvalues.length - 1) {
                            nextGroup = (MathAlignGroup) aligngroups[alignIndex + 1];
                        }
                        switch (alignOfTheGroup) {
                        case ALIGN_RIGHT:
                            group.width += maxWidth - currentWidth;
                            break;
                        case ALIGN_LEFT:
                            if (alignIndex < groupalignvalues.length - 1) {
                                nextGroup.width += maxWidth - currentWidth;
                            }
                            break;
                        case ALIGN_CENTER:
                            group.width += (maxWidth - currentWidth) / 2;
                            if (alignIndex < groupalignvalues.length - 1) {
                                nextGroup.width += (maxWidth - currentWidth) / 2;
                            }
                            break;
                        case ALIGN_DECIMALPOINT:
                        case ALIGN_MARK:
                            group.width += (leftMaxWidth - leftWidth);
                            if (alignIndex < groupalignvalues.length - 1) {
                                nextGroup.width += rightMaxWidth - rightWidth;
                            }
                            break;
                        default:
                            group.width += maxWidth - currentWidth;
                        }
                    }
                }
            }
        }
    }

    /**
     * Iterator over a NodeList.
     * 
     * @author Max Berger
     */
    private class ChildIterator implements Iterator {

        private final org.w3c.dom.NodeList nodeList;

        private int pos;

        protected ChildIterator(org.w3c.dom.NodeList nl) {
            this.nodeList = nl;
        }

        public boolean hasNext() {
            return pos < nodeList.getLength();
        }

        public Object next() {
            pos++;
            return nodeList.item(pos - 1);
        }

        public void remove() {
            // not needed.
        }

    }

    /**
     * Method calculates width of elements till point inside of mn element (or
     * first of mn-element).
     * 
     * @param elements
     *            List of elements.
     * @return Width of elements till point.
     */
    private int getWidthTillPoint(Graphics g, Iterator elements) {
        int result = 0;

        AbstractMathElement element = null;
        for (; elements.hasNext();) {
            element = (AbstractMathElement) elements.next();
            if (element instanceof MathNumber) {
                return result + ((MathNumber) element).getWidthTillPoint(g);
            } else {
                if (!containsNumber(element)) {
                    result += element.getWidth(g);
                } else {
                    result += getWidthTillPoint(g, new ChildIterator(this
                            .getChildNodes()));

                }
            }
        }

        return result;
    }

    /*
     * Checks, whether provided element contains any mn - element. @return True,
     * if contains any mn element.
     */
    private boolean containsNumber(AbstractMathElement element) {
        return containsElement(element, MathNumber.ELEMENT);
    }

    /**
     * Finds in the list of the element mn element and requests it for the with
     * of the '.' character (used for the "decimalpoint" alignment).
     * 
     * @param iterator
     *            List of elements.
     * @return Width of the point.
     */
    private int getPointWidth(Graphics g, Iterator iterator) {
        int result = 0;

        for (; iterator.hasNext();) {
            AbstractMathElement element = (AbstractMathElement) iterator.next();
            if (element instanceof MathNumber) {
                result = ((MathNumber) element).getPointWidth(g);
                break;
            }
        }

        return result;
    }

    /**
     * Method calculates width of elements till malignmark element.
     * 
     * @param elements
     *            List of elements.
     * @return Width of elements till malignmar.
     */
    private int getWidthTillMark(Graphics g, Iterator elements) {
        int result = 0;

        AbstractMathElement element = null;
        for (; elements.hasNext();) {
            element = (AbstractMathElement) elements.next();
            if (element instanceof MathAlignMark) {
                return result;
            } else {
                if (!containsMark(element)) {
                    result += element.getWidth(g);
                } else {
                    result += getWidthTillMark(g, new ChildIterator(this
                            .getChildNodes()));
                }
            }
        }

        return result;
    }

    /**
     * Method checks, whether provided element contains malignmark element.
     * 
     * @param element
     *            Container object.
     * @return True, if element contains any malignmark object.
     */
    private boolean containsMark(AbstractMathElement element) {
        return containsElement(element, MathAlignMark.ELEMENT);
    }

    /**
     * Method checks, whether provided container contains element of type, as
     * searchName.
     * 
     * @param container
     *            Container object.
     * @param searchName
     *            Type of element to look for.
     * @return True, if container contains such type of element.
     */
    private boolean containsElement(AbstractMathElement container,
            final String searchName) {
        if (container.ELEMENT.equals(searchName)) {
            return true;
        }

        for (int i = 0; i < container.getMathElementCount(); i++) {
            if (containsElement(container.getMathElement(i), searchName)) {
                return true;
            }
        }

        return false;
    }

}
