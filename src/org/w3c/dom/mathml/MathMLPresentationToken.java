
package org.w3c.dom.mathml;


public interface MathMLPresentationToken extends MathMLPresentationElement
{
    public String                 getMathvariant();
    public void                   setMathvariant(String mathvariant);
    public String                 getMathsize();
    public void                   setMathsize(String mathsize);
    public String                 getMathcolor();
    public void                   setMathcolor(String mathcolor);
    public String                 getMathbackground();
    public void                   setMathbackground(String mathbackground);
    public MathMLNodeList         getContents();
};
  