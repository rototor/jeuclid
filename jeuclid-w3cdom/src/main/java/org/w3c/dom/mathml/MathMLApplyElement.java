
package org.w3c.dom.mathml;

import org.w3c.dom.DOMException;

public interface MathMLApplyElement extends MathMLContentContainer
{
    public MathMLElement          getOperator();
    public void                   setOperator(MathMLElement operator);
    public MathMLElement          getLowLimit();
    public void                   setLowLimit(MathMLElement lowLimit)
                                                         throws DOMException;
    public MathMLElement          getUpLimit();
    public void                   setUpLimit(MathMLElement upLimit)
                                                         throws DOMException;
};
  
