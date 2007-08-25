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

package net.sourceforge.jeuclid.elements.generic;

import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.jeuclid.LayoutContext;
import net.sourceforge.jeuclid.MutableLayoutContext;
import net.sourceforge.jeuclid.LayoutContext.Parameter;
import net.sourceforge.jeuclid.context.LayoutContextImpl;
import net.sourceforge.jeuclid.dom.AbstractPartialDocumentImpl;
import net.sourceforge.jeuclid.dom.ChangeTrackingInterface;
import net.sourceforge.jeuclid.elements.DisplayableNode;
import net.sourceforge.jeuclid.elements.JEuclidNode;
import net.sourceforge.jeuclid.elements.support.ElementListSupport;
import net.sourceforge.jeuclid.layout.LayoutInfo;
import net.sourceforge.jeuclid.layout.LayoutStage;
import net.sourceforge.jeuclid.layout.LayoutView;
import net.sourceforge.jeuclid.layout.LayoutableDocument;
import net.sourceforge.jeuclid.layout.LayoutableNode;

import org.w3c.dom.mathml.MathMLDocument;
import org.w3c.dom.views.AbstractView;
import org.w3c.dom.views.DocumentView;

/**
 * Class for MathML Document Nodes.
 * 
 * @author Max Berger
 * @version $Revision$
 */
public class DocumentElement extends AbstractPartialDocumentImpl implements
        MathMLDocument, JEuclidNode, ChangeTrackingInterface,
        DisplayableNode, DocumentView, LayoutableDocument {

    private final Set<ChangeTrackingInterface> listeners = new HashSet<ChangeTrackingInterface>();

    private float lastX;

    private float lastY;

    private MutableLayoutContext layoutContext;

    /**
     * Creates a math element.
     * 
     * @param rootLayoutContext
     *            The layoutContext for this rendering.
     */
    public DocumentElement(final LayoutContextImpl rootLayoutContext) {
        this.layoutContext = new LayoutContextImpl(rootLayoutContext);
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
    public void paint(final Graphics2D g, final float posX, final float posY) {
        ElementListSupport.paint(g, posX, posY, ElementListSupport
                .createListOfChildren(this));
        this.lastX = posX;
        this.lastY = posY;
    }

    /** {@inheritDoc} */
    public float getWidth(final Graphics2D g) {
        return ElementListSupport.getWidth(g, ElementListSupport
                .createListOfChildren(this));
    }

    /** {@inheritDoc} */
    public float getAscentHeight(final Graphics2D g) {
        return ElementListSupport.getAscentHeight(g, ElementListSupport
                .createListOfChildren(this));
    }

    /** {@inheritDoc} */
    public float getDescentHeight(final Graphics2D g) {
        return ElementListSupport.getDescentHeight(g, ElementListSupport
                .createListOfChildren(this));
    }

    /** {@inheritDoc} */
    public float getFontsizeInPoint() {
        return (Float) this.layoutContext.getParameter(Parameter.MATHSIZE);
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

    /** {@inheritDoc} */
    public void fireChangeForSubTree() {
        ElementListSupport.fireChangeForSubTree(ElementListSupport
                .createListOfChildren(this));
    }

    /** {@inheritDoc} */
    public float getHeight(final Graphics2D g) {
        return this.getAscentHeight(g) + this.getDescentHeight(g);
    }

    /** {@inheritDoc} */
    public float getPaintedPosX() {
        return this.lastX;
    }

    /** {@inheritDoc} */
    public float getPaintedPosY() {
        return this.lastY;
    }

    /** {@inheritDoc} */
    public float getXCenter(final Graphics2D g) {
        return this.getWidth(g) / 2;
    }

    /**
     * Sets a LayoutContext for this rendering tree.
     * 
     * @param context
     *            the new layout context.
     */
    public void setLayoutContext(final MutableLayoutContext context) {
        this.layoutContext = context;
    }

    /** {@inheritDoc} */
    public LayoutContext getChildLayoutContext(final JEuclidNode child) {
        return this.layoutContext;
    }

    /**
     * Retrieve the current layout context.
     * <p>
     * This instance is mutable. Please be sure to call
     * {@link #fireChangeForSubTree()} after any modification!
     * 
     * @return the layout context.
     */
    public MutableLayoutContext getCurrentLayoutContext() {
        return this.layoutContext;
    }

    /** {@inheritDoc} */
    // CHECKSTYLE:OFF
    public AbstractView getDefaultView() {
        // CHECKSTYLE:ON
        // TODO Auto-generated method stub
        return null;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public List<LayoutableNode> getLayoutableNodeChildren() {
        final List l = super.getChildren();
        return l;
    }

    /** {@inheritDoc} */
    public void layoutStage1(final LayoutView view, final LayoutInfo info,
            final LayoutStage childMinStage) {
        ElementListSupport.layoutSequential(view, info, this,
                LayoutStage.STAGE1, childMinStage);
    }

    /** {@inheritDoc} */
    public void layoutStage2(final LayoutView view, final LayoutInfo info) {
        ElementListSupport.layoutSequential(view, info, this,
                LayoutStage.STAGE2, LayoutStage.STAGE2);
    }

}
