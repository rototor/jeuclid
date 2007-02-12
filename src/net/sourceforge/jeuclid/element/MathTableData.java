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

import java.util.ArrayList;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.element.generic.AbstractMathElementWithChildren;

/**
 * This class presents a cell of a table.
 * 
 */
public class MathTableData extends AbstractMathElementWithChildren {

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "mtd";

    private int m_rowspan = 1;

    private int m_columnspan = 1;

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

    // for future using
    private int[] groupsalignvalues = null;

    private ArrayList groups = new ArrayList();

    /**
     * Creates a math element.
     * 
     * @param base
     *            The base for the math element tree.
     */
    public MathTableData(MathBase base) {
        super(base);
    }

    /**
     * Adds "maligngroup" element.
     * 
     * @param element
     *            "maligngroup" element.
     */
    public void addAlignGroupElement(MathAlignGroup element) {
        groups.add(element);
    }

    /**
     * Returns array list of align groups, contained in this cell.
     * 
     * @return Array list of "maligngroup" elements.
     */
    public ArrayList getAlignGroups() {
        return groups;
    }

    /**
     * Adds mark to last added align group.
     * 
     * @param mark
     *            malignmark element.
     */
    public void addAlignMarkElement(MathAlignMark mark) {
        if (groups == null || groups.size() == 0) {
            return;
        }
        ((MathAlignGroup) groups.get(groups.size() - 1)).setMark(mark);
    }

    /**
     * @return Rowspan
     */
    public int getRowspan() {
        return m_rowspan;
    }

    /**
     * @param rowspan
     *            Rowspan
     */
    public void setRowspan(int rowspan) {
        m_rowspan = rowspan;
    }

    /**
     * @return Columnspan
     */
    public int getColumnspan() {
        return m_columnspan;
    }

    /**
     * @param columnspan
     *            Columnspan
     */
    public void setColumnspan(int columnspan) {
        m_columnspan = columnspan;
    }

    /**
     * @return Row align
     */
    public int getRowalign() {
        return m_rowalign;
    }

    /**
     * Sets property of the rowalign attribute.
     * 
     * @param rowalign
     *            new value.
     */
    public void setRowalign(int rowalign) {
        m_rowalign = rowalign;
    }

    /**
     * @return Column align
     */
    public int getColumnalign() {
        return m_columnalign;
    }

    /**
     * Sets property of the columnalign attribute.
     * 
     * @param columnalign
     *            Value
     */
    public void setColumnalign(int columnalign) {
        m_columnalign = columnalign;
    }

    /**
     * @return Group align
     */
    public int getGroupalign() {
        return m_groupalign;
    }

    /**
     * Sets property of the groupalign attribute.
     * 
     * @param groupalign
     *            Groupalign
     */
    public void setGroupalign(int groupalign) {
        m_groupalign = groupalign;
    }

    /**
     * Gets groupalign property values.
     * 
     * @return Array with tokenized values of groupalign property.
     */
    public int[] getGroupAlign() {
        return groupsalignvalues;
    }

    /**
     * Creates array with alignments for all align groups in this table.
     * 
     * @param groupalign
     *            String with table attribute "groupalign"
     */
    public void setGroupAlign(String groupalign) {
        groupsalignvalues = MathTable.createGroupAlignValues(groupalign);
    }
}
