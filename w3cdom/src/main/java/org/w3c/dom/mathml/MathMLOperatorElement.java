
package org.w3c.dom.mathml;


public interface MathMLOperatorElement extends MathMLPresentationToken
{
    public String                 getForm();
    public void                   setForm(String form);
    public String                 getFence();
    public void                   setFence(String fence);
    public String                 getSeparator();
    public void                   setSeparator(String separator);
    public String                 getLspace();
    public void                   setLspace(String lspace);
    public String                 getRspace();
    public void                   setRspace(String rspace);
    public String                 getStretchy();
    public void                   setStretchy(String stretchy);
    public String                 getSymmetric();
    public void                   setSymmetric(String symmetric);
    public String                 getMaxsize();
    public void                   setMaxsize(String maxsize);
    public String                 getMinsize();
    public void                   setMinsize(String minsize);
    public String                 getLargeop();
    public void                   setLargeop(String largeop);
    public String                 getMovablelimits();
    public void                   setMovablelimits(String movablelimits);
    public String                 getAccent();
    public void                   setAccent(String accent);
};
  
