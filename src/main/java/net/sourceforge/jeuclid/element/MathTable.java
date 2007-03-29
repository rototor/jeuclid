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

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.element.generic.AbstractMathElement;
import net.sourceforge.jeuclid.element.generic.MathElement;
import net.sourceforge.jeuclid.element.helpers.AttributesHelper;

/**
 * This class presents a table.
 * 
 * @author Unknown
 * @author Max Berger
 */
public class MathTable extends AbstractMathElement {

    /** attribute for rowlines. */
    public static final String ATTR_ROWLINES = "rowlines";

    /** attribute for columnlines. */
    public static final String ATTR_COLUMNLINES = "columnlines";

    /** attribute for align. */
    public static final String ATTR_ALIGN = "align";

    /** attribute for alignmentscope. */
    public static final String ATTR_ALIGNMENTSCOPE = "alignmentscope";

    /** attribute for columnwidth. */
    public static final String ATTR_COLUMNWIDTH = "columnwidth";

    /** attribute for width. */
    public static final String ATTR_WIDTH = "width";

    /** attribute for rowspacing. */
    public static final String ATTR_ROWSPACING = "rowspacing";

    /** attribute for columnspacing. */
    public static final String ATTR_COLUMNSPACING = "columnspacing";

    /** attribute for frame. */
    public static final String ATTR_FRAME = "frame";

    /** attribute for framespacing. */
    public static final String ATTR_FRAMESPACING = "framespacing";

    /** attribute for equalrows. */
    public static final String ATTR_EQUALROWS = "equalrows";

    /** attribute for equalcolumns. */
    public static final String ATTR_EQUALCOLUMNS = "equalcolumns";

    /** attribute for displaystyle. */
    public static final String ATTR_DISPLAYSTYLE = "displaystyle";

    /** attribute for side. */
    public static final String ATTR_SIDE = "side";

    /** attribute for minlabelspacing. */
    public static final String ATTR_MINLABELSPACING = "minlabelspacing";

    /** value for no lines. */
    public static final String VALUE_NONE = "none";

    /** value for dashed lines. */
    public static final String VALUE_DASHED = "dashed";

    /** value for solid lines. */
    public static final String VALUE_SOLID = "solid";

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

    /** Attribute for rowalign. */
    public static final String ATTR_ROWALIGN = "rowalign";

    /** Attribute for columnalign. */
    public static final String ATTR_COLUMNALIGN = "columnalign";

    /** Attribute for groupalign. */
    public static final String ATTR_GROUPALIGN = "groupalign";

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
    private static final String DEFAULT_FRAMESPACING = "0.4em 0.5ex";

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
     * Constant width auto.
     */
    public static final int WIDTH_AUTO = -1;

    /**
     * Constant width fit.
     */
    public static final int WIDTH_FIT = -2;

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

    /**  */
    public static final int SIDE_LEFT = 0;

    /**  */
    public static final int SIDE_RIGHT = 1;

    /**  */
    public static final int SIDE_LEFTOVERLAP = 2;

    /**  */
    public static final int SIDE_RIGHTOVERLAP = 3;

    /**
     * Alignment variable.
     */
    private int m_align = MathTable.ALIGN_CENTER;

    /**
     * Alignment of the row variable.
     */
    private int m_rowalign = MathTable.ALIGN_CENTER;

    /**
     * Alignment of the column variable.
     */
    private int m_columnalign = MathTable.ALIGN_CENTER;

    /**
     * Alignment of the group variable.
     */
    private int m_groupalign = MathTable.ALIGN_CENTER;

    /**
     * Alignment scope variable.
     */
    private boolean m_alignmentscope = false;

    /**
     * Array with row spacing values..
     */
    private final List<String> m_rowspacing = new Vector<String>();

    /**
     * Array with column spacing values..
     */
    private final List<String> m_columnspacing = new Vector<String>();

    private int[] groupsalignvalues = null;

    /**
     * Class for line types.
     */
    public enum LineType {
        /** No lines. */
        NONE,
        /** Solid line. */
        SOLID,
        /** Dashed line. */
        DASHED;

        /**
         * Parse a string and return a linetype.
         * 
         * @param s
         *            the string to parse
         * @return a line type for this string type
         */
        public static LineType parseLineType(final String s) {
            final LineType retVal;
            if (s.equalsIgnoreCase(MathTable.VALUE_NONE)) {
                retVal = NONE;
            } else if (s.equalsIgnoreCase(MathTable.VALUE_DASHED)) {
                retVal = DASHED;
            } else {
                retVal = SOLID;
            }
            return retVal;
        }
    };

    /**
     * Creates a math element.
     * 
     * @param base
     *            The base for the math element tree.
     */
    public MathTable(final MathBase base) {
        super(base);
        this.setDefaultMathAttribute(MathTable.ATTR_ALIGN, "axis");
        this.setDefaultMathAttribute(MathTable.ATTR_ROWALIGN, "baseline");
        this.setDefaultMathAttribute(MathTable.ATTR_COLUMNALIGN, "center");
        this.setDefaultMathAttribute(MathTable.ATTR_GROUPALIGN, "{left}");
        this.setDefaultMathAttribute(MathTable.ATTR_ALIGNMENTSCOPE, "true");
        this.setDefaultMathAttribute(MathTable.ATTR_COLUMNWIDTH, "auto");
        this.setDefaultMathAttribute(MathTable.ATTR_WIDTH, "auto");
        this.setDefaultMathAttribute(MathTable.ATTR_ROWSPACING, "1.0ex");
        this.setDefaultMathAttribute(MathTable.ATTR_COLUMNSPACING, "0.8em");
        this.setDefaultMathAttribute(MathTable.ATTR_ROWLINES,
                MathTable.VALUE_NONE);
        this.setDefaultMathAttribute(MathTable.ATTR_COLUMNLINES,
                MathTable.VALUE_NONE);
        this.setDefaultMathAttribute(MathTable.ATTR_FRAME,
                MathTable.VALUE_NONE);
        this.setDefaultMathAttribute(MathTable.ATTR_FRAMESPACING,
                DEFAULT_FRAMESPACING);
        this.setDefaultMathAttribute(MathTable.ATTR_EQUALROWS, "false");
        this.setDefaultMathAttribute(MathTable.ATTR_EQUALCOLUMNS, "false");
        this.setDefaultMathAttribute(MathTable.ATTR_DISPLAYSTYLE, "false");
        this.setDefaultMathAttribute(MathTable.ATTR_SIDE, "right");
        this.setDefaultMathAttribute(MathTable.ATTR_MINLABELSPACING, "0.8em");
    }

    /** {@inheritDoc} */
    @Override
    public boolean isChildBlock(final MathElement child) {
        return false;
    }

    /**
     * @return Table align
     */
    public int getAlign() {
        return this.m_align;
    }

    /**
     * @param align
     *            Align of the table
     */
    public void setAlign(final String align) {
        if (align.equals("axis")) {
            this.m_align = MathTable.ALIGN_AXIS;
        } else if (align.equals("bottom")) {
            this.m_align = MathTable.ALIGN_BOTTOM;
        } else if (align.equals("center")) {
            this.m_align = MathTable.ALIGN_CENTER;
        } else if (align.equals("baseline")) {
            this.m_align = MathTable.ALIGN_BASELINE;
        } else if (align.equals("top")) {
            this.m_align = MathTable.ALIGN_TOP;
        }
    }

    /**
     * @return Row Align
     */
    public int getRowAlign() {
        return this.m_rowalign;
    }

    /**
     * @param rowalign
     *            Row Align
     */
    public void setRowAlign(final int rowalign) {
        if ((rowalign == MathTable.ALIGN_TOP)
                || (rowalign == MathTable.ALIGN_BOTTOM)
                || (rowalign == MathTable.ALIGN_CENTER)
                || (rowalign == MathTable.ALIGN_BASELINE)
                || (rowalign == MathTable.ALIGN_AXIS)) {
            this.m_rowalign = rowalign;
        }
    }

    /**
     * @return Column align
     */
    public int getColumnalign() {
        return this.m_columnalign;
    }

    /**
     * @param columnalign
     *            Column align
     */
    public void setColumnalign(final int columnalign) {
        if ((columnalign == MathTable.ALIGN_LEFT)
                || (columnalign == MathTable.ALIGN_RIGHT)
                || (columnalign == MathTable.ALIGN_CENTER)) {
            this.m_columnalign = columnalign;
        }
    }

    /**
     * @param groupalign
     *            Group align
     */
    public void setGroupalign(final int groupalign) {
        if ((groupalign == MathTable.ALIGN_LEFT)
                || (groupalign == MathTable.ALIGN_RIGHT)
                || (groupalign == MathTable.ALIGN_CENTER)
                || (groupalign == MathTable.ALIGN_DECIMALPOINT)) {
            this.m_groupalign = groupalign;
        }
    }

    /**
     * 
     * @return Group Align
     */
    public int getGroupalign() {
        return this.m_groupalign;
    }

    /**
     * @return Alignment scope
     */
    public boolean getAlignmentscope() {
        return this.m_alignmentscope;
    }

    /**
     * @param alignmentscope
     *            Alignment scope
     */
    public void setAlignmentscope(final boolean alignmentscope) {
        this.m_alignmentscope = alignmentscope;
    }

    /**
     * 
     * @param row
     *            Row number
     * @return Row spacing after [row] row
     * @param g
     *            Graphics2D context to use.
     */
    public int getRowspacing(final Graphics2D g, final int row) {
        if (row < this.m_rowspacing.size()) {
            return (int) AttributesHelper.convertSizeToPt(this.m_rowspacing
                    .get(row), this, AttributesHelper.PT);
        } else {
            return (int) AttributesHelper.convertSizeToPt(
                    MathTable.DEFAULT_ROWSPACING, this, AttributesHelper.PT);
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
            this.m_rowspacing.add(rowspacing.substring(0, pos));
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
     *            Graphics2D context to use.
     */
    public int getColumnspacing(final Graphics2D g, final int col) {
        if (col < this.m_columnspacing.size()) {
            return (int) AttributesHelper.convertSizeToPt(
                    this.m_columnspacing.get(col), this, AttributesHelper.PT);
        } else {
            return (int) AttributesHelper.convertSizeToPt(
                    MathTable.DEFAULT_COLUMNSPACING, this,
                    AttributesHelper.PT);
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
            this.m_columnspacing.add(columnspacing.substring(0, pos));
            columnspacing = columnspacing.substring(pos + 1);
        }
    }

    /**
     * 
     * @return Horizontal frame spacing
     */
    protected int getFramespacingh() {
        if (MathTable.LineType.NONE.equals(this.getFrameAsLineType())) {
            return 0;
        }
        final String spacing = this.getSpaceArrayEntry(
                this.getFramespacing(), 0);
        final float retVal = AttributesHelper.convertSizeToPt(spacing, this,
                AttributesHelper.PT);
        return (int) Math.ceil(retVal);
    }

    /**
     * 
     * @return Vertical frame spacing
     */
    protected int getFramespacingv() {
        if (MathTable.LineType.NONE.equals(this.getFrameAsLineType())) {
            return 0;
        }
        final String spacing = this.getSpaceArrayEntry(
                this.getFramespacing(), 1);
        final float retVal = AttributesHelper.convertSizeToPt(spacing, this,
                AttributesHelper.PT);
        return (int) Math.ceil(retVal);
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
    public void paint(final Graphics2D g, int posX, int posY) {
        super.paint(g, posX, posY);
        posX = posX + this.getFramespacingh();
        posY = posY + this.getFramespacingv();

        int i;
        int j;
        final int[] maxrowascentheight = new int[this.getMathElementCount()];
        final int[] maxrowdescentheight = new int[this.getMathElementCount()];

        for (i = 0; i < this.getMathElementCount(); i++) {
            maxrowascentheight[i] = this.getMaxRowAscentHeight(g, i);
            maxrowdescentheight[i] = this.getMaxRowDescentHeight(g, i);
        }

        final int maxcolumns = this.getMaxColumnCount();
        final boolean isAlignGroupsExist = this.getMaxGroupAlignCount() == 0 ? false
                : true;
        final int[] maxcolumnwidth = new int[maxcolumns];

        for (i = 0; i < maxcolumns; i++) {
            maxcolumnwidth[i] = this.getMaxColumnWidth(g, i);
        }

        final int x1 = posX;
        int x = x1;

        posY = posY - this.getAscentHeight(g);
        final int startY = posY;

        final List<Float> rowlines = new Vector<Float>();
        final List<Float> columnlines = new Vector<Float>(maxcolumns);

        for (i = 0; i < this.getMathElementCount(); i++) {
            final MathElement row = this.getMathElement(i);
            posY += maxrowascentheight[i];

            x = x1;
            for (j = 0; (j < maxcolumns) && (j < row.getMathElementCount()); j++) {
                if (isAlignGroupsExist
                        && this.getAlignGroups((MathTableData) row
                                .getMathElement(j)).length > 0) {
                    // left alignment
                    row.getMathElement(j).paint(g, x, posY);
                } else {
                    // PG this code makes table to draw content of the cell in
                    // the middle of the cell
                    final int xx = x + maxcolumnwidth[j] / 2
                            - row.getMathElement(j).getWidth(g) / 2;
                    row.getMathElement(j).paint(g, xx, posY);
                }
                final int currentColSpacing = this.getColumnspacing(g, j);
                x += maxcolumnwidth[j];
                if ((i == 0) && (j < maxcolumns - 1)) {
                    // TODO: This only sets columnlines if the first row
                    // covers all the columns. Maybe this needs to be changed?
                    columnlines.add(x + currentColSpacing / 2.0f);
                }
                x += currentColSpacing;
            }

            posY += maxrowdescentheight[i];
            final int currentRowSpacing = this.getRowspacing(g, i);
            if (i < (this.getMathElementCount() - 1)) {
                rowlines.add(posY + currentRowSpacing / 2.0f);
            }
            posY += currentRowSpacing;
        }
        int col = 0;
        final Stroke oldStroke = g.getStroke();
        // TODO: Make size dependent.
        final Stroke solidStroke = new BasicStroke(1.0f);
        // TODO: Make size dependent.
        final Stroke dashedStroke = new BasicStroke(1.0f,
                BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL, 1.0f,
                new float[] { 3.0f, 3.0f }, 0);
        for (final float lineX : columnlines) {
            final LineType lt = this.getColumnLine(col);
            col++;
            if (MathTable.LineType.SOLID.equals(lt)) {
                g.setStroke(solidStroke);
            } else if (MathTable.LineType.DASHED.equals(lt)) {
                g.setStroke(dashedStroke);
            }
            if (!MathTable.LineType.NONE.equals(lt)) {
                g.draw(new Line2D.Float(lineX, startY, lineX, posY));
            }
        }
        int row = 0;
        for (final float lineY : rowlines) {
            // TODO: This only works if the last entry has all column. Maybe
            // this needs to be changed?
            final LineType lt = this.getRowLine(row);
            row++;
            // TODO: This code is very similar to the one for columnlines.
            // Refactor!
            if (MathTable.LineType.SOLID.equals(lt)) {
                g.setStroke(solidStroke);
            } else if (MathTable.LineType.DASHED.equals(lt)) {
                g.setStroke(dashedStroke);
            }
            if (!MathTable.LineType.NONE.equals(lt)) {
                g.draw(new Line2D.Float(x1, lineY, x, lineY));
            }
        }
        g.setStroke(oldStroke);
    }

    /**
     * Returns the maximal ascent height of a row in this table.
     * 
     * @param row
     *            Row.
     * @return Maximal ascent height.
     */
    private int getMaxRowAscentHeight(final Graphics2D g, final int row) {
        if (row >= this.getMathElementCount()) {
            return 0;
        }
        final MathElement child = this.getMathElement(row);
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
    private int getMaxRowDescentHeight(final Graphics2D g, final int row) {
        if (row >= this.getMathElementCount()) {
            return 0;
        }

        final MathElement child = this.getMathElement(row);
        int height = 0;

        for (int i = 0; i < child.getMathElementCount(); i++) {
            height = Math.max(height, child.getMathElement(i)
                    .getDescentHeight(g));
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
    private int getMaxColumnWidth(final Graphics2D g, final int column) {
        int width = 0;

        for (int i = 0; i < this.getMathElementCount(); i++) {
            final MathElement child = this.getMathElement(i); // row

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

        for (int i = 0; i < this.getMathElementCount(); i++) {
            final MathElement child = this.getMathElement(i);
            count = Math.max(count, child.getMathElementCount());
        }
        return count;
    }

    /** {@inheritDoc} */
    @Override
    public int calculateWidth(final Graphics2D g) {
        this.calculateAlignmentGroups(g);
        int width = 0;
        final int maxcolumns = this.getMaxColumnCount();

        for (int i = 0; i < maxcolumns; i++) {
            width = width + this.getMaxColumnWidth(g, i);
            if (i + 1 < maxcolumns) {
                width = width + this.getColumnspacing(g, i);
            }
        }
        width = width + this.getFramespacingh() * 2;
        return width;
    }

    private int calculateActualHeight(final Graphics2D g) {
        int height = 0;
        final int mec = this.getMathElementCount();
        for (int i = 0; i < mec; i++) {
            height = height + this.getMaxRowAscentHeight(g, i)
                    + this.getMaxRowDescentHeight(g, i);
            if (i + 1 < mec) {
                height = height + this.getRowspacing(g, i);
            }
        }
        height = height + this.getFramespacingv() * 2;
        return height;
    }

    /** {@inheritDoc} */
    @Override
    public int calculateAscentHeight(final Graphics2D g) {
        if (this.getAlign() == MathTable.ALIGN_BOTTOM) {
            return this.calculateActualHeight(g);
        }
        if (this.getAlign() == MathTable.ALIGN_TOP) {
            return this.getRowCount() > 0 ? this.getMaxRowAscentHeight(g, 0)
                    : 0;
        }
        if (this.getAlign() == MathTable.ALIGN_AXIS) {
            // add +1 to eliminate rounding errors
            return (this.calculateActualHeight(g) + 1) / 2;
        }
        // baseline or center
        // add +1 to eliminate rounding errors
        return (this.calculateActualHeight(g) + 1) / 2
                + this.getMiddleShift(g);
    }

    /** {@inheritDoc} */
    @Override
    public int calculateDescentHeight(final Graphics2D g) {
        if (this.getAlign() == MathTable.ALIGN_BOTTOM) {
            return 0;
        }
        if (this.getAlign() == MathTable.ALIGN_TOP) {
            return this.calculateActualHeight(g)
                    - (this.getRowCount() > 0 ? this.getMaxRowAscentHeight(g,
                            0) : 0);
        }
        if (this.getAlign() == MathTable.ALIGN_AXIS) {
            // add +1 to eliminate rounding errors
            return (this.calculateActualHeight(g) + 1) / 2;
        }
        final int b = this.getMiddleShift(g);
        // add +1 to eliminate rounding errors
        final int c = (this.calculateActualHeight(g) + 1) / 2;
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
    public void setGroupAlign(final String groupalign) {
        this.groupsalignvalues = MathTable.createGroupAlignValues(groupalign);
    }

    /**
     * Creates array with alignments according string in parameter.
     * 
     * @param groupalign
     *            String with value of attribute "groupalign"
     * @return Array with alignment constants
     */
    public static int[] createGroupAlignValues(final String groupalign) {
        final List<Integer> alignArray = new Vector<Integer>();
        String alignString = groupalign.trim();
        alignString = alignString.substring(1, alignString.length() - 1)
                + " ";

        // parsing string and filling out ArrayList object
        int spaceIndex = alignString.indexOf(" ");
        while (spaceIndex > 0) {
            final String valueString = alignString.substring(0, spaceIndex)
                    .toLowerCase();
            int valueInt = MathTable.ALIGN_LEFT;

            if (valueString.equals("center")) { // 2
                valueInt = MathTable.ALIGN_CENTER;
            } else {
                if (valueString.equals("left")) { // 5
                    valueInt = MathTable.ALIGN_LEFT;
                } else {
                    if (valueString.equals("right")) { // 6
                        valueInt = MathTable.ALIGN_RIGHT;
                    } else {
                        if (valueString.equals("decimalpoint")) { // 7
                            valueInt = MathTable.ALIGN_DECIMALPOINT;
                        }
                    }
                }
            }

            alignArray.add(new Integer(valueInt));
            alignString = alignString.substring(spaceIndex + 1);
            spaceIndex = alignString.indexOf(" ");
        }

        // copy to internal array
        final int[] result = new int[alignArray.size()];
        for (int i = 0; i < alignArray.size(); i++) {
            result[i] = (alignArray.get(i)).intValue();
        }

        return result;
    }

    /**
     * Get array with group alignment constants.
     * 
     * @return Array with group alignment constants.
     */
    public int[] getGroupAlign() {
        return this.groupsalignvalues;
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
    public int[] getGroupAlign(final MathTableData cell) {
        int[] result = null;

        if (cell == null) {
            return new int[0];
        }

        if (cell.getGroupAlignArray() == null
                || cell.getGroupAlignArray().length == 0) {
            result = ((MathTableRow) cell.getParent()).getGroupAlignArray();
        } else {
            result = cell.getGroupAlignArray();
        }

        if (result == null) {
            result = this.getGroupAlign();
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
        final int rowsCount = this.getRowCount();
        final int columnsCount = this.getMaxColumnCount();
        int tmpLength = 0;

        for (int column = 0; column < columnsCount; column++) {
            for (int row = 0; row < rowsCount; row++) {
                MathTableData cell = null;
                try {
                    cell = this.getCell(row, column);
                } catch (final Exception e) {
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
        return this.getMathElementCount();
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
    private MathTableData getCell(final int row, final int column)
            throws Exception {
        final int rowsCount = this.getRowCount();
        final int columnsCount = this.getMaxColumnCount();
        MathTableData cell = null;

        if (row > rowsCount - 1 || column > columnsCount - 1) {
            return cell;
        }

        final MathElement theRow = this.getMathElement(row); // row
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
    private MathAlignGroup[] getAlignGroups(final MathTableData cell) {
        MathAlignGroup[] result;

        final List<MathAlignGroup> list = cell.getAlignGroups();

        if (list.size() == 0) {
            result = new MathAlignGroup[0];
        } else {
            // copy to result array
            result = new MathAlignGroup[list.size()];
            for (int i = 0; i < list.size(); i++) {
                result[i] = (list.get(i));
            }
        }

        return result;
    }

    /**
     * Constant for calculating of align elements widths.
     */
    private static final int WHOLE_WIDTH = 0;

    /**
     * Constant for calculating of align elements widths.
     */
    private static final int LEFT_WIDTH = 1;

    /**
     * Constant for calculating of align elements widths.
     */
    private static final int RIGHT_WIDTH = 2;

    /**
     * Method calculates widths of alignment elements. Must be called after
     * the calculating of all MathElements widths.
     * 
     * @param g
     *            Graphics2D context to use.
     */
    protected void calculateAlignmentGroups(final Graphics2D g) {
        final int rowsCount = this.getRowCount();
        final int columnsCount = this.getMaxColumnCount();
        final int MAX_WIDTH_IN_COLUMN = rowsCount;
        final int maxGroupAlignCount = this.getMaxGroupAlignCount();
        if (maxGroupAlignCount == 0) {
            return;
        }
        // structure to hold calculated temp values, for each element from
        // align
        // group
        // [0] whole width value
        // [1] left width value (till malignmark or decimal point)
        // [2] right width value
        // rowsCount+1 - max width of element (or its part)
        final int[][][][] alignwidths = new int[columnsCount][3][rowsCount + 1][maxGroupAlignCount];
        // need to know, either this column of aligngroup uses malignmarks
        final boolean[][] usesMarks = new boolean[columnsCount][this
                .getMaxGroupAlignCount()];
        // array of aligngropus of the cell
        MathAlignGroup[] aligngroups; // align values of aligngroups in the
        // cell
        int[] groupalignvalues; // elements of the aligngroup
        List<MathElement> elements;
        for (int col = 0; col < columnsCount; col++) { // walking through the
            // column "column"
            for (int row = 0; row < rowsCount; row++) {
                MathTableData cell = null;
                try {
                    cell = this.getCell(row, col);
                } catch (final Exception e) {
                    cell = null;
                }
                if (cell != null) { // all align components of the cell
                    aligngroups = this.getAlignGroups(cell);
                    groupalignvalues = this.getGroupAlign(cell);
                    if (groupalignvalues.length == 0) {
                        // values of alignment didn't mentioned, have to use
                        // default
                        groupalignvalues = new int[aligngroups.length];
                        for (int i = 0; i < aligngroups.length; i++) {
                            groupalignvalues[i] = MathTable.ALIGN_LEFT;
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
                        alignwidths[col][MathTable.WHOLE_WIDTH][row][alignIndex] = MathAlignGroup
                                .getElementsWholeWidth(g, elements);
                        // max whole width of group
                        if (alignwidths[col][MathTable.WHOLE_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex] < alignwidths[col][MathTable.WHOLE_WIDTH][row][alignIndex]) {
                            alignwidths[col][MathTable.WHOLE_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex] = alignwidths[col][MathTable.WHOLE_WIDTH][row][alignIndex];
                        }
                        // there is alignmark in the group
                        if (aligngroups[alignIndex].getMark() != null) {
                            usesMarks[col][alignIndex] = true;
                            final int leftPart = this.getWidthTillMark(g,
                                    elements.iterator());
                            // left width = width till alignmark
                            alignwidths[col][MathTable.LEFT_WIDTH][row][alignIndex] = leftPart;
                            // right width = whole width - left width
                            alignwidths[col][MathTable.RIGHT_WIDTH][row][alignIndex] = alignwidths[col][MathTable.WHOLE_WIDTH][row][alignIndex]
                                    - leftPart;
                            // max left width
                            if (alignwidths[col][MathTable.LEFT_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex] < leftPart) {
                                alignwidths[col][MathTable.LEFT_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex] = leftPart;
                            }
                            // max right width
                            if (alignwidths[col][MathTable.RIGHT_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex] < alignwidths[col][MathTable.RIGHT_WIDTH][row][alignIndex]) {
                                alignwidths[col][MathTable.RIGHT_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex] = alignwidths[col][MathTable.RIGHT_WIDTH][row][alignIndex];
                            }
                        } else {
                            // there is no alignmark, right width is equal to
                            // the whole width
                            alignwidths[col][MathTable.LEFT_WIDTH][row][alignIndex] = 0;
                            alignwidths[col][MathTable.RIGHT_WIDTH][row][alignIndex] = alignwidths[col][MathTable.WHOLE_WIDTH][row][alignIndex];
                            // but! may be, we have alignment decimalpoint...
                            if (groupalignvalues[alignIndex] == MathTable.ALIGN_DECIMALPOINT) {
                                // width of the left (till decimal point)
                                // value
                                // of MathNumber
                                final int tillPoint = this.getWidthTillPoint(
                                        g, elements.iterator());
                                final int pointWidth = this.getPointWidth(g,
                                        elements.iterator());
                                // left width
                                alignwidths[col][MathTable.LEFT_WIDTH][row][alignIndex] = tillPoint;
                                // determine max left width till decimal point
                                if (alignwidths[col][MathTable.LEFT_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex] < tillPoint) {
                                    alignwidths[col][MathTable.LEFT_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex] = tillPoint;
                                }
                                // calculate right part
                                alignwidths[col][MathTable.RIGHT_WIDTH][row][alignIndex] = alignwidths[col][MathTable.WHOLE_WIDTH][row][alignIndex]
                                        - tillPoint - pointWidth;
                                // calculate right max
                                if (alignwidths[col][MathTable.RIGHT_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex] < alignwidths[col][MathTable.RIGHT_WIDTH][row][alignIndex]) {
                                    alignwidths[col][MathTable.RIGHT_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex] = alignwidths[col][MathTable.RIGHT_WIDTH][row][alignIndex];
                                }
                            }
                        } // no align mark in this align group
                    } // cycle: align groups in the cell
                } // if cell != null
            } // cycle: rows
        } // cycle: columns

        // calculating shifts of MathAlignGroup elements
        int alignOfTheGroup = MathTable.ALIGN_LEFT;
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
                    cell = this.getCell(row, col);
                } catch (final Exception e) {
                    cell = null;
                }

                if (cell != null) {
                    aligngroups = this.getAlignGroups(cell);
                    groupalignvalues = this.getGroupAlign(cell);
                    if (groupalignvalues.length == 0) {
                        // values of alignment didn't mentioned, have to use
                        // default
                        groupalignvalues = new int[aligngroups.length];
                        for (int i = 0; i < aligngroups.length; i++) {
                            groupalignvalues[i] = MathTable.ALIGN_LEFT;
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
                        currentWidth = alignwidths[col][MathTable.WHOLE_WIDTH][row][alignIndex];
                        maxWidth = alignwidths[col][MathTable.WHOLE_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex];
                        leftWidth = alignwidths[col][MathTable.LEFT_WIDTH][row][alignIndex];
                        leftMaxWidth = alignwidths[col][MathTable.LEFT_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex];
                        rightWidth = alignwidths[col][MathTable.RIGHT_WIDTH][row][alignIndex];
                        rightMaxWidth = alignwidths[col][MathTable.RIGHT_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex];
                        group = aligngroups[alignIndex];
                        if (usesMarks[col][alignIndex]) {
                            alignOfTheGroup = MathTable.ALIGN_MARK;
                        }
                        if (alignIndex < groupalignvalues.length - 1) {
                            nextGroup = aligngroups[alignIndex + 1];
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

        protected ChildIterator(final org.w3c.dom.NodeList nl) {
            this.nodeList = nl;
        }

        public boolean hasNext() {
            return this.pos < this.nodeList.getLength();
        }

        public Object next() {
            this.pos++;
            return this.nodeList.item(this.pos - 1);
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
    private int getWidthTillPoint(final Graphics2D g, final Iterator elements) {
        int result = 0;

        MathElement element = null;
        for (; elements.hasNext();) {
            element = (MathElement) elements.next();
            if (element instanceof MathNumber) {
                return result + ((MathNumber) element).getWidthTillPoint(g);
            } else {
                if (!this.containsNumber(element)) {
                    result += element.getWidth(g);
                } else {
                    result += this.getWidthTillPoint(g, new ChildIterator(
                            this.getChildNodes()));

                }
            }
        }

        return result;
    }

    /*
     * Checks, whether provided element contains any mn - element. @return
     * True, if contains any mn element.
     */
    private boolean containsNumber(final MathElement element) {
        return this.containsElement(element, MathNumber.ELEMENT);
    }

    /**
     * Finds in the list of the element mn element and requests it for the
     * with of the '.' character (used for the "decimalpoint" alignment).
     * 
     * @param iterator
     *            List of elements.
     * @return Width of the point.
     */
    private int getPointWidth(final Graphics2D g, final Iterator iterator) {
        int result = 0;

        for (; iterator.hasNext();) {
            final MathElement element = (MathElement) iterator.next();
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
    private int getWidthTillMark(final Graphics2D g, final Iterator elements) {
        int result = 0;

        MathElement element = null;
        for (; elements.hasNext();) {
            element = (MathElement) elements.next();
            if (element instanceof MathAlignMark) {
                return result;
            } else {
                if (!this.containsMark(element)) {
                    result += element.getWidth(g);
                } else {
                    result += this.getWidthTillMark(g, new ChildIterator(this
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
    private boolean containsMark(final MathElement element) {
        return this.containsElement(element, MathAlignMark.ELEMENT);
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
    private boolean containsElement(final MathElement container,
            final String searchName) {
        if (container.getTagName().equals(searchName)) {
            return true;
        }

        for (int i = 0; i < container.getMathElementCount(); i++) {
            if (this.containsElement(container.getMathElement(i), searchName)) {
                return true;
            }
        }

        return false;
    }

    /** {@inheritDoc} */
    public String getTagName() {
        return MathTable.ELEMENT;
    }

    /** {@inheritDoc} */
    public String getRowlines() {
        return this.getMathAttribute(MathTable.ATTR_ROWLINES);
    }

    /** {@inheritDoc} */
    public void setRowlines(final String rowlines) {
        this.setAttribute(MathTable.ATTR_ROWLINES, rowlines);
    }

    /** {@inheritDoc} */
    public String getColumnlines() {
        return this.getMathAttribute(MathTable.ATTR_COLUMNLINES);
    }

    /** {@inheritDoc} */
    public void setColumnlines(final String columnlines) {
        this.setAttribute(MathTable.ATTR_COLUMNLINES, columnlines);
    }

    private LineType getRowLine(final int row) {
        return MathTable.LineType.parseLineType(this.getSpaceArrayEntry(this
                .getRowlines(), row));
    }

    private LineType getColumnLine(final int col) {
        return MathTable.LineType.parseLineType(this.getSpaceArrayEntry(this
                .getColumnlines(), col));
    }

    private LineType getFrameAsLineType() {
        return MathTable.LineType.parseLineType(this.getFrame());
    }

    /**
     * Gets an entry in a white-space separated string.
     * <p>
     * If the entry requested is beyond the index, the last entry is returned.
     * 
     * @todo This method is probably useful for other attribute values.
     *       Examine, and move to a more common place. (like AttrHelper)
     * @param string
     *            the string in which to look.
     * @param index
     *            index of the element (0-based)
     * @return the element at that index
     */
    private String getSpaceArrayEntry(final String string, final int index) {
        final String[] array = string.split("\\s");
        int cur = -1;
        String last = "";
        for (final String s : array) {
            if (s.length() > 0) {
                cur++;
                if (cur == index) {
                    return s;
                }
                last = s;
            }
        }
        return last;
    }

    /** {@inheritDoc} */
    public String getColumnwidth() {
        return this.getMathAttribute(MathTable.ATTR_COLUMNWIDTH);
    }

    /** {@inheritDoc} */
    public void setColumnwidth(final String columnwidth) {
        this.setAttribute(MathTable.ATTR_COLUMNWIDTH, columnwidth);
    }

    /** {@inheritDoc} */
    public String getWidth() {
        return this.getMathAttribute(MathTable.ATTR_WIDTH);
    }

    /** {@inheritDoc} */
    public void setWidth(final String width) {
        this.setAttribute(MathTable.ATTR_WIDTH, width);
    }

    // public String getAlign();
    // public void setAlign(String align);
    // public String getRowalign();
    // public void setRowalign(String rowalign);
    // public String getColumnalign();
    // public void setColumnalign(String columnalign);
    // public String getGroupalign();
    // public void setGroupalign(String groupalign);
    // public String getAlignmentscope();
    // public void setAlignmentscope(String alignmentscope);

    // public String getRowspacing();
    // public void setRowspacing(String rowspacing);
    // public String getColumnspacing();
    // public void setColumnspacing(String columnspacing);

    /** {@inheritDoc} */
    public String getFrame() {
        return this.getMathAttribute(MathTable.ATTR_FRAME);
    }

    /** {@inheritDoc} */
    public void setFrame(final String frame) {
        this.setAttribute(MathTable.ATTR_FRAME, frame);
    }

    /** {@inheritDoc} */
    public String getFramespacing() {
        return this.getMathAttribute(MathTable.ATTR_FRAMESPACING);
    }

    /** {@inheritDoc} */
    public void setFramespacing(String framespacing) {
        this.setAttribute(MathTable.ATTR_FRAMESPACING, framespacing);
    }

    /** {@inheritDoc} */
    public String getEqualrows() {
        return this.getMathAttribute(MathTable.ATTR_EQUALROWS);
    }

    /** {@inheritDoc} */
    public void setEqualrows(final String equalrows) {
        this.setAttribute(MathTable.ATTR_EQUALROWS, equalrows);
    }

    /** {@inheritDoc} */
    public String getEqualcolumns() {
        return this.getMathAttribute(MathTable.ATTR_EQUALCOLUMNS);
    }

    /** {@inheritDoc} */
    public void setEqualcolumns(final String equalcolumns) {
        this.setAttribute(MathTable.ATTR_EQUALCOLUMNS, equalcolumns);
    }

    /** {@inheritDoc} */
    public String getDisplaystyle() {
        return this.getMathAttribute(MathTable.ATTR_DISPLAYSTYLE);
    }

    /** {@inheritDoc} */
    public void setDisplaystyle(final String displaystyle) {
        this.setAttribute(MathTable.ATTR_DISPLAYSTYLE, displaystyle);
    }

    /** {@inheritDoc} */
    public String getSide() {
        return this.getMathAttribute(MathTable.ATTR_SIDE);
    }

    /** {@inheritDoc} */
    public void setSide(final String side) {
        this.setAttribute(MathTable.ATTR_SIDE, side);
    }

    /** {@inheritDoc} */
    public String getMinlabelspacing() {
        return this.getMathAttribute(MathTable.ATTR_MINLABELSPACING);
    }

    /** {@inheritDoc} */
    public void setMinlabelspacing(final String minlabelspacing) {
        this.setAttribute(MathTable.ATTR_MINLABELSPACING, minlabelspacing);
    }

    // public MathMLNodeList getRows();
    // public MathMLTableRowElement insertEmptyRow(int index)
    // throws DOMException;
    // public MathMLLabeledRowElement insertEmptyLabeledRow(int index)
    // throws DOMException;
    // public MathMLTableRowElement getRow(int index);
    // public MathMLTableRowElement insertRow(int index,
    // MathMLTableRowElement newRow)
    // throws DOMException;
    // public MathMLTableRowElement setRow(int index,
    // MathMLTableRowElement newRow)
    // throws DOMException;
    // public void deleteRow(int index)
    // throws DOMException;
    // public MathMLTableRowElement removeRow(int index)
    // throws DOMException;

}
