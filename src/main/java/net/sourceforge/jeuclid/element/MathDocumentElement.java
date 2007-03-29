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

import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.jeuclid.MathBase;
import net.sourceforge.jeuclid.dom.AbstractPartialDocumentImpl;
import net.sourceforge.jeuclid.dom.ChangeTrackingInterface;
import net.sourceforge.jeuclid.element.generic.MathNode;
import net.sourceforge.jeuclid.element.helpers.ElementListSupport;

import org.w3c.dom.mathml.MathMLDocument;

/**
 * Class for MathML Document Nodes.
 * 
 * @author Max Berger
 */
public class MathDocumentElement extends AbstractPartialDocumentImpl
        implements MathMLDocument, MathNode, ChangeTrackingInterface {

    private MathBase mathbase;

    private final Set<ChangeTrackingInterface> listeners = new HashSet<ChangeTrackingInterface>();

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
        ElementListSupport.paint(g, posX, posY, ElementListSupport
                .createListOfChildren(this));
    }

    /** {@inheritDoc} */
    public int getWidth(final Graphics2D g) {
        return ElementListSupport.getWidth(g, ElementListSupport
                .createListOfChildren(this));
    }

    /** {@inheritDoc} */
    public int getAscentHeight(final Graphics2D g) {
        return ElementListSupport.getAscentHeight(g, ElementListSupport
                .createListOfChildren(this));
    }

    /** {@inheritDoc} */
    public int getDescentHeight(final Graphics2D g) {
        return ElementListSupport.getDescentHeight(g, ElementListSupport
                .createListOfChildren(this));
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

    /** {@inheritDoc} */
    public float getMathsizeInPoint() {
        return this.mathbase.getFontSize();
    }

    /** {@inheritDoc} */
    public float getFontsizeInPoint() {
        return this.mathbase.getFontSize();
    }

    /** {@inheritDoc} */
    public void addListener(final ChangeTrackingInterface listener) {
        this.listeners.add(listener);
    }

    /** {@inheritDoc} */
    public void fireChanged(final boolean propagate) {
        if (propagate) {
            for (final ChangeTrackingInterface listener : this.listeners) {
                listener.fireChanged(false);
            }
        }

    }

}
