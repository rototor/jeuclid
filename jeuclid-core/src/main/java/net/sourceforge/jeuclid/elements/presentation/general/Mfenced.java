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

package net.sourceforge.jeuclid.elements.presentation.general;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.jeuclid.Constants;
import net.sourceforge.jeuclid.elements.AbstractElementWithDelegates;
import net.sourceforge.jeuclid.elements.AbstractJEuclidElement;
import net.sourceforge.jeuclid.elements.presentation.token.Mo;
import net.sourceforge.jeuclid.elements.support.operatordict.OperatorDictionary;
import net.sourceforge.jeuclid.layout.LayoutableNode;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.Node;
import org.w3c.dom.mathml.MathMLFencedElement;

/**
 * The class represents the mfenced element.
 * 
 * @version $Revision$
 */
public final class Mfenced extends AbstractElementWithDelegates implements
        MathMLFencedElement {

    /** The separators attribute. */
    public static final String ATTR_SEPARATORS = "separators";

    /** The close attribute. */
    public static final String ATTR_CLOSE = "close";

    /** The open attribute. */
    public static final String ATTR_OPEN = "open";

    /**
     * The XML element from this class.
     */
    public static final String ELEMENT = "mfenced";

    private static final String FENCE_SPACE = "0.2em";

    private static final long serialVersionUID = 1L;

    /**
     * Default constructor. Sets MathML Namespace.
     * 
     * @param qname
     *            Qualified name.
     * @param odoc
     *            Owner Document.
     */
    public Mfenced(final String qname, final AbstractDocument odoc) {
        super(qname, odoc);
        this.setDefaultMathAttribute(Mfenced.ATTR_OPEN, "(");
        this.setDefaultMathAttribute(Mfenced.ATTR_CLOSE, ")");
        this.setDefaultMathAttribute(Mfenced.ATTR_SEPARATORS, ",");
    }

    /** {@inheritDoc} */
    @Override
    protected Node newNode() {
        return new Mfenced(this.nodeName, this.ownerDocument);
    }

    /**
     * @return opening delimiter
     */
    public String getOpen() {
        return this.getMathAttribute(Mfenced.ATTR_OPEN);
    }

    /**
     * Set the opening delimiter.
     * 
     * @param open
     *            Delimiter
     */
    public void setOpen(final String open) {
        this.setAttribute(Mfenced.ATTR_OPEN, open);
    }

    /**
     * @return Return the closing delimiter
     */
    public String getClose() {
        return this.getMathAttribute(Mfenced.ATTR_CLOSE);
    }

    /**
     * Set the closing delimiter.
     * 
     * @param close
     *            New close delimeter
     */
    public void setClose(final String close) {
        this.setAttribute(Mfenced.ATTR_CLOSE, close);
    }

    /**
     * Return the separators.
     * 
     * @return separators
     */
    public String getSeparators() {
        final StringBuilder retVal = new StringBuilder();
        final String attValue = this.getMathAttribute(Mfenced.ATTR_SEPARATORS);
        if (attValue != null) {
            for (int i = 0; i < attValue.length(); i++) {
                final char c = attValue.charAt(i);
                if (c > AbstractJEuclidElement.TRIVIAL_SPACE_MAX) {
                    retVal.append(c);
                }
            }
        }
        return retVal.toString();
    }

    /**
     * Set the separators.
     * 
     * @param separators
     *            New separators
     */
    public void setSeparators(final String separators) {
        this.setAttribute(Mfenced.ATTR_SEPARATORS, separators);
    }

    /** {@inheritDoc} */
    @Override
    protected List<LayoutableNode> createDelegates() {
        final int contentCount = this.getMathElementCount();
        final List<LayoutableNode> retVal = new ArrayList<LayoutableNode>(
                2 * contentCount + 1);

        final Mo opOpen = this.createFenceOperator();
        opOpen.setForm(OperatorDictionary.FORM_PREFIX);
        opOpen.setTextContent(this.getOpen());

        retVal.add(opOpen);
        final String sep = this.getSeparators();
        final boolean haveSep = (sep != null) && (sep.length() > 0);

        for (int i = 0; i < contentCount; i++) {
            retVal.add(this.getMathElement(i));

            if (haveSep && (i < (contentCount - 1))) {
                final Mo opSep = (Mo) this.getOwnerDocument().createElement(
                        Mo.ELEMENT);
                opSep.setSeparator(Constants.TRUE);
                if (i < sep.length()) {
                    opSep.setTextContent(String.valueOf(sep.charAt(i)));
                } else {
                    opSep.setTextContent(String.valueOf(sep
                            .charAt(sep.length() - 1)));
                }
                retVal.add(opSep);
            }
        }
        final Mo opClose = this.createFenceOperator();
        opClose.setForm(OperatorDictionary.FORM_POSTFIX);
        opClose.setTextContent(this.getClose());
        retVal.add(opClose);

        return retVal;
    }

    private Mo createFenceOperator() {
        final Mo opOpen = (Mo) this.getOwnerDocument()
                .createElement(Mo.ELEMENT);
        opOpen.setFence(Constants.TRUE);
        opOpen.setStretchy(Constants.TRUE);
        opOpen.setRspace(Mfenced.FENCE_SPACE);
        opOpen.setLspace(Mfenced.FENCE_SPACE);
        opOpen.setSymmetric(Constants.FALSE);
        return opOpen;
    }

}
