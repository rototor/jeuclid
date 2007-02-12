
package org.w3c.dom.mathml;


public interface MathMLFencedElement extends MathMLPresentationContainer
{
    public String                 getOpen();
    public void                   setOpen(String open);
    public String                 getClose();
    public void                   setClose(String close);
    public String                 getSeparators();
    public void                   setSeparators(String separators);
};
  