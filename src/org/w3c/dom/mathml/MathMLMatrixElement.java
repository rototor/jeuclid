
package org.w3c.dom.mathml;

import org.w3c.dom.DOMException;

public interface MathMLMatrixElement extends MathMLContentElement
{
    public int                    getNrows();
    public int                    getNcols();
    public MathMLNodeList         getRows();
    public MathMLMatrixrowElement getRow(int index)
                                                         throws DOMException;
    public MathMLMatrixrowElement insertRow(MathMLMatrixrowElement newRow,
                                            int index)
                                                         throws DOMException;
    public MathMLMatrixrowElement setRow(MathMLMatrixrowElement newRow,
                                         int index)
                                                         throws DOMException;
    public       void                 deleteRow(int index)
                                                         throws DOMException;
    public MathMLMatrixrowElement removeRow(int index)
                                                         throws DOMException;
};
  