
package org.w3c.dom.mathml;

import org.w3c.dom.DOMException;

public interface MathMLPiecewiseElement extends MathMLContentElement
{
    public MathMLNodeList         getPieces();
    public MathMLContentElement   getOtherwise();
    public void                   setOtherwise(MathMLContentElement otherwise);
    public MathMLCaseElement      getCase(int index);
    public MathMLCaseElement      setCase(int index,
                                          MathMLCaseElement caseElement)
                                                         throws DOMException;
    public void                   deleteCase(int index)
                                                         throws DOMException;
    public MathMLCaseElement      removeCase(int index)
                                                         throws DOMException;
    public MathMLCaseElement      insertCase(int index,
                                             MathMLCaseElement newCase)
                                                         throws DOMException;
    public MathMLContentElement   getCaseValue(int index)
                                                         throws DOMException;
    public MathMLContentElement   setCaseValue(int index,
                                               MathMLContentElement value)
                                                         throws DOMException;
    public MathMLContentElement   getCaseCondition(int index)
                                                         throws DOMException;
    public MathMLContentElement   setCaseCondition(int index,
                                                   MathMLContentElement condition)
                                                         throws DOMException;
};
  
