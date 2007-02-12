
package org.w3c.dom.mathml;


public interface MathMLCaseElement extends MathMLContentElement
{
    public MathMLContentElement   getCaseCondition();
    public void                   setCaseCondition(MathMLContentElement caseCondition);
    public MathMLContentElement   getCaseValue();
    public void                   setCaseValue(MathMLContentElement caseValue);
};
  
