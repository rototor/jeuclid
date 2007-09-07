/*
 * Copyright 2002 - 2007 JEuclid, http://jeuclid.sf.net
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/* $Id$ */

package net.sourceforge.jeuclid.elements;

import net.sourceforge.jeuclid.elements.support.attributes.AttributeMap;
import net.sourceforge.jeuclid.elements.support.attributes.MathVariant;
import net.sourceforge.jeuclid.layout.LayoutableNode;

import org.w3c.dom.mathml.MathMLElement;

/**
 * Interface for all MathElements within JEuclid.
 * 
 * @author Max Berger
 * @version $Revision$
 */
public interface JEuclidElement extends MathMLElement, JEuclidNode,
        LayoutableNode {

    /**
     * Sets the parent of this element.
     * 
     * @param parent
     *            Parent element
     */
    void setFakeParent(final JEuclidElement parent);

    /**
     * convenience method to set multiple attributes at once.
     * 
     * @param attributes
     *            List of attribute names and values.
     */
    void setMathAttributes(final AttributeMap attributes);

    /**
     * Gets index of child element.
     * 
     * @param element
     *            Child element.
     * @return Index of the element, -1 if element was not found
     */
    int getIndexOfMathElement(final JEuclidElement element);

    /**
     * Gets the current mathvariant.
     * 
     * @return the current MathVariant
     */
    MathVariant getMathvariantAsVariant();

    /**
     * Gets a child from this element.
     * <p>
     * Please note, that unlike the MathML DOM model functions this function
     * uses a 0-based index.
     * 
     * @param index
     *            Index of the child (0-based).
     * @return The child MathElement object.
     */
    JEuclidElement getMathElement(final int index);

    /**
     * Sets a specific child to the newElement, creating other subelements as
     * necessary.
     * 
     * @param index
     *            the index to set (0=the firt child)
     * @param newElement
     *            new element to be set as child.
     */
    void setMathElement(final int index, final MathMLElement newElement);

    /**
     * Retrieves the scriptlevel for a certain child. Some attributes increase
     * the scriptlevel for some of their children.
     * 
     * @param child
     *            element node of the child.
     * @return the scriptlevel for this particular child.
     */
    int getScriptlevelForChild(final JEuclidElement child);

    /**
     * Returns parent of this element.
     * 
     * @return Parent element.
     */
    JEuclidElement getParent();

    /**
     * returns true is the child has prescripts attached to it. In this case,
     * there should be no extra space on the left.
     * 
     * @param child
     *            child to test
     * @return true if there are attached prescripts
     */
    boolean hasChildPrescripts(final JEuclidElement child);

    /**
     * returns true is the child has postscripts attached to it. In this case,
     * there should be no extra space on the left.
     * 
     * @param child
     *            child to test
     * @return true if there are attached postscripts
     */
    boolean hasChildPostscripts(final JEuclidElement child);

    /**
     * Returns the count of childs from this element.
     * 
     * @return Count of childs.
     */
    int getMathElementCount();

}
