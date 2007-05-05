
package org.w3c.dom.mathml;

import org.w3c.dom.Node;

public interface MathMLContentToken extends MathMLContentElement
{
    public MathMLNodeList         getArguments();
    public String                 getDefinitionURL();
    public void                   setDefinitionURL(String definitionURL);
    public String                 getEncoding();
    public void                   setEncoding(String encoding);
    public Node                   getArgument(int index);
    public Node                   insertArgument(Node newArgument,
                                                 int index);
    public Node                   setArgument(Node newArgument,
                                              int index);
    public void                   deleteArgument(int index);
    public Node                   removeArgument(int index);
};
  
