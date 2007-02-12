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

/* $Id: MathTableData.java,v 1.1.2.3 2007/02/06 18:34:56 maxberger Exp $ */

package net.sourceforge.jeuclid.element;

import java.util.List;
import java.util.Vector;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.element.generic.AbstractRowLikeElement;

/**
 * This class presents a cell of a table.
 * 
 */
public class MathTableData extends AbstractRowLikeElement {

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

    private final List<MathAlignGroup> groups = new Vector<MathAlignGroup>();

    /**
     * Creates a math element.
     * 
     * @param base
     *            The base for the math element tree.
     */
    public MathTableData(final MathBase base) {
        super(base);
    }

    /**
     * Adds "maligngroup" element.
     * 
     * @param element
     *            "maligngroup" element.
     */
    public void addAlignGroupElement(final MathAlignGroup element) {
        this.groups.add(element);
    }

    /**
     * Returns array list of align groups, contained in this cell.
     * 
     * @return Array list of "maligngroup" elements.
     */
    public List<MathAlignGroup> getAlignGroups() {
        return this.groups;
    }

    /**
     * Adds mark to last added align group.
     * 
     * @param mark
     *            malignmark element.
     */
    public void addAlignMarkElement(final MathAlignMark mark) {
        if (this.groups == null || this.groups.size() == 0) {
            return;
        }
        (this.groups.get(this.groups.size() - 1)).setMark(mark);
    }

    /**
     * @return Rowspan
     */
    public int getRowspan() {
        return this.m_rowspan;
    }

    /**
     * @param rowspan
     *            Rowspan
     */
    public void setRowspan(final int rowspan) {
        this.m_rowspan = rowspan;
    }

    /**
     * @return Columnspan
     */
    public int getColumnspan() {
        return this.m_columnspan;
    }

    /**
     * @param columnspan
     *            Columnspan
     */
    public void setColumnspan(final int columnspan) {
        this.m_columnspan = columnspan;
    }

    /**
     * @return Row align
     */
    public int getRowalign() {
        return this.m_rowalign;
    }

    /**
     * Sets property of the rowalign attribute.
     * 
     * @param rowalign
     *            new value.
     */
    public void setRowalign(final int rowalign) {
        this.m_rowalign = rowalign;
    }

    /**
     * @return Column align
     */
    public int getColumnalign() {
        return this.m_columnalign;
    }

    /**
     * Sets property of the columnalign attribute.
     * 
     * @param columnalign
     *            Value
     */
    public void setColumnalign(final int columnalign) {
        this.m_columnalign = columnalign;
    }

    /**
     * @return Group align
     */
    public int getGroupalign() {
        return this.m_groupalign;
    }

    /**
     * Sets property of the groupalign attribute.
     * 
     * @param groupalign
     *            Groupalign
     */
    public void setGroupalign(final int groupalign) {
        this.m_groupalign = groupalign;
    }

    /**
     * Gets groupalign property values.
     * 
     * @return Array with tokenized values of groupalign property.
     */
    public int[] getGroupAlign() {
        return this.groupsalignvalues;
    }

    /**
     * Creates array with alignments for all align groups in this table.
     * 
     * @param groupalign
     *            String with table attribute "groupalign"
     */
    public void setGroupAlign(final String groupalign) {
        this.groupsalignvalues = MathTable.createGroupAlignValues(groupalign);
    }

    /** {@inheritDoc} */
    public String getTagName() {
        return ELEMENT;
    }

}
