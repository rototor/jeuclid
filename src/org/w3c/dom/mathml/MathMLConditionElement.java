
package org.w3c.dom.mathml;


public interface MathMLConditionElement extends MathMLContentElement
{
    public MathMLApplyElement     getCondition();
    public void                   setCondition(MathMLApplyElement condition);
};
  