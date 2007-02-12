
package org.w3c.dom.mathml;

import org.w3c.dom.DOMException;

public interface MathMLScriptElement extends MathMLPresentationElement
{
    public String                 getSubscriptshift();
    public void                   setSubscriptshift(String subscriptshift);
    public String                 getSuperscriptshift();
    public void                   setSuperscriptshift(String superscriptshift);
    public MathMLElement          getBase();
    public void                   setBase(MathMLElement base);
    public MathMLElement          getSubscript();
    public void                   setSubscript(MathMLElement subscript)
                                                         throws DOMException;
    public MathMLElement          getSuperscript();
    public void                   setSuperscript(MathMLElement superscript)
                                                         throws DOMException;
};
  
