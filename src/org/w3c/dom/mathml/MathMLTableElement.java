
package org.w3c.dom.mathml;

import org.w3c.dom.DOMException;

public interface MathMLTableElement extends MathMLPresentationElement
{
    public String                 getAlign();
    public void                   setAlign(String align);
    public String                 getRowalign();
    public void                   setRowalign(String rowalign);
    public String                 getColumnalign();
    public void                   setColumnalign(String columnalign);
    public String                 getGroupalign();
    public void                   setGroupalign(String groupalign);
    public String                 getAlignmentscope();
    public void                   setAlignmentscope(String alignmentscope);
    public String                 getColumnwidth();
    public void                   setColumnwidth(String columnwidth);
    public String                 getWidth();
    public void                   setWidth(String width);
    public String                 getRowspacing();
    public void                   setRowspacing(String rowspacing);
    public String                 getColumnspacing();
    public void                   setColumnspacing(String columnspacing);
    public String                 getRowlines();
    public void                   setRowlines(String rowlines);
    public String                 getColumnlines();
    public void                   setColumnlines(String columnlines);
    public String                 getFrame();
    public void                   setFrame(String frame);
    public String                 getFramespacing();
    public void                   setFramespacing(String framespacing);
    public String                 getEqualrows();
    public void                   setEqualrows(String equalrows);
    public String                 getEqualcolumns();
    public void                   setEqualcolumns(String equalcolumns);
    public String                 getDisplaystyle();
    public void                   setDisplaystyle(String displaystyle);
    public String                 getSide();
    public void                   setSide(String side);
    public String                 getMinlabelspacing();
    public void                   setMinlabelspacing(String minlabelspacing);
    public MathMLNodeList         getRows();
    public MathMLTableRowElement  insertEmptyRow(int index)
                                                         throws DOMException;
    public MathMLLabeledRowElement insertEmptyLabeledRow(int index)
                                                         throws DOMException;
    public MathMLTableRowElement  getRow(int index);
    public MathMLTableRowElement  insertRow(int index,
                                            MathMLTableRowElement newRow)
                                                         throws DOMException;
    public MathMLTableRowElement  setRow(int index,
                                         MathMLTableRowElement newRow)
                                                         throws DOMException;
    public void                   deleteRow(int index)
                                                         throws DOMException;
    public MathMLTableRowElement  removeRow(int index)
                                                         throws DOMException;
};
  