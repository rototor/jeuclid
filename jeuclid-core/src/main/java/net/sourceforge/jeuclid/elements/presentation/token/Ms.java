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

package net.sourceforge.jeuclid.elements.presentation.token;

import java.text.AttributedCharacterIterator;

import net.sourceforge.jeuclid.LayoutContext;
import net.sourceforge.jeuclid.elements.support.GraphicsSupport;
import net.sourceforge.jeuclid.elements.support.attributes.MathVariant;
import net.sourceforge.jeuclid.elements.support.text.MultiAttributedCharacterIterator;
import net.sourceforge.jeuclid.elements.support.text.StringUtil;
import net.sourceforge.jeuclid.elements.support.text.TextContentModifier;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.Node;
import org.w3c.dom.mathml.MathMLStringLitElement;

/**
 * This class represents string in a equation.
 * 
 * @version $Revision$
 */
public final class Ms extends AbstractTokenWithTextLayout implements
        MathMLStringLitElement, TextContentModifier {

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "ms";

    /** Attribute for lquote. */
    public static final String ATTR_LQUOTE = "lquote";

    /** Attribute for rquote. */
    public static final String ATTR_RQUOTE = "rquote";

    private static final String VALUE_DOUBLEQUOTE = "\"";

    private static final long serialVersionUID = 1L;

    /**
     * Default constructor. Sets MathML Namespace.
     * 
     * @param qname
     *            Qualified name.
     * @param odoc
     *            Owner Document.
     */
    public Ms(final String qname, final AbstractDocument odoc) {
        super(qname, odoc);
        this.setDefaultMathAttribute(Ms.ATTR_LQUOTE, Ms.VALUE_DOUBLEQUOTE);
        this.setDefaultMathAttribute(Ms.ATTR_RQUOTE, Ms.VALUE_DOUBLEQUOTE);
    }

    /** {@inheritDoc} */
    @Override
    protected Node newNode() {
        return new Ms(this.nodeName, this.ownerDocument);
    }

    /**
     * @param lquote
     *            Left quote
     */
    public void setLquote(final String lquote) {
        this.setAttribute(Ms.ATTR_LQUOTE, lquote);
    }

    /**
     * @return Left quote
     */
    public String getLquote() {
        return this.getMathAttribute(Ms.ATTR_LQUOTE);
    }

    /**
     * @param rquote
     *            Right quota
     */
    public void setRquote(final String rquote) {
        this.setAttribute(Ms.ATTR_RQUOTE, rquote);
    }

    /**
     * @return Right quote
     */
    public String getRquote() {
        return this.getMathAttribute(Ms.ATTR_RQUOTE);
    }

    /** {@inheritDoc} */
    public AttributedCharacterIterator modifyTextContent(
            final AttributedCharacterIterator aci,
            final LayoutContext layoutContext) {

        final float fontSizeInPoint = GraphicsSupport
                .getFontsizeInPoint(layoutContext);
        final MathVariant variant = this.getMathvariantAsVariant();

        final MultiAttributedCharacterIterator maci = new MultiAttributedCharacterIterator();
        maci.appendAttributedCharacterIterator(StringUtil
                .convertStringtoAttributedString(this.getLquote(), variant,
                        fontSizeInPoint, layoutContext).getIterator());
        maci.appendAttributedCharacterIterator(aci);
        maci.appendAttributedCharacterIterator(StringUtil
                .convertStringtoAttributedString(this.getRquote(), variant,
                        fontSizeInPoint, layoutContext).getIterator());
        return maci;
    }

}
