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
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.jeuclid.LayoutContext;
import net.sourceforge.jeuclid.context.LayoutContextImpl;
import net.sourceforge.jeuclid.dom.AbstractPartialDocumentImpl;
import net.sourceforge.jeuclid.dom.ChangeTrackingInterface;
import net.sourceforge.jeuclid.elements.JEuclidNode;
import net.sourceforge.jeuclid.elements.support.ElementListSupport;
import net.sourceforge.jeuclid.layout.JEuclidView;
import net.sourceforge.jeuclid.layout.LayoutInfo;
import net.sourceforge.jeuclid.layout.LayoutStage;
import net.sourceforge.jeuclid.layout.LayoutView;
import net.sourceforge.jeuclid.layout.LayoutableDocument;
import net.sourceforge.jeuclid.layout.LayoutableNode;

import org.w3c.dom.mathml.MathMLDocument;
import org.w3c.dom.views.DocumentView;

/**
 * Class for MathML Document Nodes.
 * 
 * @version $Revision$
 */
public class DocumentElement extends AbstractPartialDocumentImpl implements
        MathMLDocument, JEuclidNode, ChangeTrackingInterface, DocumentView,
        LayoutableDocument {

    private final Set<ChangeTrackingInterface> listeners = new HashSet<ChangeTrackingInterface>();

    /**
     * Creates a math element.
     * 
     */
    public DocumentElement() {
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
    public LayoutContext getChildLayoutContext(final int childNum,
            final LayoutContext context) {
        return context;
    }

    /** {@inheritDoc} */
    // CHECKSTYLE:OFF
    public JEuclidView getDefaultView() {
        // CHECKSTYLE:ON
        final Image tempimage = new BufferedImage(1, 1,
                BufferedImage.TYPE_INT_ARGB);
        final Graphics2D tempg = (Graphics2D) tempimage.getGraphics();
        return new JEuclidView(this, LayoutContextImpl
                .getDefaultLayoutContext(), tempg);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public List<LayoutableNode> getChildrenToLayout() {
        final List l = super.getChildren();
        return l;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public List<LayoutableNode> getChildrenToDraw() {
        final List l = super.getChildren();
        return l;
    }

    /** {@inheritDoc} */
    public void layoutStage1(final LayoutView view, final LayoutInfo info,
            final LayoutStage childMinStage, final LayoutContext context) {
        ElementListSupport.layoutSequential(view, info, this
                .getChildrenToLayout(), LayoutStage.STAGE1);
        info.setLayoutStage(childMinStage);
    }

    /** {@inheritDoc} */
    public void layoutStage2(final LayoutView view, final LayoutInfo info,
            final LayoutContext context) {
        ElementListSupport.layoutSequential(view, info, this
                .getChildrenToLayout(), LayoutStage.STAGE2);
        info.setLayoutStage(LayoutStage.STAGE2);
    }

}
