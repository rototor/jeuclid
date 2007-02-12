
package org.w3c.dom.mathml;


public interface MathMLFractionElement extends MathMLPresentationElement
{
    public String                 getLinethickness();
    public void                   setLinethickness(String linethickness);
    public MathMLElement          getNumerator();
    public void                   setNumerator(MathMLElement numerator);
    public MathMLElement          getDenominator();
    public void                   setDenominator(MathMLElement denominator);
};
  