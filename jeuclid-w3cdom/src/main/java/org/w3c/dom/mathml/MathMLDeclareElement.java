
package org.w3c.dom.mathml;


public interface MathMLDeclareElement extends MathMLContentElement
{
    public String                 getType();
    public void                   setType(String type);
    public int                    getNargs();
    public void                   setNargs(int nargs);
    public String                 getOccurrence();
    public void                   setOccurrence(String occurrence);
    public String                 getDefinitionURL();
    public void                   setDefinitionURL(String definitionURL);
    public String                 getEncoding();
    public void                   setEncoding(String encoding);
    public MathMLCiElement        getIdentifier();
    public void                   setIdentifier(MathMLCiElement identifier);
    public MathMLElement          getConstructor();
    public void                   setConstructor(MathMLElement constructor);
};
  
