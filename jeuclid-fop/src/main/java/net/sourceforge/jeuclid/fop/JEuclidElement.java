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

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.jeuclid.Constants;
import net.sourceforge.jeuclid.MutableLayoutContext;
import net.sourceforge.jeuclid.context.LayoutContextImpl;
import net.sourceforge.jeuclid.context.Parameter;
import net.sourceforge.jeuclid.layout.JEuclidView;
import net.sourceforge.jeuclid.xmlgraphics.PreloaderMathML;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.datatypes.Length;
import org.apache.fop.fo.FOEventHandler;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.properties.CommonFont;
import org.apache.fop.fo.properties.FixedLength;
import org.apache.fop.fo.properties.Property;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.fonts.FontTriplet;
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
        for (final Parameter p : Parameter.values()) {
            final String localName = p.getOptionName();
            final String attrName = "jeuclid:" + localName;
            final String isSet = e.getAttributeNS(Constants.NS_JEUCLID_EXT,
                    localName);
            if ((isSet == null) || (isSet.length() == 0)) {
                e.setAttributeNS(Constants.NS_JEUCLID_EXT, attrName, p
                        .toString(this.layoutContext.getParameter(p)));
            }
        }
    }

    private void calculate() {
        final JEuclidView view = new JEuclidView(this.doc, this.layoutContext,
                null);
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
    @SuppressWarnings("unchecked")
    @Override
    protected PropertyList createPropertyList(final PropertyList pList,
            final FOEventHandler foEventHandler) throws FOPException {
        final FOUserAgent userAgent = this.getUserAgent();
        final CommonFont commonFont = pList.getFontProps();
        final float msize = (float) (commonFont.fontSize.getNumericValue() / PreloaderMathML.MPT_FACTOR);
        final Property colorProp = pList
                .get(org.apache.fop.fo.Constants.PR_COLOR);
        if (colorProp != null) {
            final Color color = colorProp.getColor(userAgent);
            this.layoutContext.setParameter(Parameter.MATHCOLOR, color);
        }
        final Property bcolorProp = pList
                .get(org.apache.fop.fo.Constants.PR_BACKGROUND_COLOR);
        if (bcolorProp != null) {
            final Color bcolor = bcolorProp.getColor(userAgent);
            this.layoutContext.setParameter(Parameter.MATHBACKGROUND, bcolor);
        }
        final FontInfo fi = this.getFOEventHandler().getFontInfo();
        final FontTriplet[] fontkeys = commonFont.getFontState(fi);

        this.layoutContext.setParameter(Parameter.MATHSIZE, msize);
        final List<String> defaultFonts = (List<String>) this.layoutContext
                .getParameter(Parameter.FONTS_SERIF);
        final List<String> newFonts = new ArrayList<String>(fontkeys.length
                + defaultFonts.size());
        for (final FontTriplet t : fontkeys) {
            newFonts.add(t.getName());
        }
        newFonts.addAll(defaultFonts);
        this.layoutContext.setParameter(Parameter.FONTS_SERIF, newFonts);
        return super.createPropertyList(pList, foEventHandler);
    }
}
