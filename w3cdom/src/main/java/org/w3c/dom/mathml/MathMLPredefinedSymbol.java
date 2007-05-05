
package org.w3c.dom.mathml;


public interface MathMLPredefinedSymbol extends MathMLContentElement
{
    public String                 getDefinitionURL();
    public void                   setDefinitionURL(String definitionURL);
    public String                 getEncoding();
    public void                   setEncoding(String encoding);
    public String                 getArity();
    public String                 getSymbolName();
};
  
