
package org.w3c.dom.mathml;

import org.w3c.dom.DOMException;

public interface MathMLContainer
{
    public int                    getNArguments();
    public MathMLNodeList         getArguments();
    public MathMLNodeList         getDeclarations();
    public MathMLElement          getArgument(int index)
                                                         throws DOMException;
    public MathMLElement          setArgument(MathMLElement newArgument,
                                              int index)
                                                         throws DOMException;
    public MathMLElement          insertArgument(MathMLElement newArgument,
                                                 int index)
                                                         throws DOMException;
    public void                   deleteArgument(int index)
                                                         throws DOMException;
    public MathMLElement          removeArgument(int index)
                                                         throws DOMException;
    public MathMLDeclareElement   getDeclaration(int index)
                                                         throws DOMException;
    public MathMLDeclareElement   setDeclaration(MathMLDeclareElement newDeclaration,
                                                 int index)
                                                         throws DOMException;
    public MathMLDeclareElement   insertDeclaration(MathMLDeclareElement newDeclaration,
                                                    int index)
                                                         throws DOMException;
    public MathMLDeclareElement   removeDeclaration(int index)
                                                         throws DOMException;
    public void                   deleteDeclaration(int index)
                                                         throws DOMException;
};
  