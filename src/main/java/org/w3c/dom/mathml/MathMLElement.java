
package org.w3c.dom.mathml;

import org.w3c.dom.Element;

public interface MathMLElement extends Element
{
    public String                 getClassName();
    public void                   setClassName(String className);
    public String                 getMathElementStyle();
    public void                   setMathElementStyle(String mathElementStyle);
    public String                 getId();
    public void                   setId(String id);
    public String                 getXref();
    public void                   setXref(String xref);
    public String                 getHref();
    public void                   setHref(String href);
    public MathMLMathElement      getOwnerMathElement();
};
  
