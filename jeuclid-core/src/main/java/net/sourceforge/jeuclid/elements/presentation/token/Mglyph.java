/*
 * Copyright 2007 - 2007 JEuclid, http://jeuclid.sf.net
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

package net.sourceforge.jeuclid.elements.presentation.token;

import java.awt.Font;
import java.awt.font.TextAttribute;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

import net.sourceforge.jeuclid.LayoutContext;
import net.sourceforge.jeuclid.elements.support.GraphicsSupport;
import net.sourceforge.jeuclid.elements.support.text.StringUtil;
import net.sourceforge.jeuclid.elements.support.text.TextContentModifier;
import net.sourceforge.jeuclid.font.FontFactory;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.Node;
import org.w3c.dom.mathml.MathMLGlyphElement;

/**
 * Implements the mglyph element.
 * 
 * <p>
 * TODO: FontFamliy gives a "deprecated attribute" warning due to the current
 * design.
 * <p>
 * TODO: other attributes (such as italic, bold, etc.) may be inherited from the
 * context.
 * 
 * @version $Revision$
 */
public final class Mglyph extends AbstractTokenWithTextLayout implements
        MathMLGlyphElement, TextContentModifier {

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "mglyph";

    private static final String ATTR_ALT = "alt";

    private static final String ATTR_FONTFAMILY = "fontfamily";

    private static final String ATTR_INDEX = "index";

    private static final long serialVersionUID = 1L;

    /**
     * Default constructor. Sets MathML Namespace.
     * 
     * @param qname
     *            Qualified name.
     * @param odoc
     *            Owner Document.
     */
    public Mglyph(final String qname, final AbstractDocument odoc) {
        super(qname, odoc);
    }

    /** {@inheritDoc} */
    @Override
    protected Node newNode() {
        return new Mglyph(this.nodeName, this.ownerDocument);
    }

    /** {@inheritDoc} */

    public AttributedCharacterIterator modifyTextContent(
            final AttributedCharacterIterator aci, final LayoutContext now) {
        final AttributedString retVal;
        final String ffamily = this.getFontfamily();
        final String fontFamily;
        if (ffamily == null) {
            fontFamily = "serif";
        } else {
            fontFamily = ffamily.trim();
        }
        final Font font = FontFactory.getInstance().getFont(fontFamily,
                Font.PLAIN, GraphicsSupport.getFontsizeInPoint(now));
        final int codePoint = this.getIndex();
        if ((codePoint > 0) && (font.getFamily().equalsIgnoreCase(fontFamily))
                && (font.canDisplay(codePoint))) {
            retVal = new AttributedString(new String(new int[] { codePoint },
                    0, 1));
            retVal.addAttribute(TextAttribute.FONT, font);
        } else {
            retVal = StringUtil.convertStringtoAttributedString(this.getAlt(),
                    this.getMathvariantAsVariant(), GraphicsSupport
                            .getFontsizeInPoint(now), now);
        }
        return retVal.getIterator();
    }

    /** {@inheritDoc} */
    public String getAlt() {
        return this.getMathAttribute(Mglyph.ATTR_ALT);
    }

    /** {@inheritDoc} */
    public String getFontfamily() {
        return this.getMathAttribute(Mglyph.ATTR_FONTFAMILY);
    }

    /** {@inheritDoc} */
    public int getIndex() {
        int retVal = 0;
        final String indexStr = this.getMathAttribute(Mglyph.ATTR_INDEX);
        try {
            if (indexStr != null) {
                retVal = Integer.parseInt(indexStr);
            }
        } catch (final NumberFormatException e) {
            retVal = 0;
        }
        return retVal;
    }

    /** {@inheritDoc} */
    public void setAlt(final String alt) {
        this.setAttribute(Mglyph.ATTR_ALT, alt);
    }

    /** {@inheritDoc} */
    public void setFontfamily(final String fontfamily) {
        this.setAttribute(Mglyph.ATTR_FONTFAMILY, fontfamily);
    }

    /** {@inheritDoc} */
    public void setIndex(final int index) {
        this.setAttribute(Mglyph.ATTR_INDEX, Integer.toString(index));
    }

}
