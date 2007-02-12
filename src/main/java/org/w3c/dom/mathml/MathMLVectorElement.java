
package org.w3c.dom.mathml;

import org.w3c.dom.DOMException;

public interface MathMLVectorElement extends MathMLContentElement
{
    public int                    getNcomponents();
    public MathMLContentElement   getComponent(int index);
    public MathMLContentElement   insertComponent(MathMLContentElement newComponent,
                                                  int index)
                                                         throws DOMException;
    public MathMLContentElement   setComponent(MathMLContentElement newComponent,
                                               int index)
                                                         throws DOMException;
    public               void         deleteComponent(int index)
                                                         throws DOMException;
    public MathMLContentElement   removeComponent(int index);
};
  
