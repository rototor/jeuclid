/*
 * Copyright 2007 - 2008 JEuclid, http://jeuclid.sf.net
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

/*
 * Please note: This file was originally taken from the Apache FOP project,
 * available at http://xmlgraphics.apache.org/fop/ It is therefore
 * partially copyright (c) 1999-2007 The Apache Software Foundation.
 * 
 * Parts of the contents are heavily inspired by work done for Barcode4J by
 * Jeremias Maerki, available at http://barcode4j.sf.net/
 */

package net.sourceforge.jeuclid.fop;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import net.sourceforge.jeuclid.LayoutContext;
import net.sourceforge.jeuclid.MutableLayoutContext;
import net.sourceforge.jeuclid.context.LayoutContextImpl;
import net.sourceforge.jeuclid.layout.JEuclidView;
import net.sourceforge.jeuclid.xmlgraphics.PreloaderMathML;

import org.apache.fop.apps.FOPException;
import org.apache.fop.datatypes.Length;
import org.apache.fop.fo.FOEventHandler;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.properties.FixedLength;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;

/**
 * Defines the top-level element for MathML.
 * 
 * @version $Revision$
 */
public class JEuclidElement extends JEuclidObj {

    private Point2D size;

    private Length baseline;

    private final MutableLayoutContext layoutContext;

    /**
     * Default constructor.
     * 
     * @param parent
     *            Parent Node in the FO tree.
     */
    public JEuclidElement(final FONode parent) {
        super(parent);
        this.layoutContext = new LayoutContextImpl(LayoutContextImpl
                .getDefaultLayoutContext());
    }

    /** {@inheritDoc} */
    @Override
    public void processNode(final String elementName, final Locator locator,
            final Attributes attlist, final PropertyList propertyList)
            throws FOPException {
        super.processNode(elementName, locator, attlist, propertyList);
        final Document d = this.createBasicDocument();
        final Element e = d.getDocumentElement();
        for (final LayoutContext.Parameter p : LayoutContext.Parameter
                .values()) {
            e.setAttributeNS(JEuclidXMLHandler.FOPEXT_NS, "jeuclid:"
                    + p.toString(), p.toString(this.layoutContext
                    .getParameter(p)));
        }
    }

    private void calculate() {
        final Image tempimage = new BufferedImage(1, 1,
                BufferedImage.TYPE_INT_ARGB);
        final Graphics2D tempg = (Graphics2D) tempimage.getGraphics();
        final JEuclidView view = new JEuclidView(this.doc,
                this.layoutContext, tempg);
        final float descent = view.getDescentHeight();
        this.size = new Point2D.Float(view.getWidth(), view.getAscentHeight()
                + descent);
        this.baseline = FixedLength.getInstance(-descent, "pt");
    }

    /** {@inheritDoc} */
    @Override
    public Point2D getDimension(final Point2D view) {
        if (this.size == null) {
            this.calculate();
        }
        return this.size;
    }

    /** {@inheritDoc} */
    @Override
    public Length getIntrinsicAlignmentAdjust() {
        if (this.baseline == null) {
            this.calculate();
        }
        return this.baseline;
    }

    /** {@inheritDoc} */
    @Override
    protected PropertyList createPropertyList(final PropertyList pList,
            final FOEventHandler foEventHandler) throws FOPException {
        this.layoutContext
                .setParameter(
                        LayoutContext.Parameter.MATHSIZE,
                        (float) (pList.getFontProps().fontSize
                                .getNumericValue() / PreloaderMathML.MPT_FACTOR));
        return super.createPropertyList(pList, foEventHandler);
    }

}
