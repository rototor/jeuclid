
package org.w3c.dom.mathml;

import org.w3c.dom.DOMException;

public interface MathMLContentContainer extends MathMLContentElement, MathMLContainer
{
    public int                    getNBoundVariables();
    public MathMLConditionElement getCondition();
    public void                   setCondition(MathMLConditionElement condition)
                                                         throws DOMException;
    public MathMLElement          getOpDegree();
    public void                   setOpDegree(MathMLElement opDegree)
                                                         throws DOMException;
    public MathMLElement          getDomainOfApplication();
    public void                   setDomainOfApplication(MathMLElement domainOfApplication)
                                                         throws DOMException;
    public MathMLElement          getMomentAbout();
    public void                   setMomentAbout(MathMLElement momentAbout)
                                                         throws DOMException;
    public MathMLBvarElement      getBoundVariable(int index);
    public MathMLBvarElement      insertBoundVariable(MathMLBvarElement newBVar,
                                                      int index)
                                                         throws DOMException;
    public MathMLBvarElement      setBoundVariable(MathMLBvarElement newBVar,
                                                   int index)
                                                         throws DOMException;
    public void                   deleteBoundVariable(int index);
    public MathMLBvarElement      removeBoundVariable(int index);
};
  