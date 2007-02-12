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

package net.sourceforge.jeuclid.element;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.dom.AbstractPartialDocumentImpl;
import net.sourceforge.jeuclid.element.generic.MathElement;
import net.sourceforge.jeuclid.element.helpers.ElementListSupport;

import org.w3c.dom.mathml.MathMLDocument;

/**
 * Class for MathML Document Nodes.
 * 
 * @author Max Berger
 */
public class MathDocumentElement extends AbstractPartialDocumentImpl
        implements MathMLDocument {

    private MathBase mathbase;

    /**
     * Creates a math element.
     * 
     * @param base
     *            The base for the math element tree.
     */
    public MathDocumentElement(final MathBase base) {
        this.mathbase = base;
    }

    /** {@inheritDoc} */
    public String getDomain() {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public String getReferrer() {
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public String getURI() {
        throw new UnsupportedOperationException();
    }

    /**
     * This method is called, when all content of the element is known. In
     * this method elements are supposed to make all necessary size
     * pre-calculations, content examination and other font-related
     * preparations.
     */
    public void eventAllElementsComplete() {
        // TODO: This is duplicated in AbstractMathElement
        final org.w3c.dom.NodeList childList = this.getChildNodes();
        for (int i = 0; i < childList.getLength(); i++) {
            ((MathElement) childList.item(i)).eventAllElementsComplete();
        }
    }

    private List<MathElement> getChildrenAsList() {
        // TODO: This is duplicate code!
        final org.w3c.dom.NodeList childrenNodeList = this.getChildNodes();
        final List<MathElement> children = new ArrayList<MathElement>(
                childrenNodeList.getLength());
        for (int i = 0; i < childrenNodeList.getLength(); i++) {
            children.add((MathElement) childrenNodeList.item(i));
        }
        return children;
    }

    /**
     * Paints the whole MathML document.
     * 
     * @param g
     *            Graphics2D context.
     * @param posX
     *            x-offset to start from.
     * @param posY
     *            y-offset to start from.
     */
    public void paint(final Graphics2D g, final int posX, final int posY) {
        ElementListSupport.paint(g, posX, posY, this.getChildrenAsList());
    }

    /** {@inheritDoc} */
    public int getWidth(final Graphics2D g) {
        return ElementListSupport.getWidth(g, this.getChildrenAsList());
    }

    /** {@inheritDoc} */
    public int getAscentHeight(final Graphics2D g) {
        return ElementListSupport
                .getAscentHeight(g, this.getChildrenAsList());
    }

    /** {@inheritDoc} */
    public int getDescentHeight(final Graphics2D g) {
        return ElementListSupport.getDescentHeight(g, this
                .getChildrenAsList());
    }

    /**
     * Set the MathBase to use within this document tree.
     * 
     * @param base
     *            the MathBase object to use.
     */
    public void setMathBase(final MathBase base) {
        this.mathbase = base;
    }

    /**
     * Paints this component and all of its elements.
     * 
     * @param g2
     *            The graphics context to use for painting
     */
    public void paint(final Graphics2D g2) {
        final int height = this.getAscentHeight(g2)
                + this.getDescentHeight(g2);
        if (this.mathbase.isDebug()) {
            g2.setColor(Color.blue);
            g2.drawLine(0, 0, this.getWidth(g2) - 1, 0);
            g2.drawLine(this.getWidth(g2) - 1, 0, this.getWidth(g2) - 1,
                    height - 1);
            g2.drawLine(0, 0, 0, height - 1);
            g2.drawLine(0, height - 1, this.getWidth(g2) - 1, height - 1);

            g2.setColor(Color.cyan);
            g2.drawLine(0, height / 2, this.getWidth(g2) - 1, height / 2);

            g2.setColor(Color.black);
        }
        this.paint(g2, 0, this.getAscentHeight(g2));
    }

}
