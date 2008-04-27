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
import java.util.List;

import net.sourceforge.jeuclid.LayoutContext;
import net.sourceforge.jeuclid.context.LayoutContextImpl;
import net.sourceforge.jeuclid.elements.JEuclidElementFactory;
import net.sourceforge.jeuclid.elements.JEuclidNode;
import net.sourceforge.jeuclid.elements.support.ElementListSupport;
import net.sourceforge.jeuclid.layout.JEuclidView;
import net.sourceforge.jeuclid.layout.LayoutInfo;
import net.sourceforge.jeuclid.layout.LayoutStage;
import net.sourceforge.jeuclid.layout.LayoutView;
import net.sourceforge.jeuclid.layout.LayoutableDocument;
import net.sourceforge.jeuclid.layout.LayoutableNode;

import org.apache.batik.dom.GenericDocument;
import org.w3c.dom.DOMException;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.mathml.MathMLDocument;
import org.w3c.dom.views.DocumentView;

/**
 * Class for MathML Document Nodes.
 * 
 * @version $Revision$
 */
public class DocumentElement extends GenericDocument implements
        MathMLDocument, JEuclidNode, DocumentView, LayoutableDocument {

    /**
     * Creates a math element.
     * 
     */
    public DocumentElement() {
        this(null);
    }

    public DocumentElement(final DocumentType doctype) {
        super(doctype, JEuclidDOMImplementation.getInstance());
        super.setEventsEnabled(true);
        this.ownerDocument = this;
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
        final List l = ElementListSupport.createListOfLayoutChildren(this);
        return l;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public List<LayoutableNode> getChildrenToDraw() {
        final List l = ElementListSupport.createListOfLayoutChildren(this);
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

    @Override
    protected Node newNode() {
        return new DocumentElement();
    }

    /**
     * <b>DOM</b>: Implements {@link
     * org.w3c.dom.Document#createElement(String)}.
     */
    @Override
    public Element createElement(final String tagName) throws DOMException {
        // TODO: This should be refactored.
        return JEuclidElementFactory.elementFromName(tagName, null, this);
    }

    @Override
    public Element createElementNS(String namespaceURI,
            final String qualifiedName) throws DOMException {
        if (namespaceURI != null && namespaceURI.length() == 0) {
            namespaceURI = null;
        }
        if (namespaceURI == null) {
            return this.createElement(qualifiedName.intern());
        } else {
            String tagname = qualifiedName;
            final int posSeparator = tagname.indexOf(":");
            if (posSeparator >= 0) {
                tagname = tagname.substring(posSeparator + 1);
            }
            final Element e = this.createElement(tagname);
            // TODO: E should contain namespaceURI
            return e;
        }
    }

}
