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

/* $Id: AbstractMathElementWithChildren.java,v 1.3.2.2 2006/11/04 04:28:29 maxberger Exp $ */

package net.sourceforge.jeuclid.element.generic;

import java.awt.Graphics;

import net.sourceforge.jeuclid.MathBase;

/**
 * Represents a Math element that is painted and defined through its children.
 * 
 * @author Max Berger
 */
public abstract class AbstractMathElementWithChildren extends
        AbstractMathElement {
    /**
     * Default constructor.
     * 
     * @param base
     *            MathBase to use.
     */
    public AbstractMathElementWithChildren(MathBase base) {
        super(base);
    }

    /** {@inheritDoc} */
    public void paint(Graphics g, int posX, int posY) {
        super.paint(g, posX, posY);

        int pos = posX;
        AbstractMathElement child = null;

        for (int i = 0; i < getMathElementCount(); i++) {
            child = getMathElement(i);
            child.paint(g, pos, posY);
            pos += child.getWidth(g);
        }
    }

    /** {@inheritDoc} */
    public int calculateWidth(Graphics g) {
        int width = 0;
        final org.w3c.dom.NodeList childList = this.getChildNodes();
        for (int i = 0; i < childList.getLength(); i++) {
            final AbstractMathElement element = (AbstractMathElement) childList
                    .item(i);
            width += ((AbstractMathElement) element).getWidth(g);
        }
        return width;
    }

    /** {@inheritDoc} */
    public int calculateAscentHeight(Graphics g) {
        int height = 0;
        final org.w3c.dom.NodeList childList = this.getChildNodes();
        for (int i = 0; i < childList.getLength(); i++) {
            final AbstractMathElement element = (AbstractMathElement) childList
                    .item(i);
            height = Math.max(height, element.getAscentHeight(g));
        }

        return height;
    }

    /** {@inheritDoc} */
    public int calculateDescentHeight(Graphics g) {
        int height = 0;

        final org.w3c.dom.NodeList childList = this.getChildNodes();
        for (int i = 0; i < childList.getLength(); i++) {
            final AbstractMathElement element = (AbstractMathElement) childList
                    .item(i);
            height = Math.max(height, element.getDescentHeight(g));
        }

        return height;
    }

}
