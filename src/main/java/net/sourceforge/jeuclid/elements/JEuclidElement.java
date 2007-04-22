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

import java.awt.Color;
import java.awt.Graphics2D;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.elements.support.attributes.AttributeMap;
import net.sourceforge.jeuclid.elements.support.attributes.MathVariant;

import org.w3c.dom.mathml.MathMLElement;

/**
 * Interface for all MathElements within JEuclid.
 * 
 * @author Max Berger
 * @version $Revision$
 */
public interface JEuclidElement extends MathMLElement, DisplayableNode,
        JEuclidNode {

    /**
     * Sets the parent of this element.
     * 
     * @param parent
     *            Parent element
     */
    void setFakeParent(final JEuclidElement parent);

    /**
     * This method is called when all attributes for the element are known.
     * The element contents are not necesarrily complete.
     * 
     * @param attributes
     *            List of attribute names and values.
     */
    void setMathAttributes(final AttributeMap attributes);

    /**
     * returns true if the parent is currently calculating its size.
     * 
     * @return true if parent is caculating size.
     */
    boolean isCalculatingSize();

    /**
     * Returns the current height of the upper part (over the base line).
     * 
     * @return Height of the upper part.
     * @param g
     *            Graphics2D context to use.
     */
    float calculateAscentHeight(Graphics2D g);

    /**
     * Calculates descent height (under the base line) of the element.
     * 
     * @return Descent height value.
     * @param g
     *            Graphics2D context to use.
     */
    float calculateDescentHeight(Graphics2D g);

    /**
     * @param calculatingSize
     *            the calculatingSize to set
     */
    void setCalculatingSize(final boolean calculatingSize);

    /**
     * Returns background color of the element.
     * 
     * @return Color object.
     */
    Color getBackgroundColor();

    /**
     * Gets the color that this element is supposed to use for rendering its
     * foreground elements.
     * 
     * @return a color.
     */
    Color getForegroundColor();

    /**
     * Returns value of the vertical shift for the specific elements in the
     * line. This applies to "munderover", "msubsup", "mover", etc.. In case
     * such elements containes enlarged operator, other elements on the right
     * should be positioned in the center of the line. Value of the shift is
     * stored in the top-level element of the line.
     * 
     * @return Value of the corrector of the line.
     * @see #setGlobalLineCorrector(float)
     */
    float getGlobalLineCorrector();

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
     * Sets value of the vertical shift for the specific elements in the line.
     * This applies to "munderover", "msubsup", "mover", etc.. In case such
     * elements containes enlarged operator, other elements on the right
     * should be positioned in the center of the line regarding such elements.
     * Value of the shift is stored in the top-level element of the line.
     * 
     * @param corrector
     *            Value of corrector.
     * @see #getGlobalLineCorrector()
     */
    void setGlobalLineCorrector(final float corrector);

    /**
     * Gets a child from this element.
     * 
     * @param index
     *            Index of the child.
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
     * Returns true if the child should be displayed as a block (not inline).
     * Roughly corresponds to the "displaystyle" property (3.2.5.9)
     * 
     * @param child
     *            child to test
     * @return true if child is block.
     */
    boolean isChildBlock(final JEuclidElement child);

    /**
     * Returns the count of childs from this element.
     * 
     * @return Count of childs.
     */
    int getMathElementCount();

    /**
     * Gets the math base.
     * 
     * @return Math base object.
     */
    MathBase getMathBase();

}
