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

package euclid.elements.presentation.token;

import java.awt.Font;
import java.awt.font.TextAttribute;
import java.text.AttributedString;

import euclid.LayoutContext;

import org.w3c.dom.Node;
import org.w3c.dom.mathml.MathMLGlyphElement;

import euclid.elements.support.GraphicsSupport;
import euclid.elements.support.text.StringUtil;
import euclid.font.FontFactory;

/**
 * Implements the mglyph element.
 * 
 * @todo FontFamliy gives a "deprecated attribute" warning due to the current
 *       design.
 * @todo other attributes (such as italic, bold, etc.) may be inherited from
 *       the context.
 * @version $Revision$
 */
public final class Mglyph extends AbstractTokenWithTextLayout implements
        MathMLGlyphElement {

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "mglyph";

    private static final String ATTR_ALT = "alt";

    private static final String ATTR_FONTFAMILY = "fontfamily";

    private static final String ATTR_INDEX = "index";

    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public Mglyph() {
        super();
    }

    /** {@inheritDoc} */
    @Override
    protected Node newNode() {
        return new Mglyph();
    }

    /** {@inheritDoc} */
    @Override
    protected AttributedString textContentAsAttributedString(
            final LayoutContext now) {
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
        if ((font.getFamily().equalsIgnoreCase(fontFamily))
                && (font.canDisplay(codePoint))) {
            retVal = new AttributedString(new String(new int[] { codePoint },
                    0, 1));
            retVal.addAttribute(TextAttribute.FONT, font);
        } else {
            retVal = StringUtil.convertStringtoAttributedString(
                    this.getAlt(), this.getMathvariantAsVariant(),
                    GraphicsSupport.getFontsizeInPoint(now), now);
        }
        return retVal;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean isEmpty() {
        return false;
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
        return Integer.parseInt(this.getMathAttribute(Mglyph.ATTR_INDEX));
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
