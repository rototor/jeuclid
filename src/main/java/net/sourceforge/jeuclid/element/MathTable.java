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

import org.w3c.dom.mathml.MathMLLabeledRowElement;
import org.w3c.dom.mathml.MathMLNodeList;
import org.w3c.dom.mathml.MathMLTableElement;
import org.w3c.dom.mathml.MathMLTableRowElement;

/**
 * This class presents a table.
 * 
 * @author Unknown
 * @author Max Berger
 */
public class MathTable extends AbstractMathElement implements
        MathMLTableElement {

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
     * Default frame spacing.
     */
    private static final String DEFAULT_FRAMESPACING = "0.4em 0.5ex";

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
     * Class for alignment types.
     */
    public enum AlignmentType {
        /** Align to top. */
        TOP,
        /** Align to bottom. */
        BOTTOM,
        /** Align to center. */
        CENTER,
        /** Align to baseline. */
        BASELINE,
        /** Align to axis. */
        AXIS,
        /** Align to left. */
        LEFT,
        /** Align to right. */
        RIGHT,
        /** Align to decimalpoint. */
        DECIMALPOINT,
        /** Align to alignment markers. */
        MARK;

        /**
         * Parse a string and return a alignment.
         * 
         * @param s
         *            the string to parse
         * @return an alignment for this string type
         */
        public static AlignmentType parseAlignmentType(final String s) {
            final AlignmentType retVal;
            if ("top".equalsIgnoreCase(s)) {
                retVal = MathTable.AlignmentType.TOP;
            } else if ("bottom".equalsIgnoreCase(s)) {
                retVal = MathTable.AlignmentType.BOTTOM;
            } else if ("baseline".equalsIgnoreCase(s)) {
                retVal = MathTable.AlignmentType.BASELINE;
            } else if ("axis".equalsIgnoreCase(s)) {
                retVal = MathTable.AlignmentType.AXIS;
            } else if ("left".equalsIgnoreCase(s)) {
                retVal = MathTable.AlignmentType.LEFT;
            } else if ("right".equalsIgnoreCase(s)) {
                retVal = MathTable.AlignmentType.RIGHT;
            } else if ("decimalpoint".equalsIgnoreCase(s)) {
                retVal = MathTable.AlignmentType.DECIMALPOINT;
            } else {
                retVal = MathTable.AlignmentType.CENTER;
            }
            return retVal;
        }
    }

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
        this.setDefaultMathAttribute(MathTable.ATTR_ROWSPACING,
                MathTable.DEFAULT_ROWSPACING);
        this.setDefaultMathAttribute(MathTable.ATTR_COLUMNSPACING,
                MathTable.DEFAULT_COLUMNSPACING);
        this.setDefaultMathAttribute(MathTable.ATTR_ROWLINES,
                MathTable.VALUE_NONE);
        this.setDefaultMathAttribute(MathTable.ATTR_COLUMNLINES,
                MathTable.VALUE_NONE);
        this.setDefaultMathAttribute(MathTable.ATTR_FRAME,
                MathTable.VALUE_NONE);
        this.setDefaultMathAttribute(MathTable.ATTR_FRAMESPACING,
                MathTable.DEFAULT_FRAMESPACING);
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
     * 
     * @return Horizontal frame spacing
     */
    protected float getFramespacingh() {
        if (MathTable.LineType.NONE.equals(this.getFrameAsLineType())) {
            return 0;
        }
        final String spacing = this.getSpaceArrayEntry(
                this.getFramespacing(), 0);
        return AttributesHelper.convertSizeToPt(spacing, this,
                AttributesHelper.PT);
    }

    /**
     * 
     * @return Vertical frame spacing
     */
    protected float getFramespacingv() {
        if (MathTable.LineType.NONE.equals(this.getFrameAsLineType())) {
            return 0;
        }
        final String spacing = this.getSpaceArrayEntry(
                this.getFramespacing(), 1);
        return AttributesHelper.convertSizeToPt(spacing, this,
                AttributesHelper.PT);
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
    public void paint(final Graphics2D g, float posX, float posY) {
        super.paint(g, posX, posY);
        posX = posX + this.getFramespacingh();
        posY = posY + this.getFramespacingv();

        int i;
        int j;
        final float[] maxrowascentheight = new float[this
                .getMathElementCount()];
        final float[] maxrowdescentheight = new float[this
                .getMathElementCount()];

        for (i = 0; i < this.getMathElementCount(); i++) {
            maxrowascentheight[i] = this.getMaxRowAscentHeight(g, i);
            maxrowdescentheight[i] = this.getMaxRowDescentHeight(g, i);
        }

        final int maxcolumns = this.getMaxColumnCount();
        final boolean isAlignGroupsExist = this.getMaxGroupAlignCount() == 0 ? false
                : true;
        final float[] maxcolumnwidth = new float[maxcolumns];

        for (i = 0; i < maxcolumns; i++) {
            maxcolumnwidth[i] = this.getMaxColumnWidth(g, i);
        }

        final float x1 = posX;
        float x = x1;

        posY = posY - this.getAscentHeight(g);
        final float startY = posY;

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
                    final float xx = x + maxcolumnwidth[j] / 2
                            - row.getMathElement(j).getWidth(g) / 2;
                    row.getMathElement(j).paint(g, xx, posY);
                }
                final float currentColSpacing = this.getColumnspacing(j);
                x += maxcolumnwidth[j];
                if ((i == 0) && (j < maxcolumns - 1)) {
                    // TODO: This only sets columnlines if the first row
                    // covers all the columns. Maybe this needs to be changed?
                    columnlines.add(x + currentColSpacing / 2.0f);
                }
                x += currentColSpacing;
            }

            posY += maxrowdescentheight[i];
            final float currentRowSpacing = this.getRowspacing(i);
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
    private float getMaxRowAscentHeight(final Graphics2D g, final int row) {
        if (row >= this.getMathElementCount()) {
            return 0;
        }
        final MathElement child = this.getMathElement(row);
        float height = 0;

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
    private float getMaxRowDescentHeight(final Graphics2D g, final int row) {
        if (row >= this.getMathElementCount()) {
            return 0;
        }

        final MathElement child = this.getMathElement(row);
        float height = 0;

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
    private float getMaxColumnWidth(final Graphics2D g, final int column) {
        float width = 0;

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
    public float calculateWidth(final Graphics2D g) {
        this.calculateAlignmentGroups(g);
        float width = 0;
        final int maxcolumns = this.getMaxColumnCount();

        for (int i = 0; i < maxcolumns; i++) {
            width = width + this.getMaxColumnWidth(g, i);
            if (i + 1 < maxcolumns) {
                width = width + this.getColumnspacing(i);
            }
        }
        width = width + this.getFramespacingh() * 2;
        return width;
    }

    private float calculateActualHeight(final Graphics2D g) {
        float height = 0;
        final int mec = this.getMathElementCount();
        for (int i = 0; i < mec; i++) {
            height = height + this.getMaxRowAscentHeight(g, i)
                    + this.getMaxRowDescentHeight(g, i);
            if (i + 1 < mec) {
                height = height + this.getRowspacing(i);
            }
        }
        height = height + this.getFramespacingv() * 2;
        return height;
    }

    /** {@inheritDoc} */
    @Override
    public float calculateAscentHeight(final Graphics2D g) {
        final AlignmentType align = AlignmentType.parseAlignmentType(this
                .getAlign());
        if (MathTable.AlignmentType.BOTTOM.equals(align)) {
            return this.calculateActualHeight(g);
        } else if (MathTable.AlignmentType.TOP.equals(align)) {
            return this.getRowCount() > 0 ? this.getMaxRowAscentHeight(g, 0)
                    : 0;
        } else if (MathTable.AlignmentType.AXIS.equals(align)) {
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
    public float calculateDescentHeight(final Graphics2D g) {
        final AlignmentType align = AlignmentType.parseAlignmentType(this
                .getAlign());
        if (MathTable.AlignmentType.BOTTOM.equals(align)) {
            return 0;
        } else if (MathTable.AlignmentType.TOP.equals(align)) {
            return this.calculateActualHeight(g)
                    - (this.getRowCount() > 0 ? this.getMaxRowAscentHeight(g,
                            0) : 0);
        } else if (MathTable.AlignmentType.AXIS.equals(align)) {
            // add +1 to eliminate rounding errors
            return (this.calculateActualHeight(g) + 1) / 2;
        }
        final float b = this.getMiddleShift(g);
        // add +1 to eliminate rounding errors
        final float c = (this.calculateActualHeight(g) + 1) / 2;
        return c - b;
    }

    /*
     * ----------------- NEW FUNCTIONS, CONCERNED <MALIGNGROUP> ELEMENT
     * -------------------
     */

    /**
     * Retrieves groupalign values from mtd element. If requested cell doesn't
     * contain groupalign attribute, the current row (and after it current
     * table) will be requested.
     * 
     * @param cell
     *            Cell to get groupalign values.
     * @return Array with groupalign values.
     */
    public List<AlignmentType> getGroupAlign(final MathTableData cell) {
        final List<AlignmentType> result = new Vector<AlignmentType>();

        String groupAlign;

        if (cell == null) {
            return result;
        }
        final String cellGroupAlign = cell.getGroupalign();
        if (cellGroupAlign == null || cellGroupAlign.length() == 0) {
            groupAlign = ((MathTableRow) cell.getParent()).getGroupalign();
        } else {
            groupAlign = cell.getGroupalign();
        }

        if (groupAlign == null) {
            groupAlign = this.getGroupalign();
        }

        if (groupAlign != null) {

            final String[] gAlign = groupAlign.split("\\w");
            for (final String value : gAlign) {
                if (value.length() > 0) {
                    result.add(AlignmentType.parseAlignmentType(value));
                }
            }
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
        final float[][][][] alignwidths = new float[columnsCount][3][rowsCount + 1][maxGroupAlignCount];
        // need to know, either this column of aligngroup uses malignmarks
        final boolean[][] usesMarks = new boolean[columnsCount][this
                .getMaxGroupAlignCount()];
        // array of aligngropus of the cell
        MathAlignGroup[] aligngroups; // align values of aligngroups in the
        // cell
        List<AlignmentType> groupalignvalues; // elements of the aligngroup
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
                    if (groupalignvalues.size() == 0) {
                        // values of alignment didn't mentioned, have to use
                        // default
                        groupalignvalues = new Vector<AlignmentType>(
                                aligngroups.length);
                        for (MathAlignGroup element : aligngroups) {
                            groupalignvalues.add(AlignmentType.LEFT);
                        }
                    }
                    if (aligngroups.length == 0
                            || aligngroups.length < groupalignvalues.size()) {
                        continue; // there is no aligngroups in the cell or
                        // wrong count
                    }
                    for (int alignIndex = 0; alignIndex < groupalignvalues
                            .size(); alignIndex++) {
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
                            final float leftPart = this.getWidthTillMark(g,
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
                            if (groupalignvalues.get(alignIndex).equals(
                                    AlignmentType.DECIMALPOINT)) {
                                // width of the left (till decimal point)
                                // value
                                // of MathNumber
                                final float tillPoint = this
                                        .getWidthTillPoint(g, elements
                                                .iterator());
                                final float pointWidth = this.getPointWidth(
                                        g, elements.iterator());
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
        AlignmentType alignOfTheGroup = AlignmentType.LEFT;
        float currentWidth = 0;
        float maxWidth = 0;
        float leftWidth = 0;
        float leftMaxWidth = 0;
        float rightWidth = 0;
        float rightMaxWidth = 0;
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
                    if (groupalignvalues.size() == 0) {
                        // values of alignment didn't mentioned, have to use
                        // default
                        groupalignvalues = new Vector<AlignmentType>(
                                aligngroups.length);
                        for (MathAlignGroup element : aligngroups) {
                            groupalignvalues.add(AlignmentType.LEFT);
                        }
                    }
                    if (aligngroups.length == 0
                            || aligngroups.length < groupalignvalues.size()) {
                        continue;
                    }
                    for (int alignIndex = 0; alignIndex < groupalignvalues
                            .size(); alignIndex++) {
                        // retrieving previously calculated information about
                        // aligngroups widths
                        alignOfTheGroup = groupalignvalues.get(alignIndex);
                        currentWidth = alignwidths[col][MathTable.WHOLE_WIDTH][row][alignIndex];
                        maxWidth = alignwidths[col][MathTable.WHOLE_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex];
                        leftWidth = alignwidths[col][MathTable.LEFT_WIDTH][row][alignIndex];
                        leftMaxWidth = alignwidths[col][MathTable.LEFT_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex];
                        rightWidth = alignwidths[col][MathTable.RIGHT_WIDTH][row][alignIndex];
                        rightMaxWidth = alignwidths[col][MathTable.RIGHT_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex];
                        group = aligngroups[alignIndex];
                        if (usesMarks[col][alignIndex]) {
                            alignOfTheGroup = AlignmentType.MARK;
                        }
                        if (alignIndex < groupalignvalues.size() - 1) {
                            nextGroup = aligngroups[alignIndex + 1];
                        }
                        switch (alignOfTheGroup) {
                        case RIGHT:
                            group.width += maxWidth - currentWidth;
                            break;
                        case LEFT:
                            if (alignIndex < groupalignvalues.size() - 1) {
                                nextGroup.width += maxWidth - currentWidth;
                            }
                            break;
                        case CENTER:
                            group.width += (maxWidth - currentWidth) / 2;
                            if (alignIndex < groupalignvalues.size() - 1) {
                                nextGroup.width += (maxWidth - currentWidth) / 2;
                            }
                            break;
                        case DECIMALPOINT:
                        case MARK:
                            group.width += (leftMaxWidth - leftWidth);
                            if (alignIndex < groupalignvalues.size() - 1) {
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
    private float getWidthTillPoint(final Graphics2D g,
            final Iterator elements) {
        float result = 0;

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
    private float getPointWidth(final Graphics2D g, final Iterator iterator) {
        float result = 0;

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
    private float getWidthTillMark(final Graphics2D g, final Iterator elements) {
        float result = 0;

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

    /** {@inheritDoc} */
    public String getAlign() {
        return this.getMathAttribute(MathTable.ATTR_ALIGN);
    }

    /** {@inheritDoc} */
    public void setAlign(final String align) {
        this.setAttribute(MathTable.ATTR_ALIGN, align);
    }

    /** {@inheritDoc} */
    public String getRowalign() {
        return this.getMathAttribute(MathTable.ATTR_ROWALIGN);
    }

    /** {@inheritDoc} */
    public void setRowalign(final String rowalign) {
        this.setAttribute(MathTable.ATTR_ROWALIGN, rowalign);
    }

    /** {@inheritDoc} */
    public String getColumnalign() {
        return this.getMathAttribute(MathTable.ATTR_COLUMNALIGN);
    }

    /** {@inheritDoc} */
    public void setColumnalign(final String columnalign) {
        this.setAttribute(MathTable.ATTR_COLUMNALIGN, columnalign);

    }

    /** {@inheritDoc} */
    public String getGroupalign() {
        return this.getMathAttribute(MathTable.ATTR_GROUPALIGN);
    }

    /** {@inheritDoc} */
    public void setGroupalign(final String groupalign) {
        this.setAttribute(MathTable.ATTR_GROUPALIGN, groupalign);
    }

    /** {@inheritDoc} */
    public String getAlignmentscope() {
        return this.getMathAttribute(MathTable.ATTR_ALIGNMENTSCOPE);
    }

    /** {@inheritDoc} */
    public void setAlignmentscope(final String alignmentscope) {
        this.setAttribute(MathTable.ATTR_ALIGNMENTSCOPE, alignmentscope);
    }

    /** {@inheritDoc} */
    public String getRowspacing() {
        return this.getMathAttribute(MathTable.ATTR_ROWSPACING);
    }

    private float getRowspacing(final int row) {
        return AttributesHelper.convertSizeToPt(this.getSpaceArrayEntry(this
                .getRowspacing(), row), this, AttributesHelper.PT);
    }

    /** {@inheritDoc} */
    public void setRowspacing(final String rowspacing) {
        this.setAttribute(MathTable.ATTR_ROWSPACING, rowspacing);
    }

    /** {@inheritDoc} */
    public String getColumnspacing() {
        return this.getMathAttribute(MathTable.ATTR_COLUMNSPACING);
    }

    private float getColumnspacing(final int column) {
        return AttributesHelper.convertSizeToPt(this.getSpaceArrayEntry(this
                .getColumnspacing(), column), this, AttributesHelper.PT);
    }

    /** {@inheritDoc} */
    public void setColumnspacing(final String columnspacing) {
        this.setAttribute(MathTable.ATTR_COLUMNSPACING, columnspacing);
    }

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
    public void setFramespacing(final String framespacing) {
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

    /** {@inheritDoc} */
    public MathMLNodeList getRows() {
        // TODO: Implement
        return null;
    }

    /** {@inheritDoc} */
    public MathMLTableRowElement insertEmptyRow(final int index) {
        // TODO: Implement
        return null;
    }

    /** {@inheritDoc} */
    public MathMLLabeledRowElement insertEmptyLabeledRow(final int index) {
        // TODO: Implement
        return null;
    }

    /** {@inheritDoc} */
    public MathMLTableRowElement getRow(final int index) {
        // TODO: Implement
        return null;
    }

    /** {@inheritDoc} */
    public MathMLTableRowElement insertRow(final int index,
            final MathMLTableRowElement newRow) {
        // TODO: Implement
        return null;
    }

    /** {@inheritDoc} */
    public MathMLTableRowElement setRow(final int index,
            final MathMLTableRowElement newRow) {
        // TODO: Implement
        return null;
    }

    /** {@inheritDoc} */
    public void deleteRow(final int index) {
        // TODO: Implement
    }

    /** {@inheritDoc} */
    public MathMLTableRowElement removeRow(final int index) {
        // TODO: Implement
        return null;
    }
}
