/*
 * Copyright 2002 - 2006 JEuclid, http://jeuclid.sf.net
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

package net.sourceforge.jeuclid.element;

import java.awt.Graphics;
import java.util.ArrayList;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.element.generic.AbstractInvisibleMathElement;
import net.sourceforge.jeuclid.element.generic.AbstractMathElement;

/**
 * This class represents the maligngroup tag.
 * 
 * @author PG
 * @since Jan 20, 2005
 */

public class MathAlignGroup extends AbstractInvisibleMathElement {

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "maligngroup";

    protected int width = 0;

    private MathAlignMark m_mark = null;

    /**
     * Creates a math element.
     * 
     * @param base
     *            The base for the math element tree.
     */
    public MathAlignGroup(MathBase base) {
        super(base);
    }

    /**
     * Return the current width of this element. Initially it's zero, but
     * MathTable after calculating will give it the right value.
     * 
     * @return Width of this element
     */

    public int getWidth() {
        return width;
    }

    /** {@inheritDoc} */
    public int calculateWidth(Graphics g) {
        return width;
    }

    /**
     * @param mark
     *            MathAlignMark
     */
    protected void setMark(MathAlignMark mark) {
        if (m_mark == null) {
            m_mark = mark;
        }
    }

    /**
     * @return mark
     */
    protected MathAlignMark getMark() {
        return m_mark;
    }

    /**
     * @param elements
     *            Listof elements
     * @return width of all elements
     * @param g
     *            Graphics context to use.
     */
    protected static int getElementsWholeWidth(Graphics g, ArrayList elements) {
        int result = 0;

        if (elements == null || elements.size() == 0) {
            return result;
        }

        for (int i = 0; i < elements.size(); i++) {
            if (elements.get(i) != null) {
                result += ((AbstractMathElement) elements.get(i)).getWidth(g);
            }
        }

        return result;
    }

    /**
     * @param alignGroupElement
     *            maligngroup element
     * @return list of elements of the maligngroup
     */
    protected static ArrayList getElementsOfAlignGroup(
            AbstractMathElement alignGroupElement) {
        ArrayList result = new ArrayList();

        AbstractMathElement parent = alignGroupElement.getParent();
        int index = parent.getIndexOfMathElement(alignGroupElement) + 1;
        AbstractMathElement current = parent.getMathElement(index);
        boolean searching = true;

        while (searching) {
            if (parent.getMathElementCount() == index) { // end of parent
                if (parent instanceof MathTableRow
                        || parent instanceof MathRootElement) {
                    // parent is tablerow or root, exit
                    break;
                }
                index = parent.getParent().getIndexOfMathElement(parent) + 1;
                parent = parent.getParent();
                current = parent.getMathElement(index);
                // going out from mrow or something...
                continue;
            } else { // parent elements didn't over
                if (current instanceof MathRow) {
                    parent = current; // go inside mrow
                    current = parent.getMathElement(0);
                    index = 0;
                    continue;
                } else {
                    if (current instanceof MathAlignGroup) {
                        break; // we've found next aligngroup element, stop
                        // search
                    } else {
                        result.add(current); // adding element, continue loop
                    }

                    current = parent.getMathElement(index + 1); // next element
                    index++;
                }
            }
        }

        return result;
    }
}
