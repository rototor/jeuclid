
package org.w3c.dom.mathml;


public interface MathMLIntervalElement extends MathMLContentElement
{
    public String                 getClosure();
    public void                   setClosure(String closure);
    public MathMLCnElement        getStart();
    public void                   setStart(MathMLCnElement start);
    public MathMLCnElement        getEnd();
    public void                   setEnd(MathMLCnElement end);
};
  