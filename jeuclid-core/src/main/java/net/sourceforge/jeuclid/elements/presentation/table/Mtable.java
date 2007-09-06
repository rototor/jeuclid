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

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Dimension2D;
import java.awt.geom.Line2D;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import net.sourceforge.jeuclid.LayoutContext;
import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.context.InlineLayoutContext;
import net.sourceforge.jeuclid.elements.AbstractJEuclidElement;
import net.sourceforge.jeuclid.elements.JEuclidElement;
import net.sourceforge.jeuclid.elements.JEuclidNode;
import net.sourceforge.jeuclid.elements.presentation.token.Mn;
import net.sourceforge.jeuclid.elements.support.Dimension2DImpl;
import net.sourceforge.jeuclid.elements.support.ElementListSupport;
import net.sourceforge.jeuclid.elements.support.GraphicsSupport;
import net.sourceforge.jeuclid.elements.support.attributes.AttributesHelper;
import net.sourceforge.jeuclid.layout.LayoutInfo;
import net.sourceforge.jeuclid.layout.LayoutStage;
import net.sourceforge.jeuclid.layout.LayoutView;

import org.w3c.dom.Node;
import org.w3c.dom.mathml.MathMLLabeledRowElement;
import org.w3c.dom.mathml.MathMLNodeList;
import org.w3c.dom.mathml.MathMLTableElement;
import org.w3c.dom.mathml.MathMLTableRowElement;

/**
 * This class presents a table.
 * 
 * @author Unknown
 * @author Max Berger
 * @version $Revision$
 */
public class Mtable extends AbstractJEuclidElement implements
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
            if (s.equalsIgnoreCase(Mtable.VALUE_NONE)) {
                retVal = NONE;
            } else if (s.equalsIgnoreCase(Mtable.VALUE_DASHED)) {
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
                retVal = Mtable.AlignmentType.TOP;
            } else if ("bottom".equalsIgnoreCase(s)) {
                retVal = Mtable.AlignmentType.BOTTOM;
            } else if ("baseline".equalsIgnoreCase(s)) {
                retVal = Mtable.AlignmentType.BASELINE;
            } else if ("axis".equalsIgnoreCase(s)) {
                retVal = Mtable.AlignmentType.AXIS;
            } else if ("left".equalsIgnoreCase(s)) {
                retVal = Mtable.AlignmentType.LEFT;
            } else if ("right".equalsIgnoreCase(s)) {
                retVal = Mtable.AlignmentType.RIGHT;
            } else if ("decimalpoint".equalsIgnoreCase(s)) {
                retVal = Mtable.AlignmentType.DECIMALPOINT;
            } else if ("center".equalsIgnoreCase(s)) {
                retVal = Mtable.AlignmentType.CENTER;
            } else {
                retVal = null;
            }
            return retVal;
        }
    }

    /**
     * Creates a math element.
     */
    public Mtable() {
        super();
        this.setDefaultMathAttribute(Mtable.ATTR_ALIGN, "axis");
        this.setDefaultMathAttribute(Mtable.ATTR_ROWALIGN, "baseline");
        this.setDefaultMathAttribute(Mtable.ATTR_COLUMNALIGN, "center");
        this.setDefaultMathAttribute(Mtable.ATTR_GROUPALIGN, "{left}");
        this.setDefaultMathAttribute(Mtable.ATTR_ALIGNMENTSCOPE,
                MathBase.TRUE);
        this.setDefaultMathAttribute(Mtable.ATTR_COLUMNWIDTH, "auto");
        this.setDefaultMathAttribute(Mtable.ATTR_WIDTH, "auto");
        this.setDefaultMathAttribute(Mtable.ATTR_ROWSPACING,
                Mtable.DEFAULT_ROWSPACING);
        this.setDefaultMathAttribute(Mtable.ATTR_COLUMNSPACING,
                Mtable.DEFAULT_COLUMNSPACING);
        this.setDefaultMathAttribute(Mtable.ATTR_ROWLINES, Mtable.VALUE_NONE);
        this.setDefaultMathAttribute(Mtable.ATTR_COLUMNLINES,
                Mtable.VALUE_NONE);
        this.setDefaultMathAttribute(Mtable.ATTR_FRAME, Mtable.VALUE_NONE);
        this.setDefaultMathAttribute(Mtable.ATTR_FRAMESPACING,
                Mtable.DEFAULT_FRAMESPACING);
        this.setDefaultMathAttribute(Mtable.ATTR_EQUALROWS, MathBase.FALSE);
        this
                .setDefaultMathAttribute(Mtable.ATTR_EQUALCOLUMNS,
                        MathBase.FALSE);
        this
                .setDefaultMathAttribute(Mtable.ATTR_DISPLAYSTYLE,
                        MathBase.FALSE);
        this.setDefaultMathAttribute(Mtable.ATTR_SIDE, "right");
        this.setDefaultMathAttribute(Mtable.ATTR_MINLABELSPACING, "0.8em");
    }

    /** {@inheritDoc} */
    @Override
    public LayoutContext getChildLayoutContext(final JEuclidNode child) {
        return new InlineLayoutContext(this.getCurrentLayoutContext());
    }

    /**
     * 
     * @return Horizontal frame spacing
     */
    protected float getFramespacingh() {
        if (Mtable.LineType.NONE.equals(this.getFrameAsLineType())) {
            return 0;
        }
        final String spacing = this.getSpaceArrayEntry(
                this.getFramespacing(), 0);
        return AttributesHelper.convertSizeToPt(spacing, this
                .getCurrentLayoutContext(), AttributesHelper.PT);
    }

    /**
     * 
     * @return Vertical frame spacing
     */
    protected float getFramespacingv() {
        if (Mtable.LineType.NONE.equals(this.getFrameAsLineType())) {
            return 0;
        }
        final String spacing = this.getSpaceArrayEntry(
                this.getFramespacing(), 1);
        return AttributesHelper.convertSizeToPt(spacing, this
                .getCurrentLayoutContext(), AttributesHelper.PT);
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
            final JEuclidElement row = this.getMathElement(i);
            posY += maxrowascentheight[i];

            x = x1;
            for (j = 0; (j < maxcolumns) && (j < row.getMathElementCount()); j++) {
                if (isAlignGroupsExist
                        && this.getAlignGroups((Mtd) row.getMathElement(j)).length > 0) {
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
        final float lineWidth = GraphicsSupport.lineWidth(this);
        final float dashWidth = 3.0f * lineWidth;
        final Stroke solidStroke = new BasicStroke(lineWidth);
        final Stroke dashedStroke = new BasicStroke(lineWidth,
                BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL, lineWidth,
                new float[] { dashWidth, dashWidth }, 0);
        for (final float lineX : columnlines) {
            final LineType lt = this.getColumnLine(col);
            col++;
            if (Mtable.LineType.SOLID.equals(lt)) {
                g.setStroke(solidStroke);
            } else if (Mtable.LineType.DASHED.equals(lt)) {
                g.setStroke(dashedStroke);
            }
            if (!Mtable.LineType.NONE.equals(lt)) {
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
            if (Mtable.LineType.SOLID.equals(lt)) {
                g.setStroke(solidStroke);
            } else if (Mtable.LineType.DASHED.equals(lt)) {
                g.setStroke(dashedStroke);
            }
            if (!Mtable.LineType.NONE.equals(lt)) {
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
        final JEuclidElement child = this.getMathElement(row);
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

        final JEuclidElement child = this.getMathElement(row);
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
            final JEuclidElement child = this.getMathElement(i); // row

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
            final JEuclidElement child = this.getMathElement(i);
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
        final AlignmentType align = Mtable.AlignmentType
                .parseAlignmentType(this.getAlign());
        if (Mtable.AlignmentType.BOTTOM.equals(align)) {
            return this.calculateActualHeight(g);
        } else if (Mtable.AlignmentType.TOP.equals(align)) {
            return this.getRowCount() > 0 ? this.getMaxRowAscentHeight(g, 0)
                    : 0;
        } else if (Mtable.AlignmentType.AXIS.equals(align)) {
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
        final Mtable.AlignmentType align = Mtable.AlignmentType
                .parseAlignmentType(this.getAlign());
        if (Mtable.AlignmentType.BOTTOM.equals(align)) {
            return 0;
        } else if (Mtable.AlignmentType.TOP.equals(align)) {
            return this.calculateActualHeight(g)
                    - (this.getRowCount() > 0 ? this.getMaxRowAscentHeight(g,
                            0) : 0);
        } else if (Mtable.AlignmentType.AXIS.equals(align)) {
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
    public List<Mtable.AlignmentType> getGroupAlign(final Mtd cell) {
        final List<Mtable.AlignmentType> result = new Vector<Mtable.AlignmentType>();

        String groupAlign;

        if (cell == null) {
            return result;
        }
        final String cellGroupAlign = cell.getGroupalign();
        if (cellGroupAlign == null || cellGroupAlign.length() == 0) {
            groupAlign = ((Mtr) cell.getParent()).getGroupalign();
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
                    result
                            .add(Mtable.AlignmentType
                                    .parseAlignmentType(value));
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
                Mtd cell = null;
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
    private Mtd getCell(final int row, final int column) throws Exception {
        final int rowsCount = this.getRowCount();
        final int columnsCount = this.getMaxColumnCount();
        Mtd cell = null;

        if (row > rowsCount - 1 || column > columnsCount - 1) {
            return cell;
        }

        final JEuclidElement theRow = this.getMathElement(row); // row
        if (column < theRow.getMathElementCount()) {
            if (theRow.getMathElement(column) instanceof Mtd) {
                cell = (Mtd) theRow.getMathElement(column);
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
    private Maligngroup[] getAlignGroups(final Mtd cell) {
        Maligngroup[] result;

        final List<Maligngroup> list = cell.getAlignGroups();

        if (list.size() == 0) {
            result = new Maligngroup[0];
        } else {
            // copy to result array
            result = new Maligngroup[list.size()];
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
        Maligngroup[] aligngroups; // align values of aligngroups in the
        // cell
        List<Mtable.AlignmentType> groupalignvalues; // elements of the
        // aligngroup
        List<JEuclidElement> elements;
        for (int col = 0; col < columnsCount; col++) { // walking through the
            // column "column"
            for (int row = 0; row < rowsCount; row++) {
                Mtd cell = null;
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
                        groupalignvalues = new Vector<Mtable.AlignmentType>(
                                aligngroups.length);
                        for (final Maligngroup element : aligngroups) {
                            groupalignvalues.add(Mtable.AlignmentType.LEFT);
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
                        elements = Maligngroup
                                .getElementsOfAlignGroup(aligngroups[alignIndex]);
                        alignwidths[col][Mtable.WHOLE_WIDTH][row][alignIndex] = Maligngroup
                                .getElementsWholeWidth(g, elements);
                        // max whole width of group
                        if (alignwidths[col][Mtable.WHOLE_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex] < alignwidths[col][Mtable.WHOLE_WIDTH][row][alignIndex]) {
                            alignwidths[col][Mtable.WHOLE_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex] = alignwidths[col][Mtable.WHOLE_WIDTH][row][alignIndex];
                        }
                        // there is alignmark in the group
                        if (aligngroups[alignIndex].getMark() != null) {
                            usesMarks[col][alignIndex] = true;
                            final float leftPart = this.getWidthTillMark(g,
                                    elements.iterator());
                            // left width = width till alignmark
                            alignwidths[col][Mtable.LEFT_WIDTH][row][alignIndex] = leftPart;
                            // right width = whole width - left width
                            alignwidths[col][Mtable.RIGHT_WIDTH][row][alignIndex] = alignwidths[col][Mtable.WHOLE_WIDTH][row][alignIndex]
                                    - leftPart;
                            // max left width
                            if (alignwidths[col][Mtable.LEFT_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex] < leftPart) {
                                alignwidths[col][Mtable.LEFT_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex] = leftPart;
                            }
                            // max right width
                            if (alignwidths[col][Mtable.RIGHT_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex] < alignwidths[col][Mtable.RIGHT_WIDTH][row][alignIndex]) {
                                alignwidths[col][Mtable.RIGHT_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex] = alignwidths[col][Mtable.RIGHT_WIDTH][row][alignIndex];
                            }
                        } else {
                            // there is no alignmark, right width is equal to
                            // the whole width
                            alignwidths[col][Mtable.LEFT_WIDTH][row][alignIndex] = 0;
                            alignwidths[col][Mtable.RIGHT_WIDTH][row][alignIndex] = alignwidths[col][Mtable.WHOLE_WIDTH][row][alignIndex];
                            // but! may be, we have alignment decimalpoint...
                            if (groupalignvalues.get(alignIndex).equals(
                                    Mtable.AlignmentType.DECIMALPOINT)) {
                                // width of the left (till decimal point)
                                // value
                                // of MathNumber
                                final float tillPoint = this
                                        .getWidthTillPoint(g, elements
                                                .iterator());
                                final float pointWidth = this.getPointWidth(
                                        g, elements.iterator());
                                // left width
                                alignwidths[col][Mtable.LEFT_WIDTH][row][alignIndex] = tillPoint;
                                // determine max left width till decimal point
                                if (alignwidths[col][Mtable.LEFT_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex] < tillPoint) {
                                    alignwidths[col][Mtable.LEFT_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex] = tillPoint;
                                }
                                // calculate right part
                                alignwidths[col][Mtable.RIGHT_WIDTH][row][alignIndex] = alignwidths[col][Mtable.WHOLE_WIDTH][row][alignIndex]
                                        - tillPoint - pointWidth;
                                // calculate right max
                                if (alignwidths[col][Mtable.RIGHT_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex] < alignwidths[col][Mtable.RIGHT_WIDTH][row][alignIndex]) {
                                    alignwidths[col][Mtable.RIGHT_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex] = alignwidths[col][Mtable.RIGHT_WIDTH][row][alignIndex];
                                }
                            }
                        } // no align mark in this align group
                    } // cycle: align groups in the cell
                } // if cell != null
            } // cycle: rows
        } // cycle: columns

        // calculating shifts of MathAlignGroup elements
        Mtable.AlignmentType alignOfTheGroup = Mtable.AlignmentType.LEFT;
        float currentWidth = 0;
        float maxWidth = 0;
        float leftWidth = 0;
        float leftMaxWidth = 0;
        float rightWidth = 0;
        float rightMaxWidth = 0;
        Maligngroup group = null;
        Maligngroup nextGroup = null;
        for (int col = 0; col < columnsCount; col++) {
            for (int row = 0; row < rowsCount; row++) {
                Mtd cell = null;
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
                        groupalignvalues = new Vector<Mtable.AlignmentType>(
                                aligngroups.length);
                        for (final Maligngroup element : aligngroups) {
                            groupalignvalues.add(Mtable.AlignmentType.LEFT);
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
                        currentWidth = alignwidths[col][Mtable.WHOLE_WIDTH][row][alignIndex];
                        maxWidth = alignwidths[col][Mtable.WHOLE_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex];
                        leftWidth = alignwidths[col][Mtable.LEFT_WIDTH][row][alignIndex];
                        leftMaxWidth = alignwidths[col][Mtable.LEFT_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex];
                        rightWidth = alignwidths[col][Mtable.RIGHT_WIDTH][row][alignIndex];
                        rightMaxWidth = alignwidths[col][Mtable.RIGHT_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex];
                        group = aligngroups[alignIndex];
                        if (usesMarks[col][alignIndex]) {
                            alignOfTheGroup = Mtable.AlignmentType.MARK;
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

        JEuclidElement element = null;
        for (; elements.hasNext();) {
            element = (JEuclidElement) elements.next();
            if (element instanceof Mn) {
                return result + ((Mn) element).getWidthTillPoint(g);
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
    private boolean containsNumber(final JEuclidElement element) {
        return this.containsElement(element, Mn.ELEMENT);
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
            final JEuclidElement element = (JEuclidElement) iterator.next();
            if (element instanceof Mn) {
                result = ((Mn) element).getPointWidth(g);
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

        JEuclidElement element = null;
        for (; elements.hasNext();) {
            element = (JEuclidElement) elements.next();
            if (element instanceof Malignmark) {
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
    private boolean containsMark(final JEuclidElement element) {
        return this.containsElement(element, Malignmark.ELEMENT);
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
    private boolean containsElement(final JEuclidElement container,
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
        return Mtable.ELEMENT;
    }

    /** {@inheritDoc} */
    public String getRowlines() {
        return this.getMathAttribute(Mtable.ATTR_ROWLINES);
    }

    /** {@inheritDoc} */
    public void setRowlines(final String rowlines) {
        this.setAttribute(Mtable.ATTR_ROWLINES, rowlines);
    }

    /** {@inheritDoc} */
    public String getColumnlines() {
        return this.getMathAttribute(Mtable.ATTR_COLUMNLINES);
    }

    /** {@inheritDoc} */
    public void setColumnlines(final String columnlines) {
        this.setAttribute(Mtable.ATTR_COLUMNLINES, columnlines);
    }

    private LineType getRowLine(final int row) {
        return Mtable.LineType.parseLineType(this.getSpaceArrayEntry(this
                .getRowlines(), row));
    }

    private LineType getColumnLine(final int col) {
        return Mtable.LineType.parseLineType(this.getSpaceArrayEntry(this
                .getColumnlines(), col));
    }

    private LineType getFrameAsLineType() {
        return Mtable.LineType.parseLineType(this.getFrame());
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
        return this.getMathAttribute(Mtable.ATTR_COLUMNWIDTH);
    }

    /** {@inheritDoc} */
    public void setColumnwidth(final String columnwidth) {
        this.setAttribute(Mtable.ATTR_COLUMNWIDTH, columnwidth);
    }

    /** {@inheritDoc} */
    public String getWidth() {
        return this.getMathAttribute(Mtable.ATTR_WIDTH);
    }

    /** {@inheritDoc} */
    public void setWidth(final String width) {
        this.setAttribute(Mtable.ATTR_WIDTH, width);
    }

    /** {@inheritDoc} */
    public String getAlign() {
        return this.getMathAttribute(Mtable.ATTR_ALIGN);
    }

    /** {@inheritDoc} */
    public void setAlign(final String align) {
        this.setAttribute(Mtable.ATTR_ALIGN, align);
    }

    /** {@inheritDoc} */
    public String getRowalign() {
        return this.getMathAttribute(Mtable.ATTR_ROWALIGN);
    }

    /** {@inheritDoc} */
    public void setRowalign(final String rowalign) {
        this.setAttribute(Mtable.ATTR_ROWALIGN, rowalign);
    }

    /** {@inheritDoc} */
    public String getColumnalign() {
        return this.getMathAttribute(Mtable.ATTR_COLUMNALIGN);
    }

    /** {@inheritDoc} */
    public void setColumnalign(final String columnalign) {
        this.setAttribute(Mtable.ATTR_COLUMNALIGN, columnalign);

    }

    /** {@inheritDoc} */
    public String getGroupalign() {
        return this.getMathAttribute(Mtable.ATTR_GROUPALIGN);
    }

    /** {@inheritDoc} */
    public void setGroupalign(final String groupalign) {
        this.setAttribute(Mtable.ATTR_GROUPALIGN, groupalign);
    }

    /** {@inheritDoc} */
    public String getAlignmentscope() {
        return this.getMathAttribute(Mtable.ATTR_ALIGNMENTSCOPE);
    }

    /** {@inheritDoc} */
    public void setAlignmentscope(final String alignmentscope) {
        this.setAttribute(Mtable.ATTR_ALIGNMENTSCOPE, alignmentscope);
    }

    /** {@inheritDoc} */
    public String getRowspacing() {
        return this.getMathAttribute(Mtable.ATTR_ROWSPACING);
    }

    private float getRowspacing(final int row) {
        return AttributesHelper.convertSizeToPt(this.getSpaceArrayEntry(this
                .getRowspacing(), row), this.getCurrentLayoutContext(),
                AttributesHelper.PT);
    }

    /** {@inheritDoc} */
    public void setRowspacing(final String rowspacing) {
        this.setAttribute(Mtable.ATTR_ROWSPACING, rowspacing);
    }

    /** {@inheritDoc} */
    public String getColumnspacing() {
        return this.getMathAttribute(Mtable.ATTR_COLUMNSPACING);
    }

    private float getColumnspacing(final int column) {
        return AttributesHelper.convertSizeToPt(this.getSpaceArrayEntry(this
                .getColumnspacing(), column), this.getCurrentLayoutContext(),
                AttributesHelper.PT);
    }

    /** {@inheritDoc} */
    public void setColumnspacing(final String columnspacing) {
        this.setAttribute(Mtable.ATTR_COLUMNSPACING, columnspacing);
    }

    /** {@inheritDoc} */
    public String getFrame() {
        return this.getMathAttribute(Mtable.ATTR_FRAME);
    }

    /** {@inheritDoc} */
    public void setFrame(final String frame) {
        this.setAttribute(Mtable.ATTR_FRAME, frame);
    }

    /** {@inheritDoc} */
    public String getFramespacing() {
        return this.getMathAttribute(Mtable.ATTR_FRAMESPACING);
    }

    /** {@inheritDoc} */
    public void setFramespacing(final String framespacing) {
        this.setAttribute(Mtable.ATTR_FRAMESPACING, framespacing);
    }

    /** {@inheritDoc} */
    public String getEqualrows() {
        return this.getMathAttribute(Mtable.ATTR_EQUALROWS);
    }

    /** {@inheritDoc} */
    public void setEqualrows(final String equalrows) {
        this.setAttribute(Mtable.ATTR_EQUALROWS, equalrows);
    }

    /** {@inheritDoc} */
    public String getEqualcolumns() {
        return this.getMathAttribute(Mtable.ATTR_EQUALCOLUMNS);
    }

    /** {@inheritDoc} */
    public void setEqualcolumns(final String equalcolumns) {
        this.setAttribute(Mtable.ATTR_EQUALCOLUMNS, equalcolumns);
    }

    /** {@inheritDoc} */
    public String getDisplaystyle() {
        return this.getMathAttribute(Mtable.ATTR_DISPLAYSTYLE);
    }

    /** {@inheritDoc} */
    public void setDisplaystyle(final String displaystyle) {
        this.setAttribute(Mtable.ATTR_DISPLAYSTYLE, displaystyle);
    }

    /** {@inheritDoc} */
    public String getSide() {
        return this.getMathAttribute(Mtable.ATTR_SIDE);
    }

    /** {@inheritDoc} */
    public void setSide(final String side) {
        this.setAttribute(Mtable.ATTR_SIDE, side);
    }

    /** {@inheritDoc} */
    public String getMinlabelspacing() {
        return this.getMathAttribute(Mtable.ATTR_MINLABELSPACING);
    }

    /** {@inheritDoc} */
    public void setMinlabelspacing(final String minlabelspacing) {
        this.setAttribute(Mtable.ATTR_MINLABELSPACING, minlabelspacing);
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

    /** {@inheritDoc} */
    public void deleteRow(final long index) {
        // TODO Auto-generated method stub

    }

    /** {@inheritDoc} */
    public MathMLTableRowElement getRow(final long index) {
        // TODO Auto-generated method stub
        return null;
    }

    /** {@inheritDoc} */
    public MathMLLabeledRowElement insertEmptyLabeledRow(final long index) {
        // TODO Auto-generated method stub
        return null;
    }

    /** {@inheritDoc} */
    public MathMLTableRowElement insertEmptyRow(final long index) {
        // TODO Auto-generated method stub
        return null;
    }

    /** {@inheritDoc} */
    public MathMLTableRowElement insertRow(final long index,
            final MathMLTableRowElement newRow) {
        // TODO Auto-generated method stub
        return null;
    }

    /** {@inheritDoc} */
    public MathMLTableRowElement removeRow(final long index) {
        // TODO Auto-generated method stub
        return null;
    }

    /** {@inheritDoc} */
    public MathMLTableRowElement setRow(final long index,
            final MathMLTableRowElement newRow) {
        // TODO Auto-generated method stub
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public void layoutStageInvariant(final LayoutView view,
            final LayoutInfo info, final LayoutStage stage) {
        final Graphics2D g = view.getGraphics();
        final List<Node> children = this.getChildren();
        final LayoutInfo[] rowInfos = new LayoutInfo[children.size()];
        final Mtr[] rowChild = new Mtr[children.size()];
        float y = 0;
        int rows = 0;
        for (final Node child : children) {
            final Mtr mtr = (Mtr) child;
            rowChild[rows] = mtr;
            final LayoutInfo mtrInfo = view.getInfo(mtr);
            y += mtrInfo.getAscentHeight(stage);
            rowInfos[rows] = mtrInfo;
            rows++;
            mtrInfo.moveTo(0, y, stage);
            y += mtrInfo.getDescentHeight(stage);
        }
        final float height = y;
        // final String alignStr = this.getAlign();
        // AlignmentType align =
        // Mtable.AlignmentType.parseAlignmentType(alignStr);
        final float verticalShift = -this.getMiddleShift(g) - height / 2.0f;
        for (int i = 0; i < rows; i++) {
            rowInfos[i].moveTo(0, rowInfos[i].getPosY(stage) + verticalShift,
                    stage);
        }
        // TODO: Proper vertical alignment;

        final List<Float> columnwidth = new Vector<Float>();
        for (int i = 0; i < rows; i++) {
            int col = 0;
            final List<Node> mtdChildren = rowChild[i].getChildren();
            int missing = mtdChildren.size() - columnwidth.size();
            while (missing > 0) {
                columnwidth.add(0.0f);
                missing--;
            }
            for (final Node n : mtdChildren) {
                final Mtd mtd = (Mtd) n;
                final float width = Math.max(columnwidth.get(col), view
                        .getInfo(mtd).getWidth(stage));
                columnwidth.set(col, width);
                col++;
            }
        }

        for (int i = 0; i < rows; i++) {
            final List<Node> mtdChildren = rowChild[i].getChildren();
            float x = 0.0f;
            int col = 0;
            for (final Node n : mtdChildren) {
                final Mtd mtd = (Mtd) n;
                final LayoutInfo mtdInfo = view.getInfo(mtd);
                mtdInfo.moveTo(x, 0.0f, stage);
                x += columnwidth.get(col);
                col++;
            }
        }

        float totalWidth = 0.0f;
        for (final Float f : columnwidth) {
            totalWidth += f;
        }
        for (int i = 0; i < rows; i++) {
            rowInfos[i].setWidth(totalWidth, stage);
            // TODO: Proper horizontal alignment;
        }

        // TODO: Make more sophisticated.
        final Dimension2D borderLeftTop = new Dimension2DImpl(0.0f, 0.0f);
        final Dimension2D borderRightBottom = new Dimension2DImpl(0.0f, 0.0f);
        ElementListSupport.fillInfoFromChildren(view, info, this, stage,
                borderLeftTop, borderRightBottom);
    }
}
