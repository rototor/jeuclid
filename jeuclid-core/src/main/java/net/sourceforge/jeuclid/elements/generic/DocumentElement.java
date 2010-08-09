/*
 * Copyright 2002 - 2009 JEuclid, http://jeuclid.sf.net
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

import java.awt.Color;
import java.util.List;

import net.sourceforge.jeuclid.LayoutContext;
import net.sourceforge.jeuclid.context.LayoutContextImpl;
import net.sourceforge.jeuclid.context.Parameter;
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
public final class DocumentElement extends GenericDocument implements
        MathMLDocument, JEuclidNode, DocumentView, LayoutableDocument {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a math element.
     * 
     */
    public DocumentElement() {
        this(null);
    }

    /**
     * Creates a MathML compatible document with the given DocumentType.
     * 
     * @param doctype
     *            DocumentType to use. This is currently ignored.
     */
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
        return new JEuclidView(this,
                LayoutContextImpl.getDefaultLayoutContext(), null);
    }

    /** {@inheritDoc} */
    public List<LayoutableNode> getChildrenToLayout() {
        return ElementListSupport.createListOfLayoutChildren(this);
    }

    /** {@inheritDoc} */
    public List<LayoutableNode> getChildrenToDraw() {
        return ElementListSupport.createListOfLayoutChildren(this);
    }

    /** {@inheritDoc} */
    public void layoutStage1(final LayoutView view, final LayoutInfo info,
            final LayoutStage childMinStage, final LayoutContext context) {
        ElementListSupport.layoutSequential(view, info, this
                .getChildrenToLayout(), LayoutStage.STAGE1);
        info.setLayoutStage(childMinStage);
        // TODO: This should be done in a better way.
        if (context.getParameter(Parameter.MATHBACKGROUND) == null) {
            info.setLayoutStage(childMinStage);
        } else {
            info.setLayoutStage(LayoutStage.STAGE1);
        }
    }

    /** {@inheritDoc} */
    public void layoutStage2(final LayoutView view, final LayoutInfo info,
            final LayoutContext context) {
        ElementListSupport.layoutSequential(view, info, this
                .getChildrenToLayout(), LayoutStage.STAGE2);
        ElementListSupport.addBackground((Color) context
                .getParameter(Parameter.MATHBACKGROUND), info, true);
        info.setLayoutStage(LayoutStage.STAGE2);
    }

    /** {@inheritDoc} */
    @Override
    protected Node newNode() {
        return new DocumentElement();
    }

    /** {@inheritDoc} */
    @Override
    public Element createElement(final String tagName) {
        return JEuclidElementFactory.elementFromName(null, tagName, this);
    }

    /** {@inheritDoc} */
    @Override
    public Element createElementNS(final String namespaceURI,
            final String qualifiedName) {
        final String ns;
        if (namespaceURI != null && namespaceURI.length() == 0) {
            ns = null;
        } else {
            ns = namespaceURI;
        }
        return JEuclidElementFactory.elementFromName(ns, qualifiedName, this);
    }
}
