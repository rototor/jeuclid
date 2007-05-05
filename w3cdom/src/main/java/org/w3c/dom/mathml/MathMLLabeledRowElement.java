
package org.w3c.dom.mathml;

import org.w3c.dom.DOMException;

public interface MathMLLabeledRowElement extends MathMLTableRowElement
{
    public MathMLElement          getLabel();
    public void                   setLabel(MathMLElement label)
                                                         throws DOMException;
};
  
