
package org.w3c.dom.mathml;

import org.w3c.dom.Document;

public interface MathMLDocument extends Document
{
    public String                 getReferrer();
    public String                 getDomain();
    public String                 getURI();
};
  
