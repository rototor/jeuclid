
package org.w3c.dom.mathml;

import org.w3c.dom.DOMException;

public interface MathMLSemanticsElement extends MathMLElement
{
    public MathMLElement          getBody();
    public void                   setBody(MathMLElement body);
    public int                    getNAnnotations();
    public MathMLElement          getAnnotation(int index);
    public MathMLElement          insertAnnotation(MathMLElement newAnnotation,
                                                   int index)
                                                         throws DOMException;
    public MathMLElement          setAnnotation(MathMLElement newAnnotation,
                                                int index)
                                                         throws DOMException;
    public void                   deleteAnnotation(int index);
    public MathMLElement          removeAnnotation(int index);
};
  