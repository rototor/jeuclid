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

package net.sourceforge.jeuclid.elements.presentation.general;

import java.util.List;

import net.sourceforge.jeuclid.elements.JEuclidElement;
import net.sourceforge.jeuclid.elements.presentation.AbstractContainer;
import net.sourceforge.jeuclid.elements.support.ElementListSupport;

import org.w3c.dom.mathml.MathMLPresentationContainer;

/**
 * Represents a Math element that is painted and defined through its children.
 * 
 * @author Max Berger
 * @version $Revision$
 */
public abstract class AbstractMathElementWithChildren extends
        AbstractContainer implements MathMLPresentationContainer {
    /**
     * Default constructor.
     */
    public AbstractMathElementWithChildren() {
        super();
    }

    private List<JEuclidElement> getChildrenAsList() {
        return ElementListSupport.createListOfChildren(this);
    }

    // /**
    // * Calculates the width of all contained children.
    // *
    // * @param g
    // * Graphics context to use.
    // * @return the width.
    // */
    // public float calculateChildrenWidth(final Graphics2D g) {
    // return ElementListSupport.getWidth(g, this.getChildrenAsList());
    // }

    // /**
    // * Calculates the ascent height of all contained children children.
    // *
    // * @param g
    // * Graphics context to use.
    // * @return the ascent height.
    // */
    // public float calculateChildrenAscentHeight(final Graphics2D g) {
    // return ElementListSupport
    // .getAscentHeight(g, this.getChildrenAsList());
    // }

    // /**
    // * Calculates the descent height of all contained children children.
    // *
    // * @param g
    // * Graphics context to use.
    // * @return the ascent height.
    // */
    // public float calculateChildrenDescentHeight(final Graphics2D g) {
    // return ElementListSupport.getDescentHeight(g, this
    // .getChildrenAsList());
    // }

}
