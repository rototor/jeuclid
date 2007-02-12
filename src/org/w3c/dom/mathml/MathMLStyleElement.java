
package org.w3c.dom.mathml;


public interface MathMLStyleElement extends MathMLPresentationContainer
{
    public String                 getScriptlevel();
    public void                   setScriptlevel(String scriptlevel);
    public String                 getDisplaystyle();
    public void                   setDisplaystyle(String displaystyle);
    public String                 getScriptsizemultiplier();
    public void                   setScriptsizemultiplier(String scriptsizemultiplier);
    public String                 getScriptminsize();
    public void                   setScriptminsize(String scriptminsize);
    public String                 getColor();
    public void                   setColor(String color);
    public String                 getBackground();
    public void                   setBackground(String background);
};
  