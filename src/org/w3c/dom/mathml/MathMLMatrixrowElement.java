
package org.w3c.dom.mathml;

import org.w3c.dom.DOMException;

public interface MathMLMatrixrowElement extends MathMLContentElement
{
    public int                    getNEntries();
    public MathMLContentElement   getEntry(int index)
                                                         throws DOMException;
    public MathMLContentElement   insertEntry(MathMLContentElement newEntry,
                                              int index)
                                                         throws DOMException;
    public MathMLContentElement   setEntry(MathMLContentElement newEntry,
                                           int index)
                                                         throws DOMException;
    public       void                 deleteEntry(int index)
                                                         throws DOMException;
    public MathMLContentElement   removeEntry(int index)
                                                         throws DOMException;
};
  