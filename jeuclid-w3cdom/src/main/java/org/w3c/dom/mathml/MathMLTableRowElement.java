
package org.w3c.dom.mathml;

import org.w3c.dom.DOMException;

public interface MathMLTableRowElement extends MathMLPresentationElement
{
    public String                 getRowalign();
    public void                   setRowalign(String rowalign);
    public String                 getColumnalign();
    public void                   setColumnalign(String columnalign);
    public String                 getGroupalign();
    public void                   setGroupalign(String groupalign);
    public MathMLNodeList         getCells();
    public MathMLTableCellElement insertEmptyCell(int index)
                                                         throws DOMException;
    public MathMLTableCellElement insertCell(MathMLTableCellElement newCell,
                                             int index)
                                                         throws DOMException;
    public MathMLTableCellElement setCell(MathMLTableCellElement newCell,
                                          int index);
    public void                   deleteCell(int index);
};
  
