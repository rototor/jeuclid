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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import net.sourceforge.jeuclid.Constants;
import net.sourceforge.jeuclid.LayoutContext;
import net.sourceforge.jeuclid.LayoutContext.Parameter;
import net.sourceforge.jeuclid.context.InlineLayoutContext;
import net.sourceforge.jeuclid.elements.AbstractJEuclidElement;
import net.sourceforge.jeuclid.elements.JEuclidElement;
import net.sourceforge.jeuclid.elements.support.Dimension2DImpl;
import net.sourceforge.jeuclid.elements.support.ElementListSupport;
import net.sourceforge.jeuclid.elements.support.GraphicsSupport;
import net.sourceforge.jeuclid.elements.support.attributes.AttributesHelper;
import net.sourceforge.jeuclid.elements.support.attributes.HAlign;
import net.sourceforge.jeuclid.layout.GraphicsObject;
import net.sourceforge.jeuclid.layout.LayoutInfo;
import net.sourceforge.jeuclid.layout.LayoutStage;
import net.sourceforge.jeuclid.layout.LayoutView;
import net.sourceforge.jeuclid.layout.LayoutableNode;
import net.sourceforge.jeuclid.layout.LineObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.mathml.MathMLLabeledRowElement;
import org.w3c.dom.mathml.MathMLNodeList;
import org.w3c.dom.mathml.MathMLTableCellElement;
import org.w3c.dom.mathml.MathMLTableElement;
import org.w3c.dom.mathml.MathMLTableRowElement;

/**
 * This class presents a table.
 * 
 * @version $Revision$
 */
// CHECKSTYLE:OFF
// Data abstraction coupling is "to high". but this is necessary for proper
// layout.
public class Mtable extends AbstractJEuclidElement implements
        MathMLTableElement {
    // CHECKSTYLE:ON

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "mtable";

    /** Attribute for columnalign. */
    static final String ATTR_COLUMNALIGN = "columnalign";

    /** Attribute for rowalign. */
    static final String ATTR_ROWALIGN = "rowalign";

    /** Attribute for groupalign. */
    static final String ATTR_GROUPALIGN = "groupalign";

    /** attribute for rowlines. */
    private static final String ATTR_ROWLINES = "rowlines";

    /** attribute for columnlines. */
    private static final String ATTR_COLUMNLINES = "columnlines";

    /** attribute for align. */
    private static final String ATTR_ALIGN = "align";

    /** attribute for alignmentscope. */
    private static final String ATTR_ALIGNMENTSCOPE = "alignmentscope";

    /** attribute for columnwidth. */
    private static final String ATTR_COLUMNWIDTH = "columnwidth";

    /** attribute for width. */
    private static final String ATTR_WIDTH = "width";

    /** attribute for rowspacing. */
    private static final String ATTR_ROWSPACING = "rowspacing";

    /** attribute for columnspacing. */
    private static final String ATTR_COLUMNSPACING = "columnspacing";

    /** attribute for frame. */
    private static final String ATTR_FRAME = "frame";

    /** attribute for framespacing. */
    private static final String ATTR_FRAMESPACING = "framespacing";

    /** attribute for equalrows. */
    private static final String ATTR_EQUALROWS = "equalrows";

    /** attribute for equalcolumns. */
    private static final String ATTR_EQUALCOLUMNS = "equalcolumns";

    /** attribute for displaystyle. */
    private static final String ATTR_DISPLAYSTYLE = "displaystyle";

    /** attribute for side. */
    private static final String ATTR_SIDE = "side";

    /** attribute for minlabelspacing. */
    private static final String ATTR_MINLABELSPACING = "minlabelspacing";

    /** value for no lines. */
    private static final String VALUE_NONE = "none";

    /** value for dashed lines. */
    private static final String VALUE_DASHED = "dashed";

    /** value for solid lines. */
    private static final String VALUE_SOLID = "solid";

    /**
     * Default column spacing.
     */
    private static final String DEFAULT_COLUMNSPACING = "0.8em";

    /**
     * Default row spacing.
     */
    private static final String DEFAULT_ROWSPACING = "1.0ex";

    /**
     * Default frame spacing.
     */
    private static final String DEFAULT_FRAMESPACING = "0.4em 0.5ex";

    private static final String VALUE_AUTO = "auto";

    /**
     * Logger for this class
     */
    private static final Log LOGGER = LogFactory.getLog(Mtable.class);

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
            if (s.equalsIgnoreCase(Mtable.VALUE_SOLID)) {
                retVal = SOLID;
            } else if (s.equalsIgnoreCase(Mtable.VALUE_DASHED)) {
                retVal = DASHED;
            } else {
                retVal = NONE;
            }
            return retVal;
        }
    };

    /**
     * Class for alignment types.
     */
    private static final class VAlign {

        /** Align to top. */
        static final int TOP = 0;

        /** Align to bottom. */
        static final int BOTTOM = 1;

        /** Align to center. */
        static final int CENTER = 2;

        /** Align to baseline. */
        static final int BASELINE = 3;

        /** Align to axis. */
        static final int AXIS = 4;

        static final String VALUE_TOP = "top";

        static final String VALUE_BOTTOM = "bottom";

        static final String VALUE_CENTER = "center";

        static final String VALUE_BASELINE = "baseline";

        static final String VALUE_AXIS = "axis";

        static final VAlign BASELINE_ALIGN = new VAlign(
                Mtable.VAlign.BASELINE, 0);

        private static final String INVALID_VERTICAL_ALIGNMENT_VALUE = "Invalid vertical alignment value: ";

        private final int valign;

        private final int alignTo;

        private VAlign(final int align, final int relativeTo) {
            this.valign = align;
            this.alignTo = relativeTo;
        }

        /**
         * Parse a string and return a alignment.
         * 
         * @param s
         *            the string to parse
         * @return an alignment for this string type
         */
        public static VAlign parseString(final String s) {
            if ((s == null) || (s.length() == 0)) {
                return null;
            }
            final int align;
            int relativeTo = 0;
            final String s2 = s.trim().toLowerCase(Locale.ENGLISH);
            final String s3;
            if (s2.startsWith(Mtable.VAlign.VALUE_TOP)) {
                align = Mtable.VAlign.TOP;
                s3 = s2.substring(Mtable.VAlign.VALUE_TOP.length()).trim();
            } else if (s2.startsWith(Mtable.VAlign.VALUE_BOTTOM)) {
                align = Mtable.VAlign.BOTTOM;
                s3 = s2.substring(Mtable.VAlign.VALUE_BOTTOM.length()).trim();
            } else if (s2.startsWith(Mtable.VAlign.VALUE_CENTER)) {
                align = Mtable.VAlign.CENTER;
                s3 = s2.substring(Mtable.VAlign.VALUE_CENTER.length()).trim();
            } else if (s2.startsWith(Mtable.VAlign.VALUE_BASELINE)) {
                align = Mtable.VAlign.BASELINE;
                s3 = s2.substring(Mtable.VAlign.VALUE_BASELINE.length())
                        .trim();
            } else if (s2.startsWith(Mtable.VAlign.VALUE_AXIS)) {
                align = Mtable.VAlign.AXIS;
                s3 = s2.substring(Mtable.VAlign.VALUE_AXIS.length()).trim();
            } else {
                Mtable.LOGGER
                        .warn(Mtable.VAlign.INVALID_VERTICAL_ALIGNMENT_VALUE
                                + s);
                align = Mtable.VAlign.BASELINE;
                s3 = "0";
            }
            if (s3.length() > 0) {
                try {
                    relativeTo = Integer.parseInt(s3);
                } catch (final NumberFormatException nfe) {
                    Mtable.LOGGER
                            .warn(Mtable.VAlign.INVALID_VERTICAL_ALIGNMENT_VALUE
                                    + s);
                }
            }
            return new VAlign(align, relativeTo);
        }

        public int getAlign() {
            return this.valign;
        }

        public int getRelative() {
            return this.alignTo;
        }
    }

    /**
     * Creates a math element.
     */
    public Mtable() {
        super();
        this.setDefaultMathAttribute(Mtable.ATTR_ALIGN,
                Mtable.VAlign.VALUE_AXIS);
        this.setDefaultMathAttribute(Mtable.ATTR_ROWALIGN,
                Mtable.VAlign.VALUE_BASELINE);
        this.setDefaultMathAttribute(Mtable.ATTR_COLUMNALIGN,
                HAlign.ALIGN_CENTER);
        this.setDefaultMathAttribute(Mtable.ATTR_GROUPALIGN, "{left}");
        this.setDefaultMathAttribute(Mtable.ATTR_ALIGNMENTSCOPE,
                Constants.TRUE);
        this.setDefaultMathAttribute(Mtable.ATTR_COLUMNWIDTH,
                Mtable.VALUE_AUTO);
        this.setDefaultMathAttribute(Mtable.ATTR_WIDTH, Mtable.VALUE_AUTO);
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
        this.setDefaultMathAttribute(Mtable.ATTR_EQUALROWS, Constants.FALSE);
        this.setDefaultMathAttribute(Mtable.ATTR_EQUALCOLUMNS,
                Constants.FALSE);
        this.setDefaultMathAttribute(Mtable.ATTR_DISPLAYSTYLE,
                Constants.FALSE);
        this.setDefaultMathAttribute(Mtable.ATTR_SIDE, HAlign.ALIGN_RIGHT);
        this.setDefaultMathAttribute(Mtable.ATTR_MINLABELSPACING,
                Mtable.DEFAULT_COLUMNSPACING);
    }

    /** {@inheritDoc} */
    @Override
    public LayoutContext getChildLayoutContext(final int childNum,
            final LayoutContext context) {
        return new InlineLayoutContext(this
                .applyLocalAttributesToContext(context));
    }

    private float getFramespacingh(final LayoutContext now) {
        if (Mtable.LineType.NONE.equals(this.getFrameAsLineType())) {
            return 0;
        }
        final String spacing = this.getSpaceArrayEntry(
                this.getFramespacing(), 0);
        return AttributesHelper.convertSizeToPt(spacing, now,
                AttributesHelper.PT);
    }

    private float getFramespacingv(final LayoutContext now) {
        if (Mtable.LineType.NONE.equals(this.getFrameAsLineType())) {
            return 0;
        }
        final String spacing = this.getSpaceArrayEntry(
                this.getFramespacing(), 1);
        return AttributesHelper.convertSizeToPt(spacing, now,
                AttributesHelper.PT);
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
    // public void paint(final Graphics2D g, float posX, float posY) {
    // super.paint(g, posX, posY);
    // posX = posX + this.getFramespacingh();
    // posY = posY + this.getFramespacingv();
    //
    // int i;
    // int j;
    // final float[] maxrowascentheight = new float[this
    // .getMathElementCount()];
    // final float[] maxrowdescentheight = new float[this
    // .getMathElementCount()];
    //
    // for (i = 0; i < this.getMathElementCount(); i++) {
    // maxrowascentheight[i] = this.getMaxRowAscentHeight(g, i);
    // maxrowdescentheight[i] = this.getMaxRowDescentHeight(g, i);
    // }
    //
    // final int maxcolumns = this.getMaxColumnCount();
    // final boolean isAlignGroupsExist = this.getMaxGroupAlignCount() == 0 ?
    // false
    // : true;
    // final float[] maxcolumnwidth = new float[maxcolumns];
    //
    // for (i = 0; i < maxcolumns; i++) {
    // maxcolumnwidth[i] = this.getMaxColumnWidth(g, i);
    // }
    //
    // final float x1 = posX;
    // float x = x1;
    //
    // posY = posY - this.getAscentHeight(g);
    // final float startY = posY;
    //
    // final List<Float> rowlines = new Vector<Float>();
    // final List<Float> columnlines = new Vector<Float>(maxcolumns);
    //
    // for (i = 0; i < this.getMathElementCount(); i++) {
    // final JEuclidElement row = this.getMathElement(i);
    // posY += maxrowascentheight[i];
    //
    // x = x1;
    // for (j = 0; (j < maxcolumns) && (j < row.getMathElementCount()); j++) {
    // if (isAlignGroupsExist
    // && this.getAlignGroups((Mtd) row.getMathElement(j)).length > 0) {
    // // left alignment
    // row.getMathElement(j).paint(g, x, posY);
    // } else {
    // // PG this code makes table to draw content of the cell in
    // // the middle of the cell
    // final float xx = x + maxcolumnwidth[j] / 2
    // - row.getMathElement(j).getWidth(g) / 2;
    // row.getMathElement(j).paint(g, xx, posY);
    // }
    // final float currentColSpacing = this.getColumnspacing(j);
    // x += maxcolumnwidth[j];
    // if ((i == 0) && (j < maxcolumns - 1)) {
    // // TODO: This only sets columnlines if the first row
    // // covers all the columns. Maybe this needs to be changed?
    // columnlines.add(x + currentColSpacing / 2.0f);
    // }
    // x += currentColSpacing;
    // }
    //
    // posY += maxrowdescentheight[i];
    // final float currentRowSpacing = this.getRowspacing(i);
    // if (i < (this.getMathElementCount() - 1)) {
    // rowlines.add(posY + currentRowSpacing / 2.0f);
    // }
    // posY += currentRowSpacing;
    // }
    // int col = 0;
    // final Stroke oldStroke = g.getStroke();
    // final float lineWidth = GraphicsSupport.lineWidth(this);
    // final float dashWidth = 3.0f * lineWidth;
    // final Stroke solidStroke = new BasicStroke(lineWidth);
    // final Stroke dashedStroke = new BasicStroke(lineWidth,
    // BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL, lineWidth,
    // new float[] { dashWidth, dashWidth }, 0);
    // for (final float lineX : columnlines) {
    // final LineType lt = this.getColumnLine(col);
    // col++;
    // if (Mtable.LineType.SOLID.equals(lt)) {
    // g.setStroke(solidStroke);
    // } else if (Mtable.LineType.DASHED.equals(lt)) {
    // g.setStroke(dashedStroke);
    // }
    // if (!Mtable.LineType.NONE.equals(lt)) {
    // g.draw(new Line2D.Float(lineX, startY, lineX, posY));
    // }
    // }
    // int row = 0;
    // for (final float lineY : rowlines) {
    // // TODO: This only works if the last entry has all column. Maybe
    // // this needs to be changed?
    // final LineType lt = this.getRowLine(row);
    // row++;
    // // TODO: This code is very similar to the one for columnlines.
    // // Refactor!
    // if (Mtable.LineType.SOLID.equals(lt)) {
    // g.setStroke(solidStroke);
    // } else if (Mtable.LineType.DASHED.equals(lt)) {
    // g.setStroke(dashedStroke);
    // }
    // if (!Mtable.LineType.NONE.equals(lt)) {
    // g.draw(new Line2D.Float(x1, lineY, x, lineY));
    // }
    // }
    // g.setStroke(oldStroke);
    // }

    //
    // /**
    // * Retrieves groupalign values from mtd element. If requested cell
    // doesn't
    // * contain groupalign attribute, the current row (and after it current
    // * table) will be requested.
    // *
    // * @param cell
    // * Cell to get groupalign values.
    // * @return Array with groupalign values.
    // */
    // public List<Mtable.AlignmentType> getGroupAlign(final Mtd cell) {
    // final List<Mtable.AlignmentType> result = new
    // Vector<Mtable.AlignmentType>();
    //
    // String groupAlign;
    //
    // if (cell == null) {
    // return result;
    // }
    // final String cellGroupAlign = cell.getGroupalign();
    // if (cellGroupAlign == null || cellGroupAlign.length() == 0) {
    // groupAlign = ((Mtr) cell.getParent()).getGroupalign();
    // } else {
    // groupAlign = cell.getGroupalign();
    // }
    //
    // if (groupAlign == null) {
    // groupAlign = this.getGroupalign();
    // }
    //
    // if (groupAlign != null) {
    //
    // final String[] gAlign = groupAlign.split("\\w");
    // for (final String value : gAlign) {
    // if (value.length() > 0) {
    // result
    // .add(Mtable.AlignmentType
    // .parseAlignmentType(value));
    // }
    // }
    // }
    //
    // return result;
    // }
    //
    // /**
    // * Get max count of align groups in a cell.
    // *
    // * @return Max number of groups.
    // */
    // private int getMaxGroupAlignCount() {
    // int result = 0;
    // final int rowsCount = this.getRowCount();
    // final int columnsCount = this.getMaxColumnCount();
    // int tmpLength = 0;
    //
    // for (int column = 0; column < columnsCount; column++) {
    // for (int row = 0; row < rowsCount; row++) {
    // Mtd cell = null;
    // try {
    // cell = this.getCell(row, column);
    // } catch (final Exception e) {
    // return 0;
    // }
    //
    // if (cell != null) {
    // tmpLength = cell.getAlignGroups().size();
    // if (result < tmpLength) {
    // result = tmpLength;
    // }
    // }
    // }
    // }
    //
    // return result;
    // }
    //
    // /**
    // * Determines all maligngroup elements in the cell.
    // *
    // * @param cell
    // * Cell object.
    // * @return Array of maligngroup elements.
    // */
    // private Maligngroup[] getAlignGroups(final Mtd cell) {
    // Maligngroup[] result;
    //
    // final List<Maligngroup> list = cell.getAlignGroups();
    //
    // if (list.size() == 0) {
    // result = new Maligngroup[0];
    // } else {
    // // copy to result array
    // result = new Maligngroup[list.size()];
    // for (int i = 0; i < list.size(); i++) {
    // result[i] = (list.get(i));
    // }
    // }
    //
    // return result;
    // }

    /**
     * // * Method calculates widths of alignment elements. Must be called
     * after // * the calculating of all MathElements widths. // * // *
     * 
     * @param g // *
     *            Graphics2D context to use. //
     */
    // protected void calculateAlignmentGroups(final Graphics2D g) {
    // final int rowsCount = this.getRowCount();
    // final int columnsCount = this.getMaxColumnCount();
    // final int MAX_WIDTH_IN_COLUMN = rowsCount;
    // final int maxGroupAlignCount = this.getMaxGroupAlignCount();
    // if (maxGroupAlignCount == 0) {
    // return;
    // }
    // // structure to hold calculated temp values, for each element from
    // // align
    // // group
    // // [0] whole width value
    // // [1] left width value (till malignmark or decimal point)
    // // [2] right width value
    // // rowsCount+1 - max width of element (or its part)
    // final float[][][][] alignwidths = new float[columnsCount][3][rowsCount
    // + 1][maxGroupAlignCount];
    // // need to know, either this column of aligngroup uses malignmarks
    // final boolean[][] usesMarks = new boolean[columnsCount][this
    // .getMaxGroupAlignCount()];
    // // array of aligngropus of the cell
    // Maligngroup[] aligngroups; // align values of aligngroups in the
    // // cell
    // List<Mtable.AlignmentType> groupalignvalues; // elements of the
    // // aligngroup
    // List<JEuclidElement> elements;
    // for (int col = 0; col < columnsCount; col++) { // walking through the
    // // column "column"
    // for (int row = 0; row < rowsCount; row++) {
    // Mtd cell = null;
    // try {
    // cell = this.getCell(row, col);
    // } catch (final Exception e) {
    // cell = null;
    // }
    // if (cell != null) { // all align components of the cell
    // aligngroups = this.getAlignGroups(cell);
    // groupalignvalues = this.getGroupAlign(cell);
    // if (groupalignvalues.size() == 0) {
    // // values of alignment didn't mentioned, have to use
    // // default
    // groupalignvalues = new Vector<Mtable.AlignmentType>(
    // aligngroups.length);
    // for (final Maligngroup element : aligngroups) {
    // groupalignvalues.add(Mtable.AlignmentType.LEFT);
    // }
    // }
    // if (aligngroups.length == 0
    // || aligngroups.length < groupalignvalues.size()) {
    // continue; // there is no aligngroups in the cell or
    // // wrong count
    // }
    // for (int alignIndex = 0; alignIndex < groupalignvalues
    // .size(); alignIndex++) {
    // // widths of align group
    // elements = Maligngroup
    // .getElementsOfAlignGroup(aligngroups[alignIndex]);
    // alignwidths[col][Mtable.WHOLE_WIDTH][row][alignIndex] = Maligngroup
    // .getElementsWholeWidth(g, elements);
    // // max whole width of group
    // if
    // (alignwidths[col][Mtable.WHOLE_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex]
    // < alignwidths[col][Mtable.WHOLE_WIDTH][row][alignIndex]) {
    // alignwidths[col][Mtable.WHOLE_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex] =
    // alignwidths[col][Mtable.WHOLE_WIDTH][row][alignIndex];
    // }
    // // there is alignmark in the group
    // if (aligngroups[alignIndex].getMark() != null) {
    // usesMarks[col][alignIndex] = true;
    // final float leftPart = this.getWidthTillMark(g,
    // elements.iterator());
    // // left width = width till alignmark
    // alignwidths[col][Mtable.LEFT_WIDTH][row][alignIndex] = leftPart;
    // // right width = whole width - left width
    // alignwidths[col][Mtable.RIGHT_WIDTH][row][alignIndex] =
    // alignwidths[col][Mtable.WHOLE_WIDTH][row][alignIndex]
    // - leftPart;
    // // max left width
    // if
    // (alignwidths[col][Mtable.LEFT_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex] <
    // leftPart) {
    // alignwidths[col][Mtable.LEFT_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex] =
    // leftPart;
    // }
    // // max right width
    // if
    // (alignwidths[col][Mtable.RIGHT_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex]
    // < alignwidths[col][Mtable.RIGHT_WIDTH][row][alignIndex]) {
    // alignwidths[col][Mtable.RIGHT_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex] =
    // alignwidths[col][Mtable.RIGHT_WIDTH][row][alignIndex];
    // }
    // } else {
    // // there is no alignmark, right width is equal to
    // // the whole width
    // alignwidths[col][Mtable.LEFT_WIDTH][row][alignIndex] = 0;
    // alignwidths[col][Mtable.RIGHT_WIDTH][row][alignIndex] =
    // alignwidths[col][Mtable.WHOLE_WIDTH][row][alignIndex];
    // // but! may be, we have alignment decimalpoint...
    // if (groupalignvalues.get(alignIndex).equals(
    // Mtable.AlignmentType.DECIMALPOINT)) {
    // // width of the left (till decimal point)
    // // value
    // // of MathNumber
    // final float tillPoint = this
    // .getWidthTillPoint(g, elements
    // .iterator());
    // final float pointWidth = this.getPointWidth(
    // g, elements.iterator());
    // // left width
    // alignwidths[col][Mtable.LEFT_WIDTH][row][alignIndex] = tillPoint;
    // // determine max left width till decimal point
    // if
    // (alignwidths[col][Mtable.LEFT_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex] <
    // tillPoint) {
    // alignwidths[col][Mtable.LEFT_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex] =
    // tillPoint;
    // }
    // // calculate right part
    // alignwidths[col][Mtable.RIGHT_WIDTH][row][alignIndex] =
    // alignwidths[col][Mtable.WHOLE_WIDTH][row][alignIndex]
    // - tillPoint - pointWidth;
    // // calculate right max
    // if
    // (alignwidths[col][Mtable.RIGHT_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex]
    // < alignwidths[col][Mtable.RIGHT_WIDTH][row][alignIndex]) {
    // alignwidths[col][Mtable.RIGHT_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex] =
    // alignwidths[col][Mtable.RIGHT_WIDTH][row][alignIndex];
    // }
    // }
    // } // no align mark in this align group
    // } // cycle: align groups in the cell
    // } // if cell != null
    // } // cycle: rows
    // } // cycle: columns
    //
    // // calculating shifts of MathAlignGroup elements
    // Mtable.AlignmentType alignOfTheGroup = Mtable.AlignmentType.LEFT;
    // float currentWidth = 0;
    // float maxWidth = 0;
    // float leftWidth = 0;
    // float leftMaxWidth = 0;
    // float rightWidth = 0;
    // float rightMaxWidth = 0;
    // Maligngroup group = null;
    // Maligngroup nextGroup = null;
    // for (int col = 0; col < columnsCount; col++) {
    // for (int row = 0; row < rowsCount; row++) {
    // Mtd cell = null;
    // try {
    // cell = this.getCell(row, col);
    // } catch (final Exception e) {
    // cell = null;
    // }
    //
    // if (cell != null) {
    // aligngroups = this.getAlignGroups(cell);
    // groupalignvalues = this.getGroupAlign(cell);
    // if (groupalignvalues.size() == 0) {
    // // values of alignment didn't mentioned, have to use
    // // default
    // groupalignvalues = new Vector<Mtable.AlignmentType>(
    // aligngroups.length);
    // for (final Maligngroup element : aligngroups) {
    // groupalignvalues.add(Mtable.AlignmentType.LEFT);
    // }
    // }
    // if (aligngroups.length == 0
    // || aligngroups.length < groupalignvalues.size()) {
    // continue;
    // }
    // for (int alignIndex = 0; alignIndex < groupalignvalues
    // .size(); alignIndex++) {
    // // retrieving previously calculated information about
    // // aligngroups widths
    // alignOfTheGroup = groupalignvalues.get(alignIndex);
    // currentWidth = alignwidths[col][Mtable.WHOLE_WIDTH][row][alignIndex];
    // maxWidth =
    // alignwidths[col][Mtable.WHOLE_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex];
    // leftWidth = alignwidths[col][Mtable.LEFT_WIDTH][row][alignIndex];
    // leftMaxWidth =
    // alignwidths[col][Mtable.LEFT_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex];
    // rightWidth = alignwidths[col][Mtable.RIGHT_WIDTH][row][alignIndex];
    // rightMaxWidth =
    // alignwidths[col][Mtable.RIGHT_WIDTH][MAX_WIDTH_IN_COLUMN][alignIndex];
    // group = aligngroups[alignIndex];
    // if (usesMarks[col][alignIndex]) {
    // alignOfTheGroup = Mtable.AlignmentType.MARK;
    // }
    // if (alignIndex < groupalignvalues.size() - 1) {
    // nextGroup = aligngroups[alignIndex + 1];
    // }
    // switch (alignOfTheGroup) {
    // case RIGHT:
    // group.width += maxWidth - currentWidth;
    // break;
    // case LEFT:
    // if (alignIndex < groupalignvalues.size() - 1) {
    // nextGroup.width += maxWidth - currentWidth;
    // }
    // break;
    // case CENTER:
    // group.width += (maxWidth - currentWidth) / 2;
    // if (alignIndex < groupalignvalues.size() - 1) {
    // nextGroup.width += (maxWidth - currentWidth) / 2;
    // }
    // break;
    // case DECIMALPOINT:
    // case MARK:
    // group.width += (leftMaxWidth - leftWidth);
    // if (alignIndex < groupalignvalues.size() - 1) {
    // nextGroup.width += rightMaxWidth - rightWidth;
    // }
    // break;
    // default:
    // group.width += maxWidth - currentWidth;
    // }
    // }
    // }
    // }
    // }
    // }
    // /**
    // * Method calculates width of elements till point inside of mn element
    // (or
    // * first of mn-element).
    // *
    // * @param elements
    // * List of elements.
    // * @return Width of elements till point.
    // */
    // private float getWidthTillPoint(final Graphics2D g,
    // final Iterator elements) {
    // float result = 0;
    //
    // JEuclidElement element = null;
    // for (; elements.hasNext();) {
    // element = (JEuclidElement) elements.next();
    // if (element instanceof Mn) {
    // return result + ((Mn) element).getWidthTillPoint(g);
    // } else {
    // if (!this.containsNumber(element)) {
    // result += element.getWidth(g);
    // } else {
    // result += this.getWidthTillPoint(g, new ChildIterator(
    // this.getChildNodes()));
    //
    // }
    // }
    // }
    //
    // return result;
    // }
    //
    // /*
    // * Checks, whether provided element contains any mn - element. @return
    // * True, if contains any mn element.
    // */
    // private boolean containsNumber(final JEuclidElement element) {
    // return this.containsElement(element, Mn.ELEMENT);
    // }
    //
    // /**
    // * Finds in the list of the element mn element and requests it for the
    // * with of the '.' character (used for the "decimalpoint" alignment).
    // *
    // * @param iterator
    // * List of elements.
    // * @return Width of the point.
    // */
    // private float getPointWidth(final Graphics2D g, final Iterator
    // iterator) {
    // float result = 0;
    //
    // for (; iterator.hasNext();) {
    // final JEuclidElement element = (JEuclidElement) iterator.next();
    // if (element instanceof Mn) {
    // result = ((Mn) element).getPointWidth(g);
    // break;
    // }
    // }
    //
    // return result;
    // }
    // /**
    // * Method calculates width of elements till malignmark element.
    // *
    // * @param elements
    // * List of elements.
    // * @return Width of elements till malignmar.
    // */
    // private float getWidthTillMark(final Graphics2D g, final Iterator
    // elements) {
    // float result = 0;
    //
    // JEuclidElement element = null;
    // for (; elements.hasNext();) {
    // element = (JEuclidElement) elements.next();
    // if (element instanceof Malignmark) {
    // return result;
    // } else {
    // if (!this.containsMark(element)) {
    // result += element.getWidth(g);
    // } else {
    // result += this.getWidthTillMark(g, new ChildIterator(this
    // .getChildNodes()));
    // }
    // }
    // }
    //
    // return result;
    // }
    // /**
    // * Method checks, whether provided element contains malignmark element.
    // *
    // * @param element
    // * Container object.
    // * @return True, if element contains any malignmark object.
    // */
    // private boolean containsMark(final JEuclidElement element) {
    // return this.containsElement(element, Malignmark.ELEMENT);
    // }
    // /**
    // * Method checks, whether provided container contains element of type,
    // as
    // * searchName.
    // *
    // * @param container
    // * Container object.
    // * @param searchName
    // * Type of element to look for.
    // * @return True, if container contains such type of element.
    // */
    // private boolean containsElement(final JEuclidElement container,
    // final String searchName) {
    // if (container.getTagName().equals(searchName)) {
    // return true;
    // }
    //
    // for (int i = 0; i < container.getMathElementCount(); i++) {
    // if (this.containsElement(container.getMathElement(i), searchName)) {
    // return true;
    // }
    // }
    //
    // return false;
    // }
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
        final String[] array;
        if (string == null) {
            array = new String[0];
        } else {
            array = string.split("\\s");
        }
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

    /** {@inheritDoc} */
    public void setRowspacing(final String rowspacing) {
        this.setAttribute(Mtable.ATTR_ROWSPACING, rowspacing);
    }

    /** {@inheritDoc} */
    public String getColumnspacing() {
        return this.getMathAttribute(Mtable.ATTR_COLUMNSPACING);
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
    // CHECKSTYLE:OFF
    // Function is too long. Unfortunately it has to be for proper layout
    @Override
    public void layoutStageInvariant(final LayoutView view,
            final LayoutInfo info, final LayoutStage stage,
            final LayoutContext context) {
        // CHECKSTYLE:ON
        final Graphics2D g = view.getGraphics();
        final LayoutContext now = this.applyLocalAttributesToContext(context);
        final List<LayoutableNode> children = this.getChildrenToLayout();
        final LayoutInfo[] rowInfos = new LayoutInfo[children.size()];
        final LayoutableNode[] rowChild = new LayoutableNode[children.size()];
        float y = 0;
        int rows = 0;

        // Layout Rows vertically, calculate height of the table.
        final float vFrameSpacing = this.getFramespacingv(now);
        float height = vFrameSpacing;
        for (final LayoutableNode child : children) {
            rowChild[rows] = child;
            final LayoutInfo mtrInfo = view.getInfo(child);
            y += mtrInfo.getAscentHeight(stage);
            rowInfos[rows] = mtrInfo;
            rows++;
            mtrInfo.moveTo(0, y, stage);
            y += mtrInfo.getDescentHeight(stage);
            height = y;
            y += AttributesHelper.convertSizeToPt(this.getSpaceArrayEntry(
                    this.getRowspacing(), rows), now, AttributesHelper.PT);
        }
        height += vFrameSpacing;

        final float verticalShift = this.shiftTableVertically(stage, context,
                g, rowInfos, rows, height);

        final List<LayoutableNode>[] mtdChildren = this
                .createListOfMtdChildren(rowChild, rows);
        this.stretchAndAlignMtds(view, mtdChildren, rowInfos, rows, stage);

        final List<Float> columnwidth = this.calculateBasicColumnWidth(view,
                stage, rows, mtdChildren);

        // TODO This is where Alignment-Groups should be calculated

        final float totalWidth = this.layoutColumnsHorizontally(view, stage,
                now, rows, mtdChildren, columnwidth);

        this.setRowWidth(stage, rowInfos, rows, totalWidth);

        this.addRowLines(info, rowInfos, rows, totalWidth, stage, now);
        this.addColumnLines(info, columnwidth, verticalShift, height, stage,
                now);
        this.addFrame(info, totalWidth, verticalShift, height, now);

        final float hFrameSpacing = this.getFramespacingh(now);
        final Dimension2D borderLeftTop = new Dimension2DImpl(hFrameSpacing,
                vFrameSpacing);
        final Dimension2D borderRightBottom = new Dimension2DImpl(
                hFrameSpacing, vFrameSpacing);
        ElementListSupport.fillInfoFromChildren(view, info, this, stage,
                borderLeftTop, borderRightBottom);
    }

    private void addFrame(final LayoutInfo info, final float width,
            final float verticalShift, final float height,
            final LayoutContext now) {
        final LineType lineType = this.getFrameAsLineType();
        final boolean solid = Mtable.LineType.SOLID.equals(lineType);
        final boolean dashed = Mtable.LineType.DASHED.equals(lineType);
        if (dashed || solid) {
            final float lineWidth = GraphicsSupport.lineWidth(now);
            final float lineInset = lineWidth / 2.0f;
            final Color color = (Color) now.getParameter(Parameter.MATHCOLOR);
            final List<GraphicsObject> go = info.getGraphicObjects();
            final float vFrameSpacing = this.getFramespacingv(now);

            final float left = lineInset;
            final float right = width - lineInset;
            final float top = verticalShift + lineInset - vFrameSpacing;
            final float bottom = height + verticalShift - lineInset;
            go.add(new LineObject(left, top, right, top, lineWidth, color,
                    dashed));
            go.add(new LineObject(left, bottom, right, bottom, lineWidth,
                    color, dashed));
            go.add(new LineObject(left, top, left, bottom, lineWidth, color,
                    dashed));
            go.add(new LineObject(right, top, right, bottom, lineWidth,
                    color, dashed));
        }
    }

    private void addRowLines(final LayoutInfo info,
            final LayoutInfo[] rowInfos, final int rows, final float width,
            final LayoutStage stage, final LayoutContext now) {
        final float lineWidth = GraphicsSupport.lineWidth(now);
        final Color color = (Color) now.getParameter(Parameter.MATHCOLOR);

        final float inFrameStart = this.getFramespacingh(now);
        for (int row = 0; row < rows - 1; row++) {
            final LineType lineType = this.getRowLine(row);
            final boolean solid = Mtable.LineType.SOLID.equals(lineType);
            final boolean dashed = Mtable.LineType.DASHED.equals(lineType);
            if (dashed || solid) {
                final float y = (rowInfos[row].getPosY(stage)
                        + rowInfos[row].getDescentHeight(stage)
                        + rowInfos[row + 1].getPosY(stage) - rowInfos[row + 1]
                        .getAscentHeight(stage)) / 2.0f;
                info.getGraphicObjects().add(
                        new LineObject(inFrameStart, y, width - inFrameStart,
                                y, lineWidth, color, dashed));
            }
        }
    }

    private void addColumnLines(final LayoutInfo info,
            final List<Float> columnwidth, final float verticalShift,
            final float height, final LayoutStage stage,
            final LayoutContext now) {
        final float lineWidth = GraphicsSupport.lineWidth(now);
        final Color color = (Color) now.getParameter(Parameter.MATHCOLOR);
        float x = this.getFramespacingh(now);

        final float inFrameStart = this.getFramespacingv(now);
        final float colsm1 = columnwidth.size() - 1;
        for (int col = 0; col < colsm1; col++) {
            final LineType lineType = this.getColumnLine(col);
            final boolean solid = Mtable.LineType.SOLID.equals(lineType);
            final boolean dashed = Mtable.LineType.DASHED.equals(lineType);
            if (dashed || solid) {
                final float halfSpace = this.getSpaceAfterColumn(now, col) / 2.0f;
                x += columnwidth.get(col) + halfSpace;
                info.getGraphicObjects().add(
                        new LineObject(x, verticalShift, x, height
                                + verticalShift - inFrameStart, lineWidth,
                                color, dashed));
                x += halfSpace;
            }
        }
    }

    private void stretchAndAlignMtds(final LayoutView view,
            final List<LayoutableNode>[] mtdChildren,
            final LayoutInfo[] rowInfos, final int rows,
            final LayoutStage stage) {
        for (int i = 0; i < rows; i++) {
            final float rowAscent = rowInfos[i].getAscentHeight(stage);
            final float rowDescent = rowInfos[i].getDescentHeight(stage);
            for (final LayoutableNode n : mtdChildren[i]) {
                final LayoutInfo mtdInfo = view.getInfo(n);

                final VAlign valign = this.getVAlign((JEuclidElement) n, i);
                final float verticalShift;
                if (valign.getAlign() == Mtable.VAlign.TOP) {
                    verticalShift = -rowAscent
                            + mtdInfo.getAscentHeight(stage);
                } else if (valign.getAlign() == Mtable.VAlign.BOTTOM) {
                    verticalShift = rowDescent
                            - mtdInfo.getDescentHeight(stage);
                } else if (valign.getAlign() == Mtable.VAlign.CENTER) {
                    verticalShift = (-rowAscent + rowDescent
                            + mtdInfo.getAscentHeight(stage) - mtdInfo
                            .getDescentHeight(stage)) / 2.0f;
                } else if (valign.getAlign() == Mtable.VAlign.AXIS) {
                    // TODO: This uses center instead of axis.
                    verticalShift = (-rowAscent + rowDescent
                            + mtdInfo.getAscentHeight(stage) - mtdInfo
                            .getDescentHeight(stage)) / 2.0f;
                } else {
                    // BASELINE
                    verticalShift = 0.0f;
                }
                mtdInfo.shiftVertically(verticalShift, stage);
                mtdInfo.setStretchAscent(rowAscent + verticalShift);
                mtdInfo.setStretchDescent(rowDescent - verticalShift);
            }
        }
    }

    private float shiftTableVertically(final LayoutStage stage,
            final LayoutContext context, final Graphics2D g,
            final LayoutInfo[] rowInfos, final int rows, final float height) {
        // Shift table by given vertical alignment
        // final String alignStr = this.getAlign();
        // AlignmentType align =
        // Mtable.AlignmentType.parseAlignmentType(alignStr);
        // TODO: Proper vertical alignment;
        // This is "axis" alignment.
        final float verticalShift = -this.getMiddleShift(g, context) - height
                / 2.0f;

        for (int i = 0; i < rows; i++) {
            rowInfos[i].shiftVertically(verticalShift, stage);
        }
        return verticalShift;
    }

    @SuppressWarnings("unchecked")
    private List<LayoutableNode>[] createListOfMtdChildren(
            final LayoutableNode[] rowChild, final int rows) {
        final List<LayoutableNode>[] mtdChildren = new List[rows];
        for (int i = 0; i < rows; i++) {
            if (rowChild[i] instanceof MathMLTableRowElement) {
                mtdChildren[i] = rowChild[i].getChildrenToLayout();
            } else {
                mtdChildren[i] = new ArrayList<LayoutableNode>(1);
                mtdChildren[i].add(rowChild[i]);
            }
        }
        return mtdChildren;
    }

    private List<Float> calculateBasicColumnWidth(final LayoutView view,
            final LayoutStage stage, final int rows,
            final List<LayoutableNode>[] mtdChildren) {
        final List<Float> columnwidth = new Vector<Float>();
        for (int i = 0; i < rows; i++) {
            int missing = mtdChildren[i].size() - columnwidth.size();
            while (missing > 0) {
                columnwidth.add(0.0f);
                missing--;
            }
            int col = 0;
            for (final LayoutableNode n : mtdChildren[i]) {
                final float width = Math.max(columnwidth.get(col), view
                        .getInfo(n).getWidth(stage));
                columnwidth.set(col, width);
                col++;
            }
        }
        if (Boolean.parseBoolean(this.getEqualcolumns())) {
            this.makeEqual(columnwidth);
        }
        return columnwidth;
    }

    private void makeEqual(final List<Float> columnwidth) {
        float maxWidth = 0.0f;
        for (final Float width : columnwidth) {
            maxWidth = Math.max(width, maxWidth);
        }
        final int cols = columnwidth.size();
        for (int i = 0; i < cols; i++) {
            columnwidth.set(i, maxWidth);
        }
    }

    private void setRowWidth(final LayoutStage stage,
            final LayoutInfo[] rowInfos, final int rows,
            final float totalWidth) {
        for (int i = 0; i < rows; i++) {
            rowInfos[i].setWidth(totalWidth, stage);
        }
    }

    private float layoutColumnsHorizontally(final LayoutView view,
            final LayoutStage stage, final LayoutContext now, final int rows,
            final List<LayoutableNode>[] mtdChildren,
            final List<Float> columnwidth) {
        final float hFrameSpacing = this.getFramespacingh(now);
        float totalWidth = hFrameSpacing;
        for (int i = 0; i < rows; i++) {
            float x = hFrameSpacing;
            int col = 0;
            for (final LayoutableNode n : mtdChildren[i]) {
                final LayoutInfo mtdInfo = view.getInfo(n);
                final HAlign halign = this.getHAlign((JEuclidElement) n, col);
                final float colwi = columnwidth.get(col);
                final float xo = halign
                        .getHAlignOffset(stage, mtdInfo, colwi);
                mtdInfo.moveTo(x + xo, mtdInfo.getPosY(stage), stage);
                // mtdInfo.setWidth(colwi, stage);
                mtdInfo.setStretchWidth(colwi);
                x += colwi;
                totalWidth = Math.max(totalWidth, x);
                x += this.getSpaceAfterColumn(now, col);
                col++;
            }
        }
        return totalWidth + hFrameSpacing;
    }

    private float getSpaceAfterColumn(final LayoutContext now, final int col) {
        final float columnSpace = AttributesHelper.convertSizeToPt(this
                .getSpaceArrayEntry(this.getColumnspacing(), col), now,
                AttributesHelper.PT);
        return columnSpace;
    }

    private HAlign getHAlign(final JEuclidElement n, final int col) {
        assert n != null;
        final HAlign retVal;
        if (n instanceof MathMLTableCellElement) {
            final MathMLTableCellElement cell = (MathMLTableCellElement) n;
            final String alignString = cell.getColumnalign();
            final HAlign halign = HAlign.parseString(alignString, null);
            if (halign == null) {
                retVal = this.getHAlign(n.getParent(), col);
            } else {
                retVal = halign;
            }
        } else if (n instanceof MathMLTableRowElement) {
            final MathMLTableRowElement rowE = (MathMLTableRowElement) n;
            final String alignArray = rowE.getColumnalign();
            if ((alignArray != null) && (alignArray.length() > 0)) {
                retVal = HAlign.parseString(this.getSpaceArrayEntry(
                        alignArray, col), HAlign.CENTER);
            } else {
                retVal = this.getHAlign(n.getParent(), col);
            }
        } else if (n instanceof MathMLTableElement) {
            final MathMLTableElement table = (MathMLTableElement) n;
            final String alignArray = table.getColumnalign();
            if ((alignArray != null) && (alignArray.length() > 0)) {
                retVal = HAlign.parseString(this.getSpaceArrayEntry(
                        alignArray, col), HAlign.CENTER);
            } else {
                retVal = HAlign.CENTER;
            }
        } else {
            retVal = this.getHAlign(n.getParent(), col);
        }
        return retVal;
    }

    private VAlign getVAlign(final JEuclidElement n, final int row) {
        assert n != null;
        final VAlign retVal;
        if (n instanceof MathMLTableCellElement) {
            final MathMLTableCellElement cell = (MathMLTableCellElement) n;
            final String alignString = cell.getRowalign();
            final Mtable.VAlign valign = Mtable.VAlign
                    .parseString(alignString);
            if (valign == null) {
                retVal = this.getVAlign(n.getParent(), row);
            } else {
                retVal = valign;
            }
        } else if (n instanceof MathMLTableRowElement) {
            final MathMLTableRowElement rowE = (MathMLTableRowElement) n;
            final String alignString = rowE.getRowalign();
            final Mtable.VAlign valign = Mtable.VAlign
                    .parseString(alignString);
            if (valign == null) {
                retVal = this.getVAlign(n.getParent(), row);
            } else {
                retVal = valign;
            }
        } else if (n instanceof MathMLTableElement) {
            final MathMLTableElement table = (MathMLTableElement) n;
            final String alignArray = table.getRowalign();
            if ((alignArray != null) && (alignArray.length() > 0)) {
                retVal = Mtable.VAlign.parseString(this.getSpaceArrayEntry(
                        alignArray, row));
            } else {
                retVal = Mtable.VAlign.BASELINE_ALIGN;
            }
        } else {
            retVal = this.getVAlign(n.getParent(), row);
        }
        return retVal;
    }

}
