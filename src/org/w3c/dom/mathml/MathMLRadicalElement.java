
package org.w3c.dom.mathml;


public interface MathMLRadicalElement extends MathMLPresentationElement
{
    public MathMLElement          getRadicand();
    public void                   setRadicand(MathMLElement radicand);
    public MathMLElement          getIndex();
    public void                   setIndex(MathMLElement index);
};
  