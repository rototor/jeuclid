
package org.w3c.dom.mathml;


public interface MathMLTableCellElement extends MathMLPresentationContainer
{
    public String                 getRowspan();
    public void                   setRowspan(String rowspan);
    public String                 getColumnspan();
    public void                   setColumnspan(String columnspan);
    public String                 getRowalign();
    public void                   setRowalign(String rowalign);
    public String                 getColumnalign();
    public void                   setColumnalign(String columnalign);
    public String                 getGroupalign();
    public void                   setGroupalign(String groupalign);
    public boolean                getHasaligngroups();
    public String                 getCellindex();
};
  