
package org.w3c.dom.mathml;

import org.w3c.dom.DOMException;

public interface MathMLUnderOverElement extends MathMLPresentationElement
{
    public String                 getAccentunder();
    public void                   setAccentunder(String accentunder);
    public String                 getAccent();
    public void                   setAccent(String accent);
    public MathMLElement          getBase();
    public void                   setBase(MathMLElement base);
    public MathMLElement          getUnderscript();
    public void                   setUnderscript(MathMLElement underscript)
                                                         throws DOMException;
    public MathMLElement          getOverscript();
    public void                   setOverscript(MathMLElement overscript)
                                                         throws DOMException;
};
  