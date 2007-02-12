
package org.w3c.dom.mathml;

import org.w3c.dom.DOMException;

public interface MathMLMultiScriptsElement extends MathMLPresentationElement
{
    public String                 getSubscriptshift();
    public void                   setSubscriptshift(String subscriptshift);
    public String                 getSuperscriptshift();
    public void                   setSuperscriptshift(String superscriptshift);
    public MathMLElement          getBase();
    public void                   setBase(MathMLElement base);
    public MathMLNodeList         getPrescripts();
    public MathMLNodeList         getScripts();
    public int                    getNumprescriptcolumns();
    public int                    getNumscriptcolumns();
    public MathMLElement          getPreSubScript(int colIndex);
    public MathMLElement          getSubScript(int colIndex);
    public MathMLElement          getPreSuperScript(int colIndex);
    public MathMLElement          getSuperScript(int colIndex);
    public MathMLElement          insertPreSubScriptBefore(int colIndex,
                                                           MathMLElement newScript)
                                                         throws DOMException;
    public MathMLElement          setPreSubScriptAt(int colIndex,
                                                    MathMLElement newScript)
                                                         throws DOMException;
    public MathMLElement          insertSubScriptBefore(int colIndex,
                                                        MathMLElement newScript)
                                                         throws DOMException;
    public MathMLElement          setSubScriptAt(int colIndex,
                                                 MathMLElement newScript)
                                                         throws DOMException;
    public MathMLElement          insertPreSuperScriptBefore(int colIndex,
                                                             MathMLElement newScript)
                                                         throws DOMException;
    public MathMLElement          setPreSuperScriptAt(int colIndex,
                                                      MathMLElement newScript)
                                                         throws DOMException;
    public MathMLElement          insertSuperScriptBefore(int colIndex,
                                                          MathMLElement newScript)
                                                         throws DOMException;
    public MathMLElement          setSuperScriptAt(int colIndex,
                                                   MathMLElement newScript)
                                                         throws DOMException;
};
  
